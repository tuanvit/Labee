package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.ProductAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Product;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SearchHistory";
    private static final String KEY_HISTORY = "history";
    private static final int MAX_HISTORY = 10;

    private SearchView searchView;
    private ImageView btnBack;
    private ChipGroup chipGroupHistory;
    private TextView tvClearHistory, tvResultsCount;
    private RecyclerView rvSearchResults;
    private ProgressBar progressBar;
    private LinearLayout layoutSearchHistory, layoutEmptyState;
    private View divider;

    private ProductAdapter productAdapter;
    private List<String> searchHistory;
    private SharedPreferences prefs;
    private Handler searchHandler;
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        loadSearchHistory();
        setupRecyclerView();
        setupSearchView();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        searchView = findViewById(R.id.searchView);
        chipGroupHistory = findViewById(R.id.chipGroupHistory);
        tvClearHistory = findViewById(R.id.tvClearHistory);
        tvResultsCount = findViewById(R.id.tvResultsCount);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        progressBar = findViewById(R.id.progressBar);
        layoutSearchHistory = findViewById(R.id.layoutSearchHistory);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        divider = findViewById(R.id.divider);

        searchHandler = new Handler();
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, new ArrayList<>(), product -> {
            Intent intent = new Intent(SearchActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            startActivity(intent);
        });

        rvSearchResults.setLayoutManager(new GridLayoutManager(this, 2));
        rvSearchResults.setAdapter(productAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.trim().isEmpty()) {
                    performSearch(query.trim());
                    saveSearchQuery(query.trim());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Debounce search - wait 500ms after user stops typing
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                if (newText != null && newText.trim().length() >= 2) {
                    searchRunnable = () -> performSearch(newText.trim());
                    searchHandler.postDelayed(searchRunnable, 500);
                } else if (newText.trim().isEmpty()) {
                    showSearchHistory();
                }
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && searchView.getQuery().toString().trim().isEmpty()) {
                showSearchHistory();
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        tvClearHistory.setOnClickListener(v -> {
            clearSearchHistory();
        });
    }

    private void loadSearchHistory() {
        Set<String> historySet = prefs.getStringSet(KEY_HISTORY, new HashSet<>());
        searchHistory = new ArrayList<>(historySet);

        chipGroupHistory.removeAllViews();

        for (String query : searchHistory) {
            Chip chip = new Chip(this);
            chip.setText(query);
            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_search, null));
            chip.setCloseIconVisible(false);
            chip.setClickable(true);
            chip.setCheckable(false);

            chip.setOnClickListener(v -> {
                searchView.setQuery(query, true);
            });

            chipGroupHistory.addView(chip);
        }

        if (searchHistory.isEmpty()) {
            layoutSearchHistory.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
    }

    private void saveSearchQuery(String query) {
        if (searchHistory.contains(query)) {
            searchHistory.remove(query);
        }

        searchHistory.add(0, query);

        if (searchHistory.size() > MAX_HISTORY) {
            searchHistory = searchHistory.subList(0, MAX_HISTORY);
        }

        Set<String> historySet = new HashSet<>(searchHistory);
        prefs.edit().putStringSet(KEY_HISTORY, historySet).apply();

        loadSearchHistory();
    }

    private void clearSearchHistory() {
        searchHistory.clear();
        prefs.edit().clear().apply();
        chipGroupHistory.removeAllViews();
        layoutSearchHistory.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
        Toast.makeText(this, "Đã xóa lịch sử tìm kiếm", Toast.LENGTH_SHORT).show();
    }

    private void performSearch(String keyword) {
        hideSearchHistory();
        showLoading();

        // Direct Database Call (MVC)
        List<Product> products = AppDatabase.getInstance(this).labeeDao().searchProducts(keyword);
        
        hideLoading();

        if (products != null && !products.isEmpty()) {
            hideEmptyState();
            productAdapter.updateProducts(products);
            tvResultsCount.setVisibility(View.VISIBLE);
            tvResultsCount.setText(products.size() + " kết quả");
            rvSearchResults.setVisibility(View.VISIBLE);
        } else {
            showEmptyState();
        }
    }

    private void showSearchHistory() {
        if (!searchHistory.isEmpty()) {
            layoutSearchHistory.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }
        rvSearchResults.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
    }

    private void hideSearchHistory() {
        layoutSearchHistory.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        layoutEmptyState.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);
        tvResultsCount.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        layoutEmptyState.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}
