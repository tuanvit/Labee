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
    private com.example.lazabee.model.Address currentAddress;

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
        int userId = getUserId();
        List<com.example.lazabee.model.Address> addresses = AppDatabase.getInstance(this).labeeDao()
                .getAddresses(userId);

        if (addresses != null && !addresses.isEmpty()) {
            com.example.lazabee.model.Address defaultAddress = null;
            for (com.example.lazabee.model.Address addr : addresses) {
                if (addr.isDefault) {
                    defaultAddress = addr;
                    break;
                }
            }

            // If no default set, pick the first one
            if (defaultAddress == null) {
                defaultAddress = addresses.get(0);
            }

            currentAddress = defaultAddress;
            tvShippingAddress.setText(defaultAddress.name + " (" + defaultAddress.phone + ")");
            tvAddressDetails.setText(defaultAddress.address);
            btnPlaceOrder.setEnabled(true);
        } else {
            currentAddress = null;
            tvShippingAddress.setText("Chưa có địa chỉ");
            tvAddressDetails.setText("Vui lòng thêm địa chỉ giao hàng");
            btnPlaceOrder.setEnabled(false);
        }
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
            Intent intent = new Intent(this, AddressManagementActivity.class);
            startActivity(intent);
        });

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDefaultAddress();
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
        
        if (currentAddress != null) {
            order.address = currentAddress.address;
            order.phoneNumber = currentAddress.phone;
        } else {
            order.address = "N/A";
            order.phoneNumber = "N/A";
        }
        
        order.note = etOrderNote.getText().toString();
        
        int selectedPaymentId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedPaymentId == R.id.rbCOD) {
            order.paymentMethod = "COD";
        } else if (selectedPaymentId == R.id.rbBankTransfer) {
            order.paymentMethod = "Bank Transfer";
        } else if (selectedPaymentId == R.id.rbCreditCard) {
            order.paymentMethod = "Credit Card";
        } else if (selectedPaymentId == R.id.rbEWallet) {
            order.paymentMethod = "E-Wallet";
        } else {
            order.paymentMethod = "Unknown";
        }

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
