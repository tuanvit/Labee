package com.example.lazabee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int productId;
    public int quantity;
}
