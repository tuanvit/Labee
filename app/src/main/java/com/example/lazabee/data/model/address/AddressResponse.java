package com.example.lazabee.data.model.address;

import com.google.gson.annotations.SerializedName;

public class AddressResponse {
    private String addressId;

    @SerializedName("recipientName")  // Backend trả về recipientName
    private String fullName;

    private String phoneNumber;
    private String province;
    private String district;
    private String ward;

    @SerializedName("detailAddress")  // Backend trả về detailAddress
    private String streetAddress;

    private Boolean isDefault;

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    
    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }
    
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    
    public Boolean isDefault() { return isDefault; }
    public void setDefault(Boolean aDefault) { isDefault = aDefault; }

    public String getFullAddress() {
        return streetAddress + ", " + ward + ", " + district + ", " + province;
    }
}
