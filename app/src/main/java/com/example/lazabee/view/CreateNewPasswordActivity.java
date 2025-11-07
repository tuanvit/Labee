package com.example.lazabee.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lazabee.R;

public class CreateNewPasswordActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewpass);
        
        Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show();
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
