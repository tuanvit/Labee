package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.adapter.OrderAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView rvOrders;
    private ProgressBar progressBar;
    private OrderAdapter orderAdapter;

    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        initViews();
        setupRecyclerView();
        loadOrders();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        rvOrders = findViewById(R.id.rvOrders);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(this, orderList, order -> {
            // Intent intent = new Intent(OrderHistoryActivity.this,
            // OrderDetailActivity.class);
            // intent.putExtra("orderId", order.id);
            // startActivity(intent);
            Toast.makeText(this, "Chi tiết đơn hàng: " + order.id, Toast.LENGTH_SHORT).show();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);

        int userId = getUserId();
        if (userId == -1) {
            finish();
            return;
        }

        // Direct Database Call (MVC)
        List<Order> orders = AppDatabase.getInstance(this).labeeDao().getOrders(userId);

        progressBar.setVisibility(View.GONE);

        if (orders != null && !orders.isEmpty()) {
            orderList.clear();
            orderList.addAll(orders);
            orderAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Bạn chưa có đơn hàng nào", Toast.LENGTH_SHORT).show();
        }
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }
}
