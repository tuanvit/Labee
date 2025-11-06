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
    
    @NotBlank(message = "Address line is required")
    private String addressLine;
    
    @NotBlank(message = "Ward is required")
    private String ward;
    
    @NotBlank(message = "District is required")
    private String district;
    
    @NotBlank(message = "City is required")
    private String city;
    
    private Boolean isDefault = false;
}
