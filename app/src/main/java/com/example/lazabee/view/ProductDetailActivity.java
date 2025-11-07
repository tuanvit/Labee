package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.lazabee.R;
import com.example.lazabee.data.model.Product;
import com.example.lazabee.viewmodel.CartViewModel;
import com.example.lazabee.viewmodel.ProductViewModel;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView btnBack, btnShare, btnFavorite, btnAddToWishlist;
    private ImageView ivProductImage;
    private TextView tvPrice, tvOriginalPrice, tvProductName, tvDescription;
    private TextView tvRating, tvReviewCount;
    private TextView btnSizeS, btnSizeM, btnSizeL;
    private Button btnAddToCart, btnBuyNow;
    private ProgressBar progressBar;

    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;

    private Long productId;
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
        productId = getIntent().getLongExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        initViewModels();
        setupObservers();
        setupClickListeners();

        // Load product details
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
        btnSizeL = findViewById(R.id.btnSizeL);        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initViewModels() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    private void setupObservers() {
        // Observe product detail
        productViewModel.getProductDetail().observe(this, productResponse -> {
            progressBar.setVisibility(View.GONE);

            if (productResponse != null && productResponse.isSuccess() && productResponse.getData() != null) {
                currentProduct = productResponse.getData();
                displayProductDetails(currentProduct);
            } else {
                String errorMsg = productResponse != null ? productResponse.getMessage()
                        : "Không thể tải thông tin sản phẩm";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Observe add to cart result
        cartViewModel.getAddToCartResult().observe(this, response -> {
            btnAddToCart.setEnabled(true);
            progressBar.setVisibility(View.GONE);

            if (response != null) {
                if (response.isSuccess()) {
                    Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    // Optionally update cart badge or navigate to cart
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnShare.setOnClickListener(v -> {
            // TODO: Implement share functionality
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
        productViewModel.loadProductDetail(productId);
    }

    private void displayProductDetails(Product product) {
        // Set product name
        tvProductName.setText(product.getName());

        // Set product price
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvPrice.setText(formatter.format(product.getPrice()) + "đ");

        // Set original price if different
        if (product.getOriginalPrice() != null && !product.getOriginalPrice().equals(product.getPrice())) {
            tvOriginalPrice.setVisibility(View.VISIBLE);
            tvOriginalPrice.setText(formatter.format(product.getOriginalPrice()) + "đ");
        } else {
            tvOriginalPrice.setVisibility(View.GONE);
        }

        // Set description
        tvDescription.setText(product.getDescription() != null ? product.getDescription() : "Không có mô tả");

        // Set rating if available
        if (tvRating != null && product.getRating() != null) {
            tvRating.setText(String.valueOf(product.getRating()));
        }

        // Load product image
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            String imageUrl = product.getImageUrl();
            if (!imageUrl.startsWith("http")) {
                imageUrl = "http://localhost:8080" + imageUrl;
            }

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(ivProductImage);
        }
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

        Toast.makeText(this, "Đã chọn size: " + size, Toast.LENGTH_SHORT).show();
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

        // TODO: Update color selection UI
        // Reset all color options and highlight selected one
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
        if (currentProduct == null) {
            Toast.makeText(this, "Vui lòng chờ tải thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }
        
        btnAddToCart.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        
        // Add to cart via API
        cartViewModel.addProductToCart(productId, quantity);
        
        String message = String.format("Đang thêm vào giỏ hàng\nSize: %s\nMàu: %s", 
                selectedSize, selectedColor);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    private void buyNow() {
        if (currentProduct == null) {
            Toast.makeText(this, "Vui lòng chờ tải thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        // First add to cart, then navigate to checkout
        cartViewModel.addProductToCart(productId, quantity);

        // Navigate to checkout screen
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);

        String message = String.format("Mua ngay\nSản phẩm: %s\nSize: %s\nMàu: %s",
                tvProductName.getText().toString(), selectedSize, selectedColor);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
