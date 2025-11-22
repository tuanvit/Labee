package com.example.lazabee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items")
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int orderId;
    public int productId;
    public int quantity;
    public int price;
}
