package com.example.lazabee.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String email;
    public String password;
    public String fullName;
    public String phone;
    public String address;
}
