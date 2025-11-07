package com.example.lazabee.data.model.order;

import com.example.lazabee.data.model.Product;

public class OrderItemResponse {
    private String orderItemId;
    private String orderId;
    private Product product;
    private int quantity;
    private double price;
    
    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
