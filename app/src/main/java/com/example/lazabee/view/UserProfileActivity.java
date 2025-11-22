package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lazabee.R;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.User;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvFullName, tvUsername;
    private ImageView btnBack, ivAvatar, btnSettings;
    private Button btnLogout;
    private LinearLayout btnOrderHistory, btnAddressManagement, btnVoucherWallet, btnHelpCenter;
    private LinearLayout btnStatusWaitConfirm, btnStatusWaitPickup, btnStatusDelivering, btnStatusRating;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sharedPreferences = getSharedPreferences("LabeePrefs", MODE_PRIVATE);

        initViews();
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews() {
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        btnBack = findViewById(R.id.btnBack);
        ivAvatar = findViewById(R.id.ivAvatar);
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);

        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnAddressManagement = findViewById(R.id.btnAddressManagement);
        btnVoucherWallet = findViewById(R.id.btnVoucherWallet);
        btnHelpCenter = findViewById(R.id.btnHelpCenter);

        btnStatusWaitConfirm = findViewById(R.id.btnStatusWaitConfirm);
        btnStatusWaitPickup = findViewById(R.id.btnStatusWaitPickup);
        btnStatusDelivering = findViewById(R.id.btnStatusDelivering);
        btnStatusRating = findViewById(R.id.btnStatusRating);

        // Add progressBar if not in layout
        progressBar = new ProgressBar(this);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnLogout.setOnClickListener(v -> {
            // Clear user session
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        btnAddressManagement.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressManagementActivity.class);
            startActivity(intent);
        });

        btnVoucherWallet.setOnClickListener(v -> {
            Intent intent = new Intent(this, VoucherListActivity.class);
            startActivity(intent);
        });

        btnHelpCenter.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Cài đặt tài khoản", Toast.LENGTH_SHORT).show();
        });

        // Order Status Clicks - For now, all go to OrderHistory
        View.OnClickListener orderStatusListener = v -> {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
        };

        btnStatusWaitConfirm.setOnClickListener(orderStatusListener);
        btnStatusWaitPickup.setOnClickListener(orderStatusListener);
        btnStatusDelivering.setOnClickListener(orderStatusListener);
        btnStatusRating.setOnClickListener(orderStatusListener);
    }

    private void loadUserProfile() {
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        User user = AppDatabase.getInstance(this).labeeDao().getUserById(userId);

        if (user != null) {
            // Display user info
            tvFullName.setText(user.fullName != null ? user.fullName : "Người dùng");
            tvUsername.setText("@" + (user.email != null ? user.email.split("@")[0] : "username"));
        } else {
            Toast.makeText(this, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }
}
