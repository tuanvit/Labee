package com.labee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String addressId;
    private String recipientName;
    private String phoneNumber;
    private String detailAddress;
    private String ward;
    private String district;
    private String province;
    private Boolean isDefault;
}
