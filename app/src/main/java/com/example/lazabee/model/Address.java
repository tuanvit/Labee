package com.example.lazabee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "addresses")
public class Address {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String name;
    public String phone;
    public String address;
    public boolean isDefault;
}
