package com.example.lazabee.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.concurrent.Executors;

public class AdminOrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderStatus, tvOrderDate, tvShippingAddress, tvPhoneNumber, tvPaymentMethod, tvSubtotal, tvShippingFee, tvTotalAmount, tvOrderNote;
    private RecyclerView rvOrderItems;
    private ImageView btnBack;
    private Button btnStatusPending, btnStatusShipping, btnStatusCompleted, btnStatusCancelled;
    private AppDatabase db;
    private int orderId;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        db = AppDatabase.getInstance(this);
        orderId = getIntent().getIntExtra("ORDER_ID", -1);

        if (orderId == -1) {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadOrderDetails();
    }

    private void initViews() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvOrderNote = findViewById(R.id.tvOrderNote);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        btnBack = findViewById(R.id.btnBack);

        btnStatusPending = findViewById(R.id.btnStatusPending);
        btnStatusShipping = findViewById(R.id.btnStatusShipping);
        btnStatusCompleted = findViewById(R.id.btnStatusCompleted);
        btnStatusCancelled = findViewById(R.id.btnStatusCancelled);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        btnStatusPending.setOnClickListener(v -> updateStatus("Pending"));
        btnStatusShipping.setOnClickListener(v -> updateStatus("Shipping"));
        btnStatusCompleted.setOnClickListener(v -> updateStatus("Completed"));
        btnStatusCancelled.setOnClickListener(v -> updateStatus("Cancelled"));
    }

    private void loadOrderDetails() {
        Executors.newSingleThreadExecutor().execute(() -> {
            currentOrder = db.labeeDao().getOrderById(orderId);
            List<OrderItemDetail> items = db.labeeDao().getOrderItems(orderId);

            runOnUiThread(() -> {
                if (currentOrder != null) {
                    displayOrder(currentOrder);
                    OrderItemAdapter adapter = new OrderItemAdapter(this, items);
                    rvOrderItems.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void displayOrder(Order order) {
        tvOrderId.setText("#" + order.id);
        tvOrderStatus.setText(order.status);
        tvOrderDate.setText(order.orderDate);
        tvShippingAddress.setText(order.address);
        tvPhoneNumber.setText(order.phoneNumber);
        tvPaymentMethod.setText(order.paymentMethod);

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvSubtotal.setText(formatter.format(order.totalAmount - 30000) + "đ"); // Assuming fixed shipping fee for now or calculate from items
        tvShippingFee.setText("30,000đ");
        tvTotalAmount.setText(formatter.format(order.totalAmount) + "đ");

        if (order.note != null && !order.note.isEmpty()) {
            tvOrderNote.setVisibility(View.VISIBLE);
            tvOrderNote.setText("Ghi chú: " + order.note);
        } else {
            tvOrderNote.setVisibility(View.GONE);
        }
    }

    private void updateStatus(String newStatus) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.labeeDao().updateOrderStatus(orderId, newStatus);
            runOnUiThread(() -> {
                tvOrderStatus.setText(newStatus);
                Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                // Refresh the order object
                currentOrder.status = newStatus;
            });
        });
    }
}
