package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.ProductAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Product;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;
    private ChipGroup chipGroupCategories;
    private ImageView btnSearch;
    private LinearLayout btnHome, btnCart, btnOrders, btnProfile;

    private List<Product> productList = new ArrayList<>();
    private String selectedCategory = null; // null = all categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        if (!isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView();
        setupCategoryFilter();
        setupClickListeners();
        loadProducts();
    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }

    private void initViews() {
        rvProducts = findViewById(R.id.rvProducts);
        progressBar = findViewById(R.id.progressBar);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        btnSearch = findViewById(R.id.btnSearch);

        // Bottom navigation
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnOrders = findViewById(R.id.btnOrders);
        btnProfile = findViewById(R.id.btnProfile);
    }

    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        // Bottom navigation
        btnHome.setOnClickListener(v -> {
            rvProducts.smoothScrollToPosition(0);
        });

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        btnOrders.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupCategoryFilter() {
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty())
                return;

            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chipAll) {
                selectedCategory = null;
            } else if (checkedId == R.id.chipElectronics) {
                selectedCategory = "electronics";
            } else if (checkedId == R.id.chipFashion) {
                selectedCategory = "fashion";
            } else if (checkedId == R.id.chipHome) {
                selectedCategory = "home";
            } else if (checkedId == R.id.chipSports) {
                selectedCategory = "sports";
            } else if (checkedId == R.id.chipBeauty) {
                selectedCategory = "beauty";
            }

            loadProducts();
        });
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, productList, product -> {
            // Navigate to ProductDetailActivity
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            startActivity(intent);
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setAdapter(productAdapter);
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);

        // Direct Database Call (MVC)
        List<Product> allProducts = AppDatabase.getInstance(this).labeeDao().getAllProducts();

        List<Product> filteredProducts = new ArrayList<>();
        if (selectedCategory == null) {
            filteredProducts.addAll(allProducts);
        } else {
            for (Product p : allProducts) {
                if (p.category != null && p.category.equalsIgnoreCase(selectedCategory)) {
                    filteredProducts.add(p);
                }
            }
        }

        productList.clear();
        productList.addAll(filteredProducts);
        productAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
    }
}
