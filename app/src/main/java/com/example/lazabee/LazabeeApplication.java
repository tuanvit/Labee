package com.example.lazabee;

import android.app.Application;
import com.example.lazabee.database.AppDatabase;

public class LazabeeApplication extends Application {
    private static LazabeeApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize Database
        AppDatabase.getInstance(this);
    }

    public static LazabeeApplication getInstance() {
        return instance;
    }
}