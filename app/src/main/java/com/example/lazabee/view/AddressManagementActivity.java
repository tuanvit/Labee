package com.example.lazabee.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.AddressAdapter;
import com.example.lazabee.data.model.address.AddressResponse;
import com.example.lazabee.viewmodel.AddressViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddressManagementActivity extends AppCompatActivity implements AddressAdapter.OnAddressActionListener {

    private RecyclerView rvAddresses;
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyState;
    private ImageView btnBack, btnAddAddress;

    private AddressViewModel addressViewModel;
    private AddressAdapter addressAdapter;
    private List<AddressResponse> addressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);

        initViews();
        initViewModel();
        setupRecyclerView();
        setupClickListeners();
        loadAddresses();
    }

    private void initViews() {
        rvAddresses = findViewById(R.id.rvAddresses);
        progressBar = findViewById(R.id.progressBar);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnBack = findViewById(R.id.btnBack);
        btnAddAddress = findViewById(R.id.btnAddAddress);
    }

    private void initViewModel() {
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
    }

    private void setupRecyclerView() {
        addressAdapter = new AddressAdapter(this, addressList, this);
        rvAddresses.setLayoutManager(new LinearLayoutManager(this));
        rvAddresses.setAdapter(addressAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAddressActivity.class);
            startActivity(intent);
        });
    }

    private void loadAddresses() {
        showLoading();

        addressViewModel.getAllAddresses().observe(this, response -> {
            hideLoading();

            if (response != null && response.isSuccess() && response.getData() != null) {
                addressList.clear();
                addressList.addAll(response.getData());
                addressAdapter.updateAddresses(addressList);

                if (addressList.isEmpty()) {
                    showEmptyState();
                } else {
                    hideEmptyState();
                }
            } else {
                String errorMsg = (response != null && response.getMessage() != null)
                        ? response.getMessage()
                        : "Không thể tải danh sách địa chỉ";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                showEmptyState();
            }
        });
    }

    @Override
    public void onSetDefault(AddressResponse address) {
        addressViewModel.setDefaultAddress(address.getAddressId()).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Đã đặt làm địa chỉ mặc định", Toast.LENGTH_SHORT).show();
                loadAddresses(); // Reload to update UI
            } else {
                String errorMsg = (response != null && response.getMessage() != null)
                        ? response.getMessage()
                        : "Không thể đặt địa chỉ mặc định";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEdit(AddressResponse address) {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra("address_id", address.getAddressId());
        intent.putExtra("edit_mode", true);
        startActivity(intent);
    }

    @Override
    public void onDelete(AddressResponse address) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa địa chỉ")
                .setMessage("Bạn có chắc muốn xóa địa chỉ này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    addressViewModel.deleteAddress(address.getAddressId()).observe(this, response -> {
                        if (response != null && response.isSuccess()) {
                            Toast.makeText(this, "Đã xóa địa chỉ", Toast.LENGTH_SHORT).show();
                            loadAddresses(); // Reload
                        } else {
                            String errorMsg = (response != null && response.getMessage() != null)
                                    ? response.getMessage()
                                    : "Không thể xóa địa chỉ";
                            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddresses(); // Reload when returning from AddAddressActivity
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvAddresses.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        rvAddresses.setVisibility(View.VISIBLE);
    }

    private void showEmptyState() {
        layoutEmptyState.setVisibility(View.VISIBLE);
        rvAddresses.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        layoutEmptyState.setVisibility(View.GONE);
    }
}
