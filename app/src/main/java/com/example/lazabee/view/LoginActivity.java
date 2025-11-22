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

        // Check if we need to re-seed (e.g., if only old sample data exists)
        boolean needReseed = false;
        if (db.labeeDao().getAllProducts().isEmpty()) {
            needReseed = true;
        } else {
            // Check if the first product is the old "Sản phẩm mẫu" OR has placeholder image
            Product p = db.labeeDao().getAllProducts().get(0);
            if (p.name.startsWith("Sản phẩm mẫu") ||
                    (p.name.equals("Laptop Gaming Dell") && p.imageResName.equals("ic_launcher_foreground"))) {
                needReseed = true;
                db.labeeDao().deleteAllProducts();
            }
        }

        if (needReseed) {
            // Electronics
            db.labeeDao().insertProduct(createProduct("Laptop Gaming Dell", 25000000,
                    "Laptop cấu hình khủng cho game thủ", "img_laptop", "electronics"));
            db.labeeDao().insertProduct(createProduct("iPhone 15 Pro Max", 34000000,
                    "Điện thoại thông minh cao cấp nhất từ Apple", "img_iphone", "electronics"));
            db.labeeDao().insertProduct(createProduct("Tai nghe Sony WH-1000XM5", 8000000,
                    "Tai nghe chống ồn chủ động hàng đầu", "img_headphone", "electronics"));
            db.labeeDao().insertProduct(createProduct("Samsung Galaxy S24 Ultra", 30000000,
                    "Siêu phẩm Android với bút S-Pen", "img_samsung", "electronics"));

            // Fashion
            db.labeeDao().insertProduct(createProduct("Áo thun nam Basic", 150000, "Áo thun cotton 100% thoáng mát",
                    "img_tshirt", "fashion"));
            db.labeeDao().insertProduct(
                    createProduct("Quần Jean Slimfit", 450000, "Quần jean dáng ôm thời trang", "img_jeans", "fashion"));
            db.labeeDao().insertProduct(createProduct("Giày Sneaker trắng", 800000,
                    "Giày thể thao năng động, dễ phối đồ", "img_sneaker", "fashion"));
            db.labeeDao().insertProduct(
                    createProduct("Áo khoác Bomber", 600000, "Áo khoác phong cách đường phố", "img_jacket", "fashion"));

            // Home
            db.labeeDao().insertProduct(createProduct("Đèn bàn học LED", 250000, "Đèn chống cận thị, 3 chế độ sáng",
                    "img_lamp", "home"));
            db.labeeDao().insertProduct(createProduct("Bộ chăn ga gối Cotton", 1200000,
                    "Chất liệu mềm mại, họa tiết hiện đại", "img_bedding", "home"));
            db.labeeDao().insertProduct(createProduct("Máy xay sinh tố", 500000, "Công suất lớn, xay nhuyễn mọi thứ",
                    "img_blender", "home"));

            // Sports
            db.labeeDao().insertProduct(createProduct("Giày chạy bộ Nike", 2000000, "Giày chạy bộ chuyên nghiệp, êm ái",
                    "img_running_shoes", "sports"));
            db.labeeDao().insertProduct(createProduct("Vợt cầu lông Yonex", 1500000, "Vợt nhẹ, trợ lực tốt",
                    "img_racket", "sports"));
            db.labeeDao().insertProduct(createProduct("Bóng đá size 5", 300000, "Bóng da tiêu chuẩn thi đấu",
                    "img_soccer_ball", "sports"));

            // Beauty
            db.labeeDao().insertProduct(createProduct("Son môi MAC Ruby Woo", 600000, "Màu đỏ huyền thoại, lâu trôi",
                    "img_lipstick", "beauty"));
            db.labeeDao().insertProduct(createProduct("Kem dưỡng ẩm Laneige", 800000, "Cấp nước cho da căng mọng",
                    "img_cream", "beauty"));
            db.labeeDao().insertProduct(createProduct("Nước hoa Chanel No.5", 3500000,
                    "Hương thơm sang trọng, quyến rũ", "img_perfume", "beauty"));
        }

        // Add sample user if not exists
        if (db.labeeDao().checkUserExist("admin@gmail.com") == null) {
            User u = new User();
            u.email = "admin@gmail.com";
            u.password = "123456";
            u.fullName = "Admin User";
            db.labeeDao().register(u);
        }
    }

    private Product createProduct(String name, int price, String description, String imageRes, String category) {
        Product p = new Product();
        p.name = name;
        p.price = price;
        p.description = description;
        p.imageResName = imageRes;
        p.category = category;
        return p;
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