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

public class VerifyCodeActivity extends AppCompatActivity {
    
    private EditText etVerificationCode;
    private Button btnVerify;
    private TextView tvResendCode, tvBackToLogin;
    private ProgressBar progressBar;
    
    private AuthViewModel authViewModel;
    private String email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifycode);
        
        email = getIntent().getStringExtra("email");
        
        initViews();
        initViewModel();
        setupObservers();
        setupClickListeners();
    }
    
    private void initViews() {
        etVerificationCode = findViewById(R.id.etVerificationCode);
        btnVerify = findViewById(R.id.btnVerify);
        tvResendCode = findViewById(R.id.tvResendCode);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
    
    private void setupObservers() {
        authViewModel.getVerifyCodeResult().observe(this, response -> {
            if (response != null) {
                if (response.isSuccess()) {
                    Toast.makeText(this, "Xác nhận thành công!", Toast.LENGTH_SHORT).show();
                    navigateToCreateNewPassword();
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        authViewModel.getForgotPasswordResult().observe(this, response -> {
            if (response != null) {
                if (response.isSuccess()) {
                    Toast.makeText(this, "Mã xác nhận mới đã được gửi!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                btnVerify.setEnabled(!isLoading);
                tvResendCode.setEnabled(!isLoading);
            }
        });
        
        authViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setupClickListeners() {
        btnVerify.setOnClickListener(v -> {
            String code = etVerificationCode.getText().toString().trim();
            
            if (validateInput(code)) {
                authViewModel.verifyCode(code);
            }
        });
        
        tvResendCode.setOnClickListener(v -> {
            if (email != null) {
                authViewModel.forgotPassword(email);
            }
        });
        
        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
    
    private boolean validateInput(String code) {
        if (code.isEmpty()) {
            etVerificationCode.setError("Mã xác nhận không được để trống");
            return false;
        }
        
        if (code.length() != 6) {
            etVerificationCode.setError("Mã xác nhận phải có 6 ký tự");
            return false;
        }
        
        return true;
    }
    
    private void navigateToCreateNewPassword() {
        Intent intent = new Intent(this, CreateNewPasswordActivity.class);
        startActivity(intent);
    }
}