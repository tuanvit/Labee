package com.example.lazabee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int price;
    public String description;
    public String imageResName; // e.g., "ic_shirt"
    public String category;
    public int stock; // Inventory count
}
