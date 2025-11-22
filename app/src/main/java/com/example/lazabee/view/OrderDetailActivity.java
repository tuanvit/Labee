package com.example.lazabee.view;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.adapter.OrderItemAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Order;
import com.example.lazabee.model.OrderItemDetail;
import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvOrderId, tvOrderStatus, tvOrderDate;
    private TextView tvShippingAddress, tvPhoneNumber;
    private RecyclerView rvOrderItems;
    private TextView tvPaymentMethod, tvOrderNote;
    private TextView tvSubtotal, tvShipping, tvTotal;
    private Button btnCancelOrder, btnSimulateDelivery;
    private ProgressBar progressBar;

    private OrderItemAdapter orderItemAdapter;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String orderIdStr = getIntent().getStringExtra("orderId");
        int orderId = -1;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            // Try getting as int if passed as int
            orderId = getIntent().getIntExtra("orderId", -1);
        }

        initViews();
        setupRecyclerView();

        if (orderId != -1) {
            loadOrderDetail(orderId);
        } else {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvOrderNote = findViewById(R.id.tvOrderNote);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShippingFee);
        tvTotal = findViewById(R.id.tvTotalAmount);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnSimulateDelivery = findViewById(R.id.btnSimulateDelivery);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrderDetail(int orderId) {
        progressBar.setVisibility(View.VISIBLE);

        // Direct Database Call (MVC)
        AppDatabase db = AppDatabase.getInstance(this);
        currentOrder = db.labeeDao().getOrderById(orderId);
        List<OrderItemDetail> items = db.labeeDao().getOrderItems(orderId);

        progressBar.setVisibility(View.GONE);

        if (currentOrder != null) {
            displayOrderDetail(currentOrder, items);
        } else {
            Toast.makeText(this, "Failed to load order", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayOrderDetail(Order order, List<OrderItemDetail> items) {
        tvOrderId.setText("Order #" + order.id);
        tvOrderStatus.setText(order.status);
        tvOrderDate.setText(order.date);

        // Status Color Logic
        if ("Pending".equals(order.status)) {
            tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvOrderStatus.setBackgroundResource(R.drawable.count_bg); // You might want a blue tinted bg here
        } else if ("Completed".equals(order.status)) {
            tvOrderStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if ("Cancelled".equals(order.status)) {
            tvOrderStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Address - Hardcoded for now as we don't store address in Order table yet
        tvShippingAddress.setText("Địa chỉ mặc định");
        tvPhoneNumber.setText("0901234567");

        // Order items
        if (items != null) {
            orderItemAdapter = new OrderItemAdapter(this, items);
            rvOrderItems.setAdapter(orderItemAdapter);
        }

        // Payment
        tvPaymentMethod.setText("COD"); // Hardcoded

        // Note
        tvOrderNote.setVisibility(View.GONE);

        // Total
        DecimalFormat formatter = new DecimalFormat("#,###");
        double subtotal = order.totalPrice - 30000; // Assuming fixed shipping
        tvSubtotal.setText(formatter.format(subtotal) + "đ");
        tvShipping.setText("30,000đ");
        tvTotal.setText(formatter.format(order.totalPrice) + "đ");

        // Cancel button - only show if status is Pending
        if ("Pending".equals(order.status)) {
            btnCancelOrder.setVisibility(View.VISIBLE);
            btnCancelOrder.setOnClickListener(v -> cancelOrder(order.id));
            
            // Show Simulate Delivery button for testing
            btnSimulateDelivery.setVisibility(View.VISIBLE);
            btnSimulateDelivery.setOnClickListener(v -> simulateDelivery(order.id));
        } else {
            btnCancelOrder.setVisibility(View.GONE);
            btnSimulateDelivery.setVisibility(View.GONE);
        }
    }

    private void simulateDelivery(int orderId) {
        progressBar.setVisibility(View.VISIBLE);
        
        // Simulate a delay then update status
        new android.os.Handler().postDelayed(() -> {
            AppDatabase.getInstance(this).labeeDao().updateOrderStatus(orderId, "Completed");
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Đơn hàng đã được giao thành công (Mô phỏng)", Toast.LENGTH_SHORT).show();
            loadOrderDetail(orderId);
        }, 1500);
    }

    private void cancelOrder(int orderId) {
        progressBar.setVisibility(View.VISIBLE);
        btnCancelOrder.setEnabled(false);

        AppDatabase.getInstance(this).labeeDao().cancelOrder(orderId);

        progressBar.setVisibility(View.GONE);
        btnCancelOrder.setEnabled(true);

        Toast.makeText(this, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
        loadOrderDetail(orderId); // Reload to update status
    }
}
