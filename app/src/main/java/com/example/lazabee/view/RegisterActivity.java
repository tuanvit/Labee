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

import com.example.lazabee.MainActivity;
import com.example.lazabee.R;
import com.example.lazabee.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etFullName, etPhone;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initViewModel();
        setupObservers();
        setupClickListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void setupObservers() {
        // No need for separate observers now, we observe in the register click listener
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (validateInput(username, email, password, confirmPassword, fullName, phone)) {
                performRegister(username, email, password, fullName, phone);
            }
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void performRegister(String username, String email, String password, String fullName, String phone) {
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        authViewModel.register(username, email, password, fullName, phone).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);

            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            } else {
                String errorMsg = (response != null && response.getMessage() != null)
                        ? response.getMessage()
                        : "Đăng ký thất bại";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String username, String email, String password,
            String confirmPassword, String fullName, String phone) {

        if (username.isEmpty()) {
            etUsername.setError("Tên đăng nhập không được để trống");
            return false;
        }

        if (username.length() < 3) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 3 ký tự");
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Mật khẩu không được để trống");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return false;
        }

        if (fullName.isEmpty()) {
            etFullName.setError("Họ tên không được để trống");
            return false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Số điện thoại không được để trống");
            return false;
        }

        if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            etPhone.setError("Số điện thoại không hợp lệ");
            return false;
        }

        return true;
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}