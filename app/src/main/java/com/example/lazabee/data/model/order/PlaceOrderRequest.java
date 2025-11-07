package com.example.lazabee.data.model.order;

public class PlaceOrderRequest {
    private String addressId;
    private String paymentMethod;
    private String note;
    
    public PlaceOrderRequest(String addressId, String paymentMethod, String note) {
        this.addressId = addressId;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }
    
    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
