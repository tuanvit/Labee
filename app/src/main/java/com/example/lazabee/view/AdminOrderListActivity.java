package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.OrderAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Order;

import java.util.List;

public class AdminOrderListActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_list);

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = AppDatabase.getInstance(this).labeeDao().getAllOrders();
        adapter = new OrderAdapter(this, orders, order -> {
            Intent intent = new Intent(this, AdminOrderDetailActivity.class);
            intent.putExtra("orderId", order.id);
            startActivity(intent);
        });
        rvOrders.setAdapter(adapter);
    }
}