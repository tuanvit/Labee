package com.example.lazabee.data.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("cartItemId")
    private String id;  // Backend trả về cartItemId dạng String

    private String productId;  // Backend trả về String
    private String productName;
    private String productImageUrl;
    private Double price;
    private Integer quantity;
    private Double subtotal;  // Backend trả về thêm field này

    public CartItem() {
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
