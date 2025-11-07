package com.example.lazabee.data.model.order;

import com.example.lazabee.data.model.address.AddressResponse;
import java.util.List;

public class OrderResponse {
    private String orderId;
    private String userId;
    private AddressResponse shippingAddress;
    private List<OrderItemResponse> items;
    private double totalAmount;
    private String status;
    private String paymentMethod;
    private String note;
    private String createdAt;
    private String updatedAt;
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public AddressResponse getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressResponse shippingAddress) { this.shippingAddress = shippingAddress; }
    
    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
