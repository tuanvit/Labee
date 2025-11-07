package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lazabee.R;
import com.example.lazabee.adapter.OrderAdapter;
import com.example.lazabee.data.model.order.OrderResponse;
import com.example.lazabee.viewmodel.OrderViewModel;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView rvOrders;
    private ProgressBar progressBar;
    private OrderViewModel orderViewModel;
    private OrderAdapter orderAdapter;

    private List<OrderResponse> orderList = new ArrayList<>();
    private int currentPage = 0;
    private int pageSize = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        initViews();
        initViewModel();
        setupRecyclerView();
        loadOrders();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        rvOrders = findViewById(R.id.rvOrders);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViewModel() {
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(this, orderList, order -> {
            Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(orderAdapter);

        // Pagination
        rvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isLoading && !isLastPage) {
                    if (layoutManager.findLastVisibleItemPosition() == orderList.size() - 1) {
                        loadOrders();
                    }
                }
            }
        });
    }

    private void loadOrders() {
        if (isLoading || isLastPage)
            return;

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        orderViewModel.getOrders(currentPage, pageSize).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            isLoading = false;

            if (response != null && response.isSuccess() && response.getData() != null) {
                List<OrderResponse> newOrders = response.getData().getContent();

                if (newOrders != null && !newOrders.isEmpty()) {
                    orderList.addAll(newOrders);
                    orderAdapter.notifyDataSetChanged();
                    currentPage++;

                    isLastPage = response.getData().isLast();
                } else {
                    isLastPage = true;
                }
            } else {
                String errorMessage = response != null ? response.getMessage() : "Failed to load orders";
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
