package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lazabee.R;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.CartItem;
import com.example.lazabee.model.Product;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView btnBack, btnShare, btnFavorite, btnAddToWishlist;
    private ImageView ivProductImage;
    private TextView tvPrice, tvOriginalPrice, tvProductName, tvDescription;
    private TextView tvRating;
    private TextView btnSizeS, btnSizeM, btnSizeL;
    private Button btnAddToCart, btnBuyNow;
    private ProgressBar progressBar;

    private int productId;
    private Product currentProduct;
    private String selectedSize = "M";
    private String selectedColor = "Blue";
    private boolean isFavorite = false;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get product ID from intent
        productId = getIntent().getIntExtra("product_id", -1);

        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadProductDetails();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnAddToWishlist = findViewById(R.id.btnAddToWishlist);

        ivProductImage = findViewById(R.id.ivProductImage);
        tvPrice = findViewById(R.id.tvPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);
        tvProductName = findViewById(R.id.tvProductName);
        tvDescription = findViewById(R.id.tvDescription);
        tvRating = findViewById(R.id.tvRating);

        btnSizeS = findViewById(R.id.btnSizeS);
        btnSizeM = findViewById(R.id.btnSizeM);
        btnSizeL = findViewById(R.id.btnSizeL);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnShare.setOnClickListener(v -> {
            Toast.makeText(this, "Chia sẻ sản phẩm", Toast.LENGTH_SHORT).show();
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

    private void loadProductDetails() {
        progressBar.setVisibility(View.VISIBLE);

        // Direct Database Call (MVC)
        currentProduct = AppDatabase.getInstance(this).labeeDao().getProductById(productId);

        progressBar.setVisibility(View.GONE);

        if (currentProduct != null) {
            displayProductDetails(currentProduct);
        } else {
            Toast.makeText(this, "Không thể tải thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayProductDetails(Product product) {
        tvProductName.setText(product.name);

        DecimalFormat formatter = new DecimalFormat("#,###");
        tvPrice.setText(formatter.format(product.price) + "đ");

        // Hide original price and rating for simplicity
        if (tvOriginalPrice != null)
            tvOriginalPrice.setVisibility(View.GONE);
        if (tvRating != null)
            tvRating.setVisibility(View.GONE);

        if (product.stock <= 0) {
            tvDescription.setText("HẾT HÀNG\n\n" + (product.description != null ? product.description : ""));
            btnAddToCart.setEnabled(false);
            btnBuyNow.setEnabled(false);
            btnAddToCart.setText("Hết hàng");
            btnBuyNow.setText("Hết hàng");
            btnAddToCart.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            btnBuyNow.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        } else {
            tvDescription.setText("Còn lại: " + product.stock + "\n\n" + (product.description != null ? product.description : ""));
            btnAddToCart.setEnabled(true);
            btnBuyNow.setEnabled(true);
        }

        // Load image from Drawable
        int resId = 0;
        if (product.imageResName != null) {
            resId = getResources().getIdentifier(product.imageResName, "drawable", getPackageName());
        }

        if (resId != 0) {
            ivProductImage.setImageResource(resId);
        } else {
            ivProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    private void selectSize(String size) {
        selectedSize = size;
        resetSizeButtons();

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
        Toast.makeText(this, "Đã chọn màu: " + color, Toast.LENGTH_SHORT).show();
    }

    private void toggleFavorite() {
        isFavorite = !isFavorite;
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
            btnAddToWishlist.setImageResource(R.drawable.ic_favorite);
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
            btnAddToWishlist.setImageResource(R.drawable.ic_favorite_border);
            Toast.makeText(this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToCart() {
        if (currentProduct == null)
            return;

        int userId = getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);

        // Check if item already in cart
        CartItem existingItem = db.labeeDao().getCartItem(userId, productId);

        if (existingItem != null) {
            existingItem.quantity += quantity;
            db.labeeDao().updateCartItem(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.userId = userId;
            newItem.productId = productId;
            newItem.quantity = quantity;
            db.labeeDao().addToCart(newItem);
        }

        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void buyNow() {
        addToCart();
        Intent intent = new Intent(this, CartActivity.class); // Or CheckoutActivity
        startActivity(intent);
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }
}
