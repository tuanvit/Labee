package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lazabee.R;

public class OrderSuccessActivity extends AppCompatActivity {

    private TextView tvOrderId;
    private Button btnViewOrder, btnContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        String orderId = getIntent().getStringExtra("orderId");

        tvOrderId = findViewById(R.id.tvOrderId);
        btnViewOrder = findViewById(R.id.btnViewOrder);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);

        tvOrderId.setText("Order ID: " + orderId);

        btnViewOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, OrderDetailActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
            finish();
        });

        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
