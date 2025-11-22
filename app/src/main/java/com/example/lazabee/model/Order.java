package com.example.lazabee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int totalPrice;
    public String date;
    public String status; // "Pending", "Completed"
    public String address;
    public String phoneNumber;
    public String paymentMethod;
    public String note;
}
