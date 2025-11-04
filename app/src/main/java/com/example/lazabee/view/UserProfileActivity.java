package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lazabee.R;
import com.example.lazabee.data.model.User;
import com.example.lazabee.viewmodel.AuthViewModel;

public class UserProfileActivity extends AppCompatActivity {
    
    private ImageView ivAvatar;
    private TextView tvFullName, tvUsername, tvEmail, tvPhone;
    private Button btnLogout;
    
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        
        initViews();
        initViewModel();
        setupObservers();
        setupClickListeners();
        loadUserInfo();
    }
    
    private void initViews() {
        ivAvatar = findViewById(R.id.ivAvatar);
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnLogout = findViewById(R.id.btnLogout);
    }
    
    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
    
    private void setupObservers() {
        authViewModel.getLogoutResult().observe(this, logoutSuccess -> {
            if (logoutSuccess != null && logoutSuccess) {
                Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        });
        
        authViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
        });
        
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            finish();
        });
    }
    
    private void loadUserInfo() {
        User currentUser = authViewModel.getCurrentUser();
        if (currentUser != null) {
            tvFullName.setText(currentUser.getFullName());
            tvUsername.setText("@" + currentUser.getUsername());
            tvEmail.setText(currentUser.getEmail());
            tvPhone.setText(currentUser.getPhone());
        }
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}