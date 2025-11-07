package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.ProductAdapter;
import com.example.lazabee.data.model.Product;
import com.example.lazabee.viewmodel.ProductViewModel;
import com.google.android.material.chip.Chip;
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
    private ProductViewModel productViewModel;

    private List<Product> productList = new ArrayList<>();
    private int currentPage = 0;
    private int pageSize = 20;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String selectedCategory = null; // null = all categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("HomeActivity", "onCreate started");

        try {
            setContentView(R.layout.activity_home);
            android.util.Log.d("HomeActivity", "setContentView done");

            initViews();
            android.util.Log.d("HomeActivity", "initViews done");

            initViewModel();
            android.util.Log.d("HomeActivity", "initViewModel done");

            setupRecyclerView();
            android.util.Log.d("HomeActivity", "setupRecyclerView done");

            setupCategoryFilter();
            android.util.Log.d("HomeActivity", "setupCategoryFilter done");

            setupClickListeners();
            android.util.Log.d("HomeActivity", "setupClickListeners done");

            loadProducts();
            android.util.Log.d("HomeActivity", "loadProducts called");
        } catch (Exception e) {
            android.util.Log.e("HomeActivity", "Error in onCreate", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    private void initViewModel() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
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

            // Reset and reload with new category
            currentPage = 0;
            productList.clear();
            isLastPage = false;
            loadProducts();
        });
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, productList, product -> {
            // Navigate to ProductDetailActivity
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getProductId());
            startActivity(intent);
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setAdapter(productAdapter);

        // Pagination
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isLoading && !isLastPage) {
                    if (layoutManager.findLastVisibleItemPosition() == productList.size() - 1) {
                        loadProducts();
                    }
                }
            }
        });
    }

    private void loadProducts() {
        android.util.Log.d("HomeActivity", "loadProducts: isLoading=" + isLoading + ", isLastPage=" + isLastPage);

        if (isLoading || isLastPage)
            return;

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        android.util.Log.d("HomeActivity",
                "Calling productViewModel.getProducts(page=" + currentPage + ", size=" + pageSize + ")");

        productViewModel.getProducts(currentPage, pageSize).observe(this, response -> {
            android.util.Log.d("HomeActivity",
                    "Response received: " + (response != null ? response.isSuccess() : "null"));

            progressBar.setVisibility(View.GONE);
            isLoading = false;

            if (response != null && response.isSuccess() && response.getData() != null) {
                List<Product> newProducts = response.getData().getContent();
                android.util.Log.d("HomeActivity",
                        "Products loaded: " + (newProducts != null ? newProducts.size() : "null"));

                if (newProducts != null && !newProducts.isEmpty()) {
                    // Filter by category if selected
                    if (selectedCategory != null) {
                        List<Product> filtered = new ArrayList<>();
                        for (Product p : newProducts) {
                            if (p.getCategoryId() != null && p.getCategoryId().equalsIgnoreCase(selectedCategory)) {
                                filtered.add(p);
                            }
                        }
                        newProducts = filtered;
                    }

                    productList.addAll(newProducts);
                    productAdapter.notifyDataSetChanged();
                    currentPage++;

                    // Check if last page
                    isLastPage = response.getData().isLast();
                } else {
                    isLastPage = true;
                }
            } else {
                String errorMessage = response != null ? response.getMessage() : "Failed to load products";
                android.util.Log.e("HomeActivity", "Error loading products: " + errorMessage);
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
