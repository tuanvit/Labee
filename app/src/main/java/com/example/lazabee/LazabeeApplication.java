package com.example.lazabee;

import android.app.Application;
import com.example.lazabee.data.local.AppDatabase;

public class LazabeeApplication extends Application {
    
    private AppDatabase database;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Khởi tạo database
        database = AppDatabase.getInstance(this);
    }
    
    public AppDatabase getDatabase() {
        return database;
    }
}