package com.example.lazabee.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lazabee.R;

public class ProductDetailActivity extends AppCompatActivity {
    
    private ImageView btnBack, btnShare, btnFavorite, btnAddToWishlist;
    private TextView tvPrice, tvProductName, tvDescription;
    private TextView btnSizeS, btnSizeM, btnSizeL;
    private Button btnAddToCart, btnBuyNow;
    
    private String selectedSize = "M";
    private String selectedColor = "Blue";
    private boolean isFavorite = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        
        initViews();
        setupClickListeners();
        loadProductData();
    }
    
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnAddToWishlist = findViewById(R.id.btnAddToWishlist);
        
        tvPrice = findViewById(R.id.tvPrice);
        tvProductName = findViewById(R.id.tvProductName);
        tvDescription = findViewById(R.id.tvDescription);
        
        btnSizeS = findViewById(R.id.btnSizeS);
        btnSizeM = findViewById(R.id.btnSizeM);
        btnSizeL = findViewById(R.id.btnSizeL);
        
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnShare.setOnClickListener(v -> {
            // TODO: Implement share functionality
            showToast("Chia sẻ sản phẩm");
        });
        
        btnFavorite.setOnClickListener(v -> toggleFavorite());
        btnAddToWishlist.setOnClickListener(v -> toggleFavorite());
        
        // Size selection
        btnSizeS.setOnClickListener(v -> selectSize("S"));
        btnSizeM.setOnClickListener(v -> selectSize("M"));
        btnSizeL.setOnClickListener(v -> selectSize("L"));
        
        // Color selection
        findViewById(R.id.colorRed).setOnClickListener(v -> selectColor("Red"));
        findViewById(R.id.colorBlue).setOnClickListener(v -> selectColor("Blue"));
        findViewById(R.id.colorGreen).setOnClickListener(v -> selectColor("Green"));
        
        btnAddToCart.setOnClickListener(v -> addToCart());
        btnBuyNow.setOnClickListener(v -> buyNow());
    }
    
    private void loadProductData() {
        // Load product data from intent or API
        String productName = getIntent().getStringExtra("product_name");
        String productPrice = getIntent().getStringExtra("product_price");
        
        if (productName != null) {
            tvProductName.setText(productName);
        } else {
            tvProductName.setText("Giày thể thao Nike Air Max");
        }
        
        if (productPrice != null) {
            tvPrice.setText(productPrice);
        } else {
            tvPrice.setText("$17.00");
        }
        
        tvDescription.setText("Giày thể thao cao cấp với công nghệ đệm khí tiên tiến, " +
                "thiết kế hiện đại và chất liệu bền bỉ. Phù hợp cho mọi hoạt động thể thao " +
                "và đi lại hàng ngày. Có nhiều màu sắc và kích thước để lựa chọn.");
    }
    
    private void selectSize(String size) {
        selectedSize = size;
        
        // Reset all size buttons
        resetSizeButtons();
        
        // Highlight selected size
        switch (size) {
            case "S":
                btnSizeS.setBackgroundResource(R.drawable.size_option_selected);
                btnSizeS.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                break;
            case "M":
                btnSizeM.setBackgroundResource(R.drawable.size_option_selected);
                btnSizeM.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                break;
            case "L":
                btnSizeL.setBackgroundResource(R.drawable.size_option_selected);
                btnSizeL.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                break;
        }
        
        showToast("Đã chọn size: " + size);
    }
    
    private void resetSizeButtons() {
        btnSizeS.setBackgroundResource(R.drawable.size_option_background);
        btnSizeS.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        
        btnSizeM.setBackgroundResource(R.drawable.size_option_background);
        btnSizeM.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        
        btnSizeL.setBackgroundResource(R.drawable.size_option_background);
        btnSizeL.setTextColor(ContextCompat.getColor(this, android.R.color.black));
    }
    
    private void selectColor(String color) {
        selectedColor = color;
        showToast("Đã chọn màu: " + color);
        
        // TODO: Update color selection UI
        // Reset all color options and highlight selected one
    }
    
    private void toggleFavorite() {
        isFavorite = !isFavorite;
        
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
            btnAddToWishlist.setImageResource(R.drawable.ic_favorite);
            showToast("Đã thêm vào yêu thích");
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            btnAddToWishlist.setImageResource(R.drawable.ic_favorite_border);
            showToast("Đã xóa khỏi yêu thích");
        }
    }
    
    private void addToCart() {
        String message = String.format("Đã thêm vào giỏ hàng\nSize: %s\nMàu: %s", 
                selectedSize, selectedColor);
        showToast(message);
        
        // TODO: Implement add to cart functionality
        // - Add product to cart with selected options
        // - Update cart badge count
        // - Show success animation
    }
    
    private void buyNow() {
        String message = String.format("Mua ngay\nSản phẩm: %s\nSize: %s\nMàu: %s", 
                tvProductName.getText().toString(), selectedSize, selectedColor);
        showToast(message);
        
        // TODO: Implement buy now functionality
        // - Navigate to checkout screen
        // - Pass product and selected options
    }
    
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}