package com.example.lazabee.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.AddressAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressManagementActivity extends AppCompatActivity implements AddressAdapter.OnAddressActionListener {

    private RecyclerView rvAddresses;
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyState;
    private ImageView btnBack, btnAddAddress;

    private AddressAdapter addressAdapter;
    private List<Address> addressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);

        initViews();
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

        int userId = getUserId();
        if (userId == -1) {
            finish();
            return;
        }

        // Direct Database Call (MVC)
        List<Address> addresses = AppDatabase.getInstance(this).labeeDao().getAddresses(userId);

        hideLoading();

        if (addresses != null) {
            addressList.clear();
            addressList.addAll(addresses);
            addressAdapter.updateAddresses(addressList);

            if (addressList.isEmpty()) {
                showEmptyState();
            } else {
                hideEmptyState();
            }
        } else {
            showEmptyState();
        }
    }

    @Override
    public void onSetDefault(Address address) {
        int userId = getUserId();
        AppDatabase db = AppDatabase.getInstance(this);
        db.labeeDao().clearDefaultAddress(userId);
        db.labeeDao().setDefaultAddress(address.id);

        Toast.makeText(this, "Đã đặt làm địa chỉ mặc định", Toast.LENGTH_SHORT).show();
        loadAddresses(); // Reload to update UI
    }

    @Override
    public void onEdit(Address address) {
        Intent intent = new Intent(this, AddAddressActivity.class);
        intent.putExtra("address_id", address.id);
        intent.putExtra("edit_mode", true);
        startActivity(intent);
    }

    @Override
    public void onDelete(Address address) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa địa chỉ")
                .setMessage("Bạn có chắc muốn xóa địa chỉ này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    AppDatabase.getInstance(this).labeeDao().deleteAddress(address);
                    Toast.makeText(this, "Đã xóa địa chỉ", Toast.LENGTH_SHORT).show();
                    loadAddresses(); // Reload
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

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }
}
