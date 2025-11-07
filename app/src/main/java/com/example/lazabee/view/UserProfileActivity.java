package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.lazabee.R;
import com.example.lazabee.data.model.auth.AuthResponse;
import com.example.lazabee.viewmodel.AuthViewModel;

public class UserProfileActivity extends AppCompatActivity {
    
    private AuthViewModel authViewModel;
    private TextView tvFullName, tvUsername, tvEmail, tvPhone;
    private ImageView btnBack, ivAvatar;
    private Button btnLogout;
    private LinearLayout btnOrderHistory, btnAddressManagement;
    private ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        
        initViews();
        initViewModel();
        setupClickListeners();
        loadUserProfile();
    }
    
    private void initViews() {
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnBack = findViewById(R.id.btnBack);
        ivAvatar = findViewById(R.id.ivAvatar);
        btnLogout = findViewById(R.id.btnLogout);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnAddressManagement = findViewById(R.id.btnAddressManagement);
        
        // Add progressBar if not in layout
        progressBar = new ProgressBar(this);
    }
    
    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
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
    }
    
    private void loadUserProfile() {
        authViewModel.getProfile().observe(this, response -> {
            if (response != null && response.isSuccess() && response.getData() != null) {
                AuthResponse user = response.getData();
                
                // Display user info
                tvFullName.setText(user.getFullName() != null ? user.getFullName() : "Người dùng");
                tvUsername.setText("@" + (user.getUsername() != null ? user.getUsername() : "username"));
                tvEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
                tvPhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "Chưa cập nhật");
                
                // TODO: Load avatar with Glide if user.getAvatar() is available
                // Glide.with(this).load(user.getAvatar()).placeholder(R.drawable.ic_user_avatar).into(ivAvatar);
                
            } else {
                String errorMsg = (response != null && response.getMessage() != null) 
                    ? response.getMessage() 
                    : "Không thể tải thông tin người dùng";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
