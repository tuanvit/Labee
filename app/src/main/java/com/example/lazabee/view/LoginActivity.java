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
import com.example.lazabee.model.Product;
import com.example.lazabee.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Seed data if needed
        seedData();

        initViews();
        setupClickListeners();

        // Check if user is already logged in
        if (isLoggedIn()) {
            navigateToMain();
        }
    }

    private void seedData() {
        AppDatabase db = AppDatabase.getInstance(this);
        if (db.labeeDao().getAllProducts().isEmpty()) {
            // Add sample products
            for (int i = 1; i <= 10; i++) {
                Product p = new Product();
                p.name = "Sản phẩm mẫu " + i;
                p.price = 100000 * i;
                p.description = "Mô tả cho sản phẩm mẫu " + i;
                p.imageResName = "ic_launcher_foreground"; // Placeholder
                p.category = "Áo thun";
                db.labeeDao().insertProduct(p);
            }

            // Add sample user
            if (db.labeeDao().checkUserExist("admin@gmail.com") == null) {
                User u = new User();
                u.email = "admin@gmail.com";
                u.password = "123456";
                u.fullName = "Admin User";
                db.labeeDao().register(u);
            }
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInput(email, password)) {
                performLogin(email, password);
            }
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            // startActivity(new Intent(this, ForgotPasswordActivity.class));
            Toast.makeText(this, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void performLogin(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // Direct Database Call (MVC)
        User user = AppDatabase.getInstance(this).labeeDao().login(email, password);

        progressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);

        if (user != null) {
            saveLoginState(user);
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            navigateToMain();
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoginState(User user) {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putInt("userId", user.id);
        editor.putString("userEmail", user.email);
        editor.putString("userName", user.fullName);
        editor.apply();
    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("LabeePrefs", MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Mật khẩu không được để trống");
            return false;
        }
        return true;
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}