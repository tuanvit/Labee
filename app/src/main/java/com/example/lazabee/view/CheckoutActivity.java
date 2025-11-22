package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.adapter.CheckoutItemAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.CartItemDetail;
import com.example.lazabee.model.Order;
import com.example.lazabee.model.OrderItem;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvShippingAddress, tvAddressDetails, tvChangeAddress;
    private RecyclerView rvCheckoutItems;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbCOD, rbBankTransfer, rbCreditCard, rbEWallet;
    private EditText etOrderNote;
    private TextView tvSubtotal, tvShipping, tvTotal;
    private Button btnPlaceOrder;
    private ProgressBar progressBar;

    private CheckoutItemAdapter checkoutAdapter;
    private List<CartItemDetail> cartItems = new ArrayList<>();

    private double subtotal = 0;
    private double shippingFee = 30000; // Fixed 30k

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupRecyclerView();
        loadCartItems();
        loadDefaultAddress();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvAddressDetails = findViewById(R.id.tvAddressDetails);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        rvCheckoutItems = findViewById(R.id.rvCheckoutItems);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rbCOD = findViewById(R.id.rbCOD);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);
        rbCreditCard = findViewById(R.id.rbCreditCard);
        rbEWallet = findViewById(R.id.rbEWallet);
        etOrderNote = findViewById(R.id.etOrderNote);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        progressBar = findViewById(R.id.progressBar);

        // Set default payment method
        rbCOD.setChecked(true);
    }

    private void setupRecyclerView() {
        checkoutAdapter = new CheckoutItemAdapter(this, cartItems);
        rvCheckoutItems.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutItems.setAdapter(checkoutAdapter);
    }

    private void loadCartItems() {
        progressBar.setVisibility(View.VISIBLE);

        int userId = getUserId();
        if (userId == -1) {
            finish();
            return;
        }

        // Direct Database Call (MVC)
        List<CartItemDetail> items = AppDatabase.getInstance(this).labeeDao().getCartItemDetails(userId);

        progressBar.setVisibility(View.GONE);

        if (items != null) {
            cartItems.clear();
            cartItems.addAll(items);
            checkoutAdapter.notifyDataSetChanged();
            calculateTotal();
        }
    }

    private void loadDefaultAddress() {
        // For now, just show a dummy address or user's info
        // In a real app, we would query the Address table
        tvShippingAddress.setText("Địa chỉ mặc định");
        tvAddressDetails.setText("123 Đường ABC, Quận 1, TP.HCM\n0901234567");
        btnPlaceOrder.setEnabled(true);
    }

    private void calculateTotal() {
        subtotal = 0;
        for (CartItemDetail item : cartItems) {
            subtotal += item.price * item.quantity;
        }

        double total = subtotal + shippingFee;

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvSubtotal.setText(formatter.format(subtotal) + "đ");
        tvShipping.setText(formatter.format(shippingFee) + "đ");
        tvTotal.setText(formatter.format(total) + "đ");
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        tvChangeAddress.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnPlaceOrder.setEnabled(false);

        int userId = getUserId();

        // Create Order
        Order order = new Order();
        order.userId = userId;
        order.totalPrice = (int) (subtotal + shippingFee);
        order.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        order.status = "Pending";

        AppDatabase db = AppDatabase.getInstance(this);
        long orderId = db.labeeDao().insertOrder(order);

        // Create Order Items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDetail cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.orderId = (int) orderId;
            orderItem.productId = cartItem.productId;
            orderItem.quantity = cartItem.quantity;
            orderItem.price = cartItem.price;
            orderItems.add(orderItem);
        }
        db.labeeDao().insertOrderItems(orderItems);

        // Clear Cart
        db.labeeDao().clearCart(userId);

        progressBar.setVisibility(View.GONE);
        btnPlaceOrder.setEnabled(true);

        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

        // Navigate to OrderSuccessActivity
        Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
        intent.putExtra("orderId", String.valueOf(orderId));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }
}
