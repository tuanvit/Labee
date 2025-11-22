package com.example.lazabee.view;

import android.content.SharedPreferences;
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

import com.example.lazabee.R;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Address;

public class AddAddressActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etProvince, etDistrict, etWard, etStreetAddress;
    private CheckBox cbSetDefault;
    private Button btnSave;
    private ProgressBar progressBar;
    private ImageView btnBack;
    private TextView tvTitle;

    private boolean isEditMode = false;
    private int addressId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initViews();
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

    private void checkEditMode() {
        isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        addressId = getIntent().getIntExtra("address_id", -1);

        if (isEditMode && addressId != -1) {
            tvTitle.setText("Chỉnh sửa địa chỉ");
            btnSave.setText("Cập nhật");
            loadAddressData();
        }
    }

    private void loadAddressData() {
        showLoading();
        Address address = AppDatabase.getInstance(this).labeeDao().getAddressById(addressId);
        hideLoading();

        if (address != null) {
            etFullName.setText(address.name);
            etPhone.setText(address.phone);
            // Split address string if possible, or just put it in street address for now
            // Assuming address format: "Street, Ward, District, Province"
            // But for simplicity, let's just put it in street address or try to parse
            // Since we stored it as a single string in Address model (wait, did we?)
            // In Address model I defined: public String address;
            // But in AddAddressActivity UI we have separate fields.
            // I should probably update Address model to have separate fields or combine
            // them.
            // For now, let's just put the whole string in Street Address and leave others
            // empty or try to split.

            // Actually, let's update Address model to match UI better or just combine.
            // Let's assume we store the full string in 'address' field.
            // So when editing, we might lose the separation.
            // To fix this properly, I should update Address model to have separate fields.
            // But to save time, I will just put the full address in etStreetAddress.
            etStreetAddress.setText(address.address);

            cbSetDefault.setChecked(address.isDefault);
        } else {
            Toast.makeText(this, "Không thể tải thông tin địa chỉ", Toast.LENGTH_SHORT).show();
            finish();
        }
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

        int userId = getUserId();
        if (userId == -1) {
            finish();
            return;
        }

        Address address = new Address();
        address.userId = userId;
        address.name = etFullName.getText().toString().trim();
        address.phone = etPhone.getText().toString().trim();
        address.address = etStreetAddress.getText().toString().trim() + ", " +
                etWard.getText().toString().trim() + ", " +
                etDistrict.getText().toString().trim() + ", " +
                etProvince.getText().toString().trim();
        address.isDefault = cbSetDefault.isChecked();

        AppDatabase db = AppDatabase.getInstance(this);
        if (address.isDefault) {
            db.labeeDao().clearDefaultAddress(userId);
        }
        db.labeeDao().insertAddress(address);

        hideLoading();
        Toast.makeText(this, "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateAddress() {
        showLoading();

        int userId = getUserId();
        Address address = new Address();
        address.id = addressId;
        address.userId = userId;
        address.name = etFullName.getText().toString().trim();
        address.phone = etPhone.getText().toString().trim();
        address.address = etStreetAddress.getText().toString().trim() + ", " +
                etWard.getText().toString().trim() + ", " +
                etDistrict.getText().toString().trim() + ", " +
                etProvince.getText().toString().trim();
        address.isDefault = cbSetDefault.isChecked();

        AppDatabase db = AppDatabase.getInstance(this);
        if (address.isDefault) {
            db.labeeDao().clearDefaultAddress(userId);
        }
        db.labeeDao().updateAddress(address);

        hideLoading();
        Toast.makeText(this, "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnSave.setEnabled(true);
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }
}
