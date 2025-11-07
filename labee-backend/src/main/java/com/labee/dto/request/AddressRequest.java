package com.labee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    @NotBlank(message = "Recipient name is required")
    private String recipientName;
    
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    
    @NotBlank(message = "Detail address is required")
    private String detailAddress;
    
    @NotBlank(message = "Ward is required")
    private String ward;
    
    @NotBlank(message = "District is required")
    private String district;
    
    @NotBlank(message = "Province is required")
    private String province;
    
    private Boolean isDefault = false;
}
