package com.example.lazabee.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lazabee.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        
        Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show();
        findViewById(R.id.tvBackToLogin).setOnClickListener(v -> finish());
    }
}
