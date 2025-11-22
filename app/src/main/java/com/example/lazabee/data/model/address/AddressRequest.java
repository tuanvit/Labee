package com.example.lazabee.data.model.address;

import com.google.gson.annotations.SerializedName;

public class AddressRequest {
    @SerializedName("recipientName")  // Backend mong đợi recipientName
    private String fullName;

    private String phoneNumber;
    private String province;
    private String district;
    private String ward;

    @SerializedName("detailAddress")  // Backend mong đợi detailAddress
    private String streetAddress;

    private Boolean isDefault;

    public AddressRequest() {
    }

    public AddressRequest(String fullName, String phoneNumber, String province,
            String district, String ward, String streetAddress, Boolean isDefault) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.streetAddress = streetAddress;
        this.isDefault = isDefault;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
