package com.example.lazabee.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lazabee.R;
import com.example.lazabee.database.AppDatabase;
import com.example.lazabee.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etFullName, etPhone;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
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

        AppDatabase db = AppDatabase.getInstance(this);

        // Check if user exists
        if (db.labeeDao().checkUserExist(email) != null) {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
            Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new user
        User newUser = new User();
        newUser.email = email;
        newUser.password = password;
        newUser.fullName = fullName;
        newUser.phone = phone;
        newUser.role = "customer";
        // newUser.username = username; // If we add username to User entity, but for
        // now email is key

        db.labeeDao().register(newUser);

        progressBar.setVisibility(View.GONE);
        btnRegister.setEnabled(true);

        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

        // Auto login
        User user = db.labeeDao().login(email, password);
        if (user != null) {
            saveLoginState(user);
            navigateToMain();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void saveLoginState(User user) {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putInt("userId", user.id);
        editor.putString("userEmail", user.email);
        editor.putString("userName", user.fullName);
        editor.putString("userRole", user.role);
        editor.apply();
    }

    private boolean validateInput(String username, String email, String password,
            String confirmPassword, String fullName, String phone) {

        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Mật khẩu không được để trống");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return false;
        }
        return true;
    }

    private void navigateToMain() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        String role = prefs.getString("userRole", "customer");

        Intent intent;
        if ("admin".equals(role)) {
            intent = new Intent(this, AdminHomeActivity.class);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}