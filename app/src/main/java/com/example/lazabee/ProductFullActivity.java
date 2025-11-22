package com.example.lazabee;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.adapter.ProductAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Product;
import com.example.lazabee.view.ProductDetailActivity;
import com.example.lazabee.view.SearchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductFullActivity extends AppCompatActivity {

    private ImageView btnBack, btnSearch;
    private TextView tvTitle, tvProductsCount, tvSortLabel;
    private LinearLayout btnFilter, btnSort, layoutEmptyState;
    private RecyclerView rvProducts;
    private ProgressBar progressBar, progressBarLoadMore;

    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    private int currentPage = 0;
    private int pageSize = 20;
    private boolean isLoading = false;
    private boolean hasMorePages = true;

    private String currentSortBy = "default"; // default, price_asc, price_desc, name, rating

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadProducts();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        tvTitle = findViewById(R.id.tvTitle);
        tvProductsCount = findViewById(R.id.tvProductsCount);
        tvSortLabel = findViewById(R.id.tvSortLabel);
        btnFilter = findViewById(R.id.btnFilter);
        btnSort = findViewById(R.id.btnSort);
        rvProducts = findViewById(R.id.rvProducts);
        progressBar = findViewById(R.id.progressBar);
        progressBarLoadMore = findViewById(R.id.progressBarLoadMore);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, productList, this::openProductDetail);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);

        // Scroll listener for pagination
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && hasMorePages && !isLoading) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 4) {
                        loadMoreProducts();
                    }
                }
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        btnSort.setOnClickListener(v -> showSortDialog());

        btnFilter.setOnClickListener(v -> {
            Toast.makeText(this, "Filter feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadProducts() {
        if (isLoading)
            return;

        isLoading = true;
        currentPage = 0;
        productList.clear();

        showLoading();

        // Direct Database Call (MVC)
        List<Product> products = AppDatabase.getInstance(this).labeeDao().getProducts(pageSize, currentPage * pageSize);

        hideLoading();
        isLoading = false;

        if (products != null) {
            productList.addAll(products);
            productAdapter.updateProducts(productList);

            updateProductsCount();

            hasMorePages = products.size() >= pageSize;

            if (productList.isEmpty()) {
                showEmptyState();
            } else {
                hideEmptyState();
            }
        } else {
            showEmptyState();
        }
    }

    private void loadMoreProducts() {
        if (isLoading || !hasMorePages)
            return;

        isLoading = true;
        currentPage++;

        progressBarLoadMore.setVisibility(View.VISIBLE);

        List<Product> products = AppDatabase.getInstance(this).labeeDao().getProducts(pageSize, currentPage * pageSize);

        progressBarLoadMore.setVisibility(View.GONE);
        isLoading = false;

        if (products != null && !products.isEmpty()) {
            int oldSize = productList.size();
            productList.addAll(products);
            productAdapter.notifyItemRangeInserted(oldSize, products.size());

            updateProductsCount();

            hasMorePages = products.size() >= pageSize;
        } else {
            hasMorePages = false;
        }
    }

    private void showSortDialog() {
        String[] sortOptions = {
                "Mặc định",
                "Giá: Thấp đến cao",
                "Giá: Cao đến thấp",
                "Tên: A-Z",
                "Đánh giá cao nhất"
        };

        int currentSelection = getCurrentSortIndex();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sắp xếp theo");
        builder.setSingleChoiceItems(sortOptions, currentSelection, (dialog, which) -> {
            switch (which) {
                case 0:
                    currentSortBy = "default";
                    tvSortLabel.setText("Sắp xếp");
                    break;
                case 1:
                    currentSortBy = "price_asc";
                    tvSortLabel.setText("Giá tăng");
                    break;
                case 2:
                    currentSortBy = "price_desc";
                    tvSortLabel.setText("Giá giảm");
                    break;
                case 3:
                    currentSortBy = "name";
                    tvSortLabel.setText("Tên A-Z");
                    break;
                case 4:
                    currentSortBy = "rating";
                    tvSortLabel.setText("Đánh giá");
                    break;
            }
            sortProductsLocally();
            dialog.dismiss();
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private int getCurrentSortIndex() {
        switch (currentSortBy) {
            case "price_asc":
                return 1;
            case "price_desc":
                return 2;
            case "name":
                return 3;
            case "rating":
                return 4;
            default:
                return 0;
        }
    }

    private void sortProductsLocally() {
        switch (currentSortBy) {
            case "price_asc":
                Collections.sort(productList, (p1, p2) -> Double.compare(p1.price, p2.price));
                break;
            case "price_desc":
                Collections.sort(productList, (p1, p2) -> Double.compare(p2.price, p1.price));
                break;
            case "name":
                Collections.sort(productList, (p1, p2) -> p1.name.compareToIgnoreCase(p2.name));
                break;
            case "rating":
                // Rating not implemented in Product model yet, so ignore or sort by ID
                break;
            default:
                // Reload to reset order (or just sort by ID)
                Collections.sort(productList, (p1, p2) -> Integer.compare(p1.id, p2.id));
                break;
        }
        productAdapter.updateProducts(productList);
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.id);
        intent.putExtra("product_name", product.name);
        startActivity(intent);
    }

    private void updateProductsCount() {
        tvProductsCount.setText(productList.size() + " sản phẩm");
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvProducts.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        rvProducts.setVisibility(View.VISIBLE);
    }

    private void showEmptyState() {
        layoutEmptyState.setVisibility(View.VISIBLE);
        rvProducts.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        layoutEmptyState.setVisibility(View.GONE);
    }
}
