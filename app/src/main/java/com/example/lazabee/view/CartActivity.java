package com.example.lazabee.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lazabee.R;

public class CartActivity extends AppCompatActivity {
    
    private ImageView btnBack, btnEditAddress;
    private TextView tvCartCount, tvShippingAddress, tvTotalPrice;
    private TextView tvQuantityItem1, tvQuantityItem2;
    private ImageView btnDeleteItem1, btnDeleteItem2;
    private ImageView btnDecreaseItem1, btnIncreaseItem1;
    private ImageView btnDecreaseItem2, btnIncreaseItem2;
    private ImageView btnDeleteWishlist1, btnAddToCartFromWishlist1, btnAddToCartFromWishlist2;
    private Button btnCheckout;
    
    private int quantityItem1 = 1;
    private int quantityItem2 = 1;
    private double priceItem1 = 17.00;
    private double priceItem2 = 17.00;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        initViews();
        setupClickListeners();
        updateTotal();
    }
    
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEditAddress = findViewById(R.id.btnEditAddress);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        
        // Cart items
        tvQuantityItem1 = findViewById(R.id.tvQuantityItem1);
        tvQuantityItem2 = findViewById(R.id.tvQuantityItem2);
        btnDeleteItem1 = findViewById(R.id.btnDeleteItem1);
        btnDeleteItem2 = findViewById(R.id.btnDeleteItem2);
        btnDecreaseItem1 = findViewById(R.id.btnDecreaseItem1);
        btnIncreaseItem1 = findViewById(R.id.btnIncreaseItem1);
        btnDecreaseItem2 = findViewById(R.id.btnDecreaseItem2);
        btnIncreaseItem2 = findViewById(R.id.btnIncreaseItem2);
        
        // Wishlist items
        btnDeleteWishlist1 = findViewById(R.id.btnDeleteWishlist1);
        btnAddToCartFromWishlist1 = findViewById(R.id.btnAddToCartFromWishlist1);
        btnAddToCartFromWishlist2 = findViewById(R.id.btnAddToCartFromWishlist2);
        
        btnCheckout = findViewById(R.id.btnCheckout);
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnEditAddress.setOnClickListener(v -> {
            showToast("Chỉnh sửa địa chỉ giao hàng");
            // TODO: Open address edit dialog or activity
        });
        
        // Cart item 1 controls
        btnDecreaseItem1.setOnClickListener(v -> {
            if (quantityItem1 > 1) {
                quantityItem1--;
                tvQuantityItem1.setText(String.valueOf(quantityItem1));
                updateTotal();
            }
        });
        
        btnIncreaseItem1.setOnClickListener(v -> {
            quantityItem1++;
            tvQuantityItem1.setText(String.valueOf(quantityItem1));
            updateTotal();
        });
        
        btnDeleteItem1.setOnClickListener(v -> {
            showToast("Đã xóa sản phẩm khỏi giỏ hàng");
            quantityItem1 = 0;
            updateTotal();
            updateCartCount();
            // TODO: Hide item or remove from list
        });
        
        // Cart item 2 controls
        btnDecreaseItem2.setOnClickListener(v -> {
            if (quantityItem2 > 1) {
                quantityItem2--;
                tvQuantityItem2.setText(String.valueOf(quantityItem2));
                updateTotal();
            }
        });
        
        btnIncreaseItem2.setOnClickListener(v -> {
            quantityItem2++;
            tvQuantityItem2.setText(String.valueOf(quantityItem2));
            updateTotal();
        });
        
        btnDeleteItem2.setOnClickListener(v -> {
            showToast("Đã xóa sản phẩm khỏi giỏ hàng");
            quantityItem2 = 0;
            updateTotal();
            updateCartCount();
            // TODO: Hide item or remove from list
        });
        
        // Wishlist controls
        btnDeleteWishlist1.setOnClickListener(v -> {
            showToast("Đã xóa khỏi danh sách yêu thích");
            // TODO: Remove from wishlist
        });
        
        btnAddToCartFromWishlist1.setOnClickListener(v -> {
            showToast("Đã thêm vào giỏ hàng từ danh sách yêu thích");
            updateCartCount();
            // TODO: Add to cart and remove from wishlist
        });
        
        btnAddToCartFromWishlist2.setOnClickListener(v -> {
            showToast("Đã thêm vào giỏ hàng từ danh sách yêu thích");
            updateCartCount();
            // TODO: Add to cart and remove from wishlist
        });
        
        btnCheckout.setOnClickListener(v -> {
            if (getTotalItems() > 0) {
                showToast("Chuyển đến trang thanh toán");
                // TODO: Navigate to checkout activity
            } else {
                showToast("Giỏ hàng trống");
            }
        });
    }
    
    private void updateTotal() {
        double total = (quantityItem1 * priceItem1) + (quantityItem2 * priceItem2);
        tvTotalPrice.setText(String.format("$%.2f", total));
    }
    
    private void updateCartCount() {
        int totalItems = getTotalItems();
        tvCartCount.setText(String.valueOf(totalItems));
    }
    
    private int getTotalItems() {
        return quantityItem1 + quantityItem2;
    }
    
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}