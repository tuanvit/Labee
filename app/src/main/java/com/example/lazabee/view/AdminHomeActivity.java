package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lazabee.R;

public class AdminHomeActivity extends AppCompatActivity {

    private Button btnManageProducts, btnManageOrders, btnRevenueStats, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnManageProducts = findViewById(R.id.btnManageProducts);
        btnManageOrders = findViewById(R.id.btnManageOrders);
        btnRevenueStats = findViewById(R.id.btnRevenueStats);
        btnLogout = findViewById(R.id.btnLogout);

        btnManageProducts.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminProductListActivity.class));
        });

        btnManageOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminOrderListActivity.class));
        });

        btnRevenueStats.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminRevenueActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}