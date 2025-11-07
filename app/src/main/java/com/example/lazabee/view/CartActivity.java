package com.example.lazabee.view;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.CartItemAdapter;
import com.example.lazabee.data.model.CartItem;
import com.example.lazabee.viewmodel.CartViewModel;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnCartItemListener {

    private ImageView btnBack;
    private TextView tvCartCount, tvTotalPrice, tvEmptyCart;
    private RecyclerView rvCartItems;
    private Button btnCheckout;
    private ProgressBar progressBar;
    private LinearLayout layoutEmptyCart;

    private CartViewModel cartViewModel;
    private CartItemAdapter cartItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        initViewModel();
        setupRecyclerView();
        setupObservers();
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

    private void initViewModel() {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    private void setupRecyclerView() {
        cartItemAdapter = new CartItemAdapter(this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartItemAdapter);
    }

    private void setupObservers() {
        // Observe cart items
        cartViewModel.getCartItems().observe(this, cartResponse -> {
            progressBar.setVisibility(View.GONE);

            if (cartResponse != null && cartResponse.isSuccess() && cartResponse.getData() != null) {
                cartItemAdapter.setCartItems(cartResponse.getData());

                if (cartResponse.getData().isEmpty()) {
                    showEmptyCart();
                } else {
                    showCartItems();
                    updateCartSummary(cartResponse.getData());
                }
            } else {
                showEmptyCart();
            }
        });

        // Observe update result
        cartViewModel.getUpdateCartResult().observe(this, response -> {
            if (response != null) {
                if (!response.isSuccess()) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // Reload cart after update
                loadCart();
            }
        });

        // Observe delete result
        cartViewModel.getDeleteCartItemResult().observe(this, response -> {
            if (response != null) {
                if (response.isSuccess()) {
                    Toast.makeText(this, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // Reload cart after delete
                loadCart();
            }
        });
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
        cartViewModel.getCart();
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

    private void updateCartSummary(java.util.List<CartItem> cartItems) {
        int totalItems = 0;
        double totalPrice = 0;

        for (CartItem item : cartItems) {
            totalItems += item.getQuantity();
            totalPrice += item.getPrice() * item.getQuantity();
        }

        tvCartCount.setText(String.valueOf(totalItems));

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvTotalPrice.setText(formatter.format(totalPrice) + "đ");
    }

        @Override
    public void onQuantityChanged(CartItem cartItem, int newQuantity) {
        // Update cart item quantity via API
        cartViewModel.updateCart(cartItem.getId(), newQuantity);
    }
    
    @Override
    public void onDeleteItem(CartItem cartItem) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Delete cart item via API
                    cartViewModel.deleteItem(cartItem.getId());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
