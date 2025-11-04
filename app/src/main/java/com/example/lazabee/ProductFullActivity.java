package com.example.lazabee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lazabee.view.CartActivity;
import com.example.lazabee.view.ProductDetailActivity;
import com.example.lazabee.view.UserProfileActivity;


public class ProductFullActivity extends AppCompatActivity {

    private ImageView ivUserAvatar;
    private LinearLayout navHome, navFavorite, navMenu, navCart, navProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productfull);

        initViews();
        setupClickListeners();
    }
    
    private void initViews() {
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        navHome = findViewById(R.id.navHome);
        navFavorite = findViewById(R.id.navFavorite);
        navMenu = findViewById(R.id.navMenu);
        navCart = findViewById(R.id.navCart);
        navProfile = findViewById(R.id.navProfile);
    }
    
    private void setupClickListeners() {
        // Setup avatar click listener
        ivUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        });
        
        // Bottom navigation click listeners
        navHome.setOnClickListener(v -> {
            // Already on home, do nothing or scroll to top
        });
        
        navFavorite.setOnClickListener(v -> {
            // TODO: Navigate to favorites activity
            showToast("Yêu thích");
        });
        
        navMenu.setOnClickListener(v -> {
            // TODO: Navigate to menu/categories activity
            showToast("Menu");
        });
        
        navCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        });
        
        // Add click listeners for product items
        setupProductClickListeners();
    }
    
    private void setupProductClickListeners() {
        // Top Products section
        findViewById(R.id.productItem1).setOnClickListener(v -> 
            openProductDetail("Giày thể thao", "$29.99"));
        
        findViewById(R.id.productItem2).setOnClickListener(v -> 
            openProductDetail("Áo thun nam", "$19.99"));
        
        findViewById(R.id.productItem3).setOnClickListener(v -> 
            openProductDetail("Túi xách nữ", "$39.99"));
        
        // New Items section
        findViewById(R.id.newItem1).setOnClickListener(v -> 
            openProductDetail("Áo khoác mới", "$49.99"));
        
        findViewById(R.id.newItem2).setOnClickListener(v -> 
            openProductDetail("Quần jean", "$35.99"));
    }
    
    private void openProductDetail(String productName, String productPrice) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_name", productName);
        intent.putExtra("product_price", productPrice);
        startActivity(intent);
    }
    
    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }



}
