package com.example.lazabee.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lazabee.R;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.Product;
import com.google.android.material.textfield.TextInputEditText;

public class AdminProductEditActivity extends AppCompatActivity {

    private TextInputEditText etName, etPrice, etStock, etDescription, etImageRes, etCategory;
    private Button btnSave, btnDelete;
    private TextView tvTitle;
    private int productId = -1;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_edit);

        initViews();

        productId = getIntent().getIntExtra("productId", -1);
        if (productId != -1) {
            loadProduct(productId);
            tvTitle.setText("Chỉnh sửa sản phẩm");
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnSave.setOnClickListener(v -> saveProduct());
        btnDelete.setOnClickListener(v -> deleteProduct());
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        etDescription = findViewById(R.id.etDescription);
        etImageRes = findViewById(R.id.etImageRes);
        etCategory = findViewById(R.id.etCategory);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void loadProduct(int id) {
        currentProduct = AppDatabase.getInstance(this).labeeDao().getProductById(id);
        if (currentProduct != null) {
            etName.setText(currentProduct.name);
            etPrice.setText(String.valueOf(currentProduct.price));
            etStock.setText(String.valueOf(currentProduct.stock));
            etDescription.setText(currentProduct.description);
            etImageRes.setText(currentProduct.imageResName);
            etCategory.setText(currentProduct.category);
        }
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String imageRes = etImageRes.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và giá", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceStr);
        int stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);

        if (currentProduct == null) {
            currentProduct = new Product();
        }

        currentProduct.name = name;
        currentProduct.price = price;
        currentProduct.stock = stock;
        currentProduct.description = description;
        currentProduct.imageResName = imageRes;
        currentProduct.category = category;

        if (productId == -1) {
            AppDatabase.getInstance(this).labeeDao().insertProduct(currentProduct);
            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
        } else {
            AppDatabase.getInstance(this).labeeDao().updateProduct(currentProduct);
            Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void deleteProduct() {
        if (currentProduct != null) {
            AppDatabase.getInstance(this).labeeDao().deleteProduct(currentProduct);
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}