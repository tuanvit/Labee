package com.example.lazabee.model;

import java.io.Serializable;

public class Voucher implements Serializable {
    public String id;
    public String code;
    public String title;
    public String description;
    public double discountAmount;
    public int quantity;
    public String expiryDate;
    public boolean isCollected;

    public Voucher(String id, String code, String title, String description, double discountAmount, int quantity, String expiryDate) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.discountAmount = discountAmount;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.isCollected = false;
    }
}
