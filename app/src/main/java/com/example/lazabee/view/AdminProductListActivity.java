package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazabee.R;
import com.example.lazabee.adapter.ProductAdapter;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AdminProductListActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private FloatingActionButton fabAddProduct;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list.xml);

        rvProducts = findViewById(R.id.rvProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        fabAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminProductEditActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = AppDatabase.getInstance(this).labeeDao().getAllProducts();
        adapter = new ProductAdapter(this, products, product -> {
            Intent intent = new Intent(this, AdminProductEditActivity.class);
            intent.putExtra("productId", product.id);
            startActivity(intent);
        });
        rvProducts.setAdapter(adapter);
    }
}