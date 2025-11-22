package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.CartItemAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.CartItem;
import com.example.lazabee.model.CartItemDetail;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnCartItemListener {

    private ImageView btnBack;
    private TextView tvCartCount, tvTotalPrice, tvEmptyCart;
    private RecyclerView rvCartItems;
    private Button btnCheckout;
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyCart;

    private CartItemAdapter cartItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupRecyclerView();
        setupClickListeners();

        // Load cart items
        loadCart();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        rvCartItems = findViewById(R.id.rvCartItems);
        btnCheckout = findViewById(R.id.btnCheckout);
        progressBar = findViewById(R.id.progressBar);
        layoutEmptyCart = findViewById(R.id.layoutEmptyCart);
    }

    private void setupRecyclerView() {
        cartItemAdapter = new CartItemAdapter(this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartItemAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCheckout.setOnClickListener(v -> {
            // Navigate to checkout
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        });
    }

    private void loadCart() {
        progressBar.setVisibility(View.VISIBLE);

        int userId = getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Direct Database Call (MVC)
        List<CartItemDetail> cartItems = AppDatabase.getInstance(this).labeeDao().getCartItemDetails(userId);

        progressBar.setVisibility(View.GONE);

        if (cartItems != null && !cartItems.isEmpty()) {
            cartItemAdapter.setCartItems(cartItems);
            showCartItems();
            updateCartSummary(cartItems);
        } else {
            showEmptyCart();
        }
    }

    private void showEmptyCart() {
        rvCartItems.setVisibility(View.GONE);
        layoutEmptyCart.setVisibility(View.VISIBLE);
        btnCheckout.setEnabled(false);
        tvCartCount.setText("0");
        tvTotalPrice.setText("0đ");
    }

    private void showCartItems() {
        rvCartItems.setVisibility(View.VISIBLE);
        layoutEmptyCart.setVisibility(View.GONE);
        btnCheckout.setEnabled(true);
    }

    private void updateCartSummary(List<CartItemDetail> cartItems) {
        int totalItems = 0;
        double totalPrice = 0;

        for (CartItemDetail item : cartItems) {
            totalItems += item.quantity;
            totalPrice += item.price * item.quantity;
        }

        tvCartCount.setText(String.valueOf(totalItems));

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalPrice.setText(formatter.format(totalPrice) + "đ");
    }

    @Override
    public void onQuantityChanged(CartItemDetail cartItemDetail, int newQuantity) {
        // Update cart item quantity via DB
        AppDatabase db = AppDatabase.getInstance(this);
        CartItem item = db.labeeDao().getCartItemById(cartItemDetail.id);
        if (item != null) {
            item.quantity = newQuantity;
            db.labeeDao().updateCartItem(item);
            loadCart(); // Reload to update UI
        }
    }

    @Override
    public void onDeleteItem(CartItemDetail cartItemDetail) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Delete cart item via DB
                    AppDatabase db = AppDatabase.getInstance(this);
                    CartItem item = db.labeeDao().getCartItemById(cartItemDetail.id);
                    if (item != null) {
                        db.labeeDao().deleteCartItem(item);
                        Toast.makeText(this, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                        loadCart(); // Reload
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }
}
