package com.example.lazabee.view;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.adapter.OrderItemAdapter;
import com.example.lazabee.data.model.order.OrderResponse;
import com.example.lazabee.viewmodel.OrderViewModel;
import java.text.DecimalFormat;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvOrderId, tvOrderStatus, tvOrderDate;
    private TextView tvShippingAddress, tvPhoneNumber;
    private RecyclerView rvOrderItems;
    private TextView tvPaymentMethod, tvOrderNote;
    private TextView tvSubtotal, tvShipping, tvTotal;
    private Button btnCancelOrder;
    private ProgressBar progressBar;

    private OrderViewModel orderViewModel;
    private OrderItemAdapter orderItemAdapter;
    private OrderResponse currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String orderId = getIntent().getStringExtra("orderId");

        initViews();
        initViewModel();
        setupRecyclerView();

        if (orderId != null) {
            loadOrderDetail(orderId);
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
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViewModel() {
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void setupRecyclerView() {
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrderDetail(String orderId) {
        progressBar.setVisibility(View.VISIBLE);

        orderViewModel.getOrderById(orderId).observe(this, response -> {
            progressBar.setVisibility(View.GONE);

            if (response != null && response.isSuccess() && response.getData() != null) {
                currentOrder = response.getData();
                displayOrderDetail(currentOrder);
            } else {
                String errorMessage = response != null ? response.getMessage() : "Failed to load order";
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrderDetail(OrderResponse order) {
        tvOrderId.setText("Order #" + order.getOrderId());
        tvOrderStatus.setText(order.getStatus());
        tvOrderDate.setText(order.getCreatedAt());

        // Address
        if (order.getShippingAddress() != null) {
            tvShippingAddress.setText(order.getShippingAddress().getFullAddress());
            tvPhoneNumber.setText(order.getShippingAddress().getPhoneNumber());
        }

        // Order items
        if (order.getItems() != null) {
            orderItemAdapter = new OrderItemAdapter(this, order.getItems());
            rvOrderItems.setAdapter(orderItemAdapter);
        }

        // Payment
        tvPaymentMethod.setText(order.getPaymentMethod());
        if (order.getNote() != null && !order.getNote().isEmpty()) {
            tvOrderNote.setVisibility(View.VISIBLE);
            tvOrderNote.setText("Note: " + order.getNote());
        } else {
            tvOrderNote.setVisibility(View.GONE);
        }

        // Total
        DecimalFormat formatter = new DecimalFormat("#,###");
        double subtotal = order.getTotalAmount() - 30000; // Assuming fixed shipping
        tvSubtotal.setText(formatter.format(subtotal) + "đ");
        tvShipping.setText("30,000đ");
        tvTotal.setText(formatter.format(order.getTotalAmount()) + "đ");

        // Cancel button - only show if status is PENDING
        if ("PENDING".equals(order.getStatus())) {
            btnCancelOrder.setVisibility(View.VISIBLE);
            btnCancelOrder.setOnClickListener(v -> cancelOrder(order.getOrderId()));
        } else {
            btnCancelOrder.setVisibility(View.GONE);
        }
    }

    private void cancelOrder(String orderId) {
        progressBar.setVisibility(View.VISIBLE);
        btnCancelOrder.setEnabled(false);

        orderViewModel.cancelOrder(orderId).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            btnCancelOrder.setEnabled(true);

            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
                loadOrderDetail(orderId); // Reload to update status
            } else {
                String errorMessage = response != null ? response.getMessage() : "Failed to cancel order";
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
