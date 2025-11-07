package com.example.lazabee.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lazabee.R;
import com.example.lazabee.data.model.address.AddressRequest;
import com.example.lazabee.viewmodel.AddressViewModel;

public class AddAddressActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etProvince, etDistrict, etWard, etStreetAddress;
    private CheckBox cbSetDefault;
    private Button btnSave;
    private ProgressBar progressBar;
    private ImageView btnBack;
    private TextView tvTitle;

    private AddressViewModel addressViewModel;
    private boolean isEditMode = false;
    private String addressId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initViews();
        initViewModel();
        checkEditMode();
        setupClickListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etProvince = findViewById(R.id.etProvince);
        etDistrict = findViewById(R.id.etDistrict);
        etWard = findViewById(R.id.etWard);
        etStreetAddress = findViewById(R.id.etStreetAddress);
        cbSetDefault = findViewById(R.id.cbSetDefault);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void initViewModel() {
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
    }

    private void checkEditMode() {
        isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        addressId = getIntent().getStringExtra("address_id");

        if (isEditMode && addressId != null) {
            tvTitle.setText("Chỉnh sửa địa chỉ");
            btnSave.setText("Cập nhật");
            loadAddressData();
        }
    }

    private void loadAddressData() {
        showLoading();
        addressViewModel.getAddressById(addressId).observe(this, response -> {
            hideLoading();
            if (response != null && response.isSuccess() && response.getData() != null) {
                var address = response.getData();
                etFullName.setText(address.getFullName());
                etPhone.setText(address.getPhoneNumber());
                etProvince.setText(address.getProvince());
                etDistrict.setText(address.getDistrict());
                etWard.setText(address.getWard());
                etStreetAddress.setText(address.getStreetAddress());
                cbSetDefault.setChecked(address.isDefault());
            } else {
                Toast.makeText(this, "Không thể tải thông tin địa chỉ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                if (isEditMode) {
                    updateAddress();
                } else {
                    createAddress();
                }
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etFullName.getText().toString().trim())) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return false;
        }

        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return false;
        }

        if (phone.length() < 10) {
            etPhone.setError("Số điện thoại không hợp lệ");
            etPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etProvince.getText().toString().trim())) {
            etProvince.setError("Vui lòng nhập tỉnh/thành phố");
            etProvince.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etDistrict.getText().toString().trim())) {
            etDistrict.setError("Vui lòng nhập quận/huyện");
            etDistrict.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etWard.getText().toString().trim())) {
            etWard.setError("Vui lòng nhập phường/xã");
            etWard.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etStreetAddress.getText().toString().trim())) {
            etStreetAddress.setError("Vui lòng nhập địa chỉ cụ thể");
            etStreetAddress.requestFocus();
            return false;
        }

        return true;
    }

    private void createAddress() {
        showLoading();

        AddressRequest request = new AddressRequest(
                etFullName.getText().toString().trim(),
                etPhone.getText().toString().trim(),
                etProvince.getText().toString().trim(),
                etDistrict.getText().toString().trim(),
                etWard.getText().toString().trim(),
                etStreetAddress.getText().toString().trim(),
                cbSetDefault.isChecked());

        addressViewModel.createAddress(request).observe(this, response -> {
            hideLoading();
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String errorMsg = (response != null && response.getMessage() != null)
                        ? response.getMessage()
                        : "Không thể thêm địa chỉ";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAddress() {
        showLoading();

        AddressRequest request = new AddressRequest(
                etFullName.getText().toString().trim(),
                etPhone.getText().toString().trim(),
                etProvince.getText().toString().trim(),
                etDistrict.getText().toString().trim(),
                etWard.getText().toString().trim(),
                etStreetAddress.getText().toString().trim(),
                cbSetDefault.isChecked());

        addressViewModel.updateAddress(addressId, request).observe(this, response -> {
            hideLoading();
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String errorMsg = (response != null && response.getMessage() != null)
                        ? response.getMessage()
                        : "Không thể cập nhật địa chỉ";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnSave.setEnabled(true);
    }
}
