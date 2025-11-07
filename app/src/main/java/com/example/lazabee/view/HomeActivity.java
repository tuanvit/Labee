package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;
    private ProductViewModel productViewModel;

    private List<Product> productList = new ArrayList<>();
    private int currentPage = 0;
    private int pageSize = 20;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initViewModel();
        setupRecyclerView();
        loadProducts();
    }

    private void initViews() {
        rvProducts = findViewById(R.id.rvProducts);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initViewModel() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, productList, product -> {
            // Navigate to ProductDetailActivity
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("productId", product.getProductId());
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
        if (isLoading || isLastPage)
            return;

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        productViewModel.getProducts(currentPage, pageSize).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            isLoading = false;

            if (response != null && response.isSuccess() && response.getData() != null) {
                List<Product> newProducts = response.getData().getContent();

                if (newProducts != null && !newProducts.isEmpty()) {
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
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
