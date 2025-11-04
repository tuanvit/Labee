package com.example.lazabee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lazabee.R;
import com.example.lazabee.viewmodel.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    
    private EditText etEmail;
    private Button btnSendCode;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;
    
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        
        initViews();
        initViewModel();
        setupObservers();
        setupClickListeners();
    }
    
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        btnSendCode = findViewById(R.id.btnSendCode);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
    
    private void setupObservers() {
        authViewModel.getForgotPasswordResult().observe(this, response -> {
            if (response != null) {
                if (response.isSuccess()) {
                    Toast.makeText(this, "Mã xác nhận đã được gửi đến email của bạn!", Toast.LENGTH_SHORT).show();
                    navigateToVerifyCode();
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                btnSendCode.setEnabled(!isLoading);
            }
        });
        
        authViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setupClickListeners() {
        btnSendCode.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            
            if (validateInput(email)) {
                authViewModel.forgotPassword(email);
            }
        });
        
        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
    
    private boolean validateInput(String email) {
        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            return false;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }
        
        return true;
    }
    
    private void navigateToVerifyCode() {
        Intent intent = new Intent(this, VerifyCodeActivity.class);
        intent.putExtra("email", etEmail.getText().toString().trim());
        startActivity(intent);
    }
}