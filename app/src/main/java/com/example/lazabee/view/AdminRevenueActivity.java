package com.example.lazabee.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lazabee.R;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.database.LabeeDao;

import java.text.DecimalFormat;

public class AdminRevenueActivity extends AppCompatActivity {

    private TextView tvTotalRevenue, tvTotalOrders, tvCompletedOrders, tvPendingOrders, tvCancelledOrders;
    private ImageView btnBack;
    private LabeeDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_revenue);

        dao = AppDatabase.getInstance(this).labeeDao();

        initViews();
        loadStatistics();
    }

    private void initViews() {
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvCompletedOrders = findViewById(R.id.tvCompletedOrders);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);
        tvCancelledOrders = findViewById(R.id.tvCancelledOrders);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadStatistics() {
        // Total Revenue
        Long revenue = dao.getTotalRevenue();
        if (revenue == null) revenue = 0L;
        
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalRevenue.setText(formatter.format(revenue) + "đ");

        // Order Counts
        int total = dao.getTotalOrders();
        int completed = dao.getOrderCountByStatus("Completed");
        int pending = dao.getOrderCountByStatus("Pending");
        int cancelled = dao.getOrderCountByStatus("Cancelled");
        // Note: "Shipping" is also a status, but for simplicity we might group it or just show these 3.
        // Let's add Shipping to Pending count or show separately? 
        // For now, let's just show these specific counts.
        // Actually, let's check if we have "Shipping" status. Yes we do.
        int shipping = dao.getOrderCountByStatus("Shipping");
        
        // Let's group Pending + Shipping as "Active/Pending" for the UI label "Đang xử lý"
        // Or just show Pending. The UI says "Đang xử lý" (Processing).
        // Let's sum Pending + Shipping for that box.
        int processing = pending + shipping;

        tvTotalOrders.setText(String.valueOf(total));
        tvCompletedOrders.setText(String.valueOf(completed));
        tvPendingOrders.setText(String.valueOf(processing));
        tvCancelledOrders.setText(String.valueOf(cancelled));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
    }
}
