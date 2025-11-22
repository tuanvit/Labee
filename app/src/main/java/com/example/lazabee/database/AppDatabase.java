package com.example.lazabee.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.lazabee.model.Address;
import com.example.lazabee.model.CartItem;
import com.example.lazabee.model.Order;
import com.example.lazabee.model.OrderItem;
import com.example.lazabee.model.Product;
import com.example.lazabee.model.User;

@Database(entities = { User.class, Product.class, Order.class, CartItem.class, OrderItem.class,
        Address.class }, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LabeeDao labeeDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "labee_db")
                    .allowMainThreadQueries() // IMPORTANT: Allow main thread queries for simplicity
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
