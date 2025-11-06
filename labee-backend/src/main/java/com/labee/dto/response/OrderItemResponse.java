package com.labee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private String orderItemId;
    private String productId;
    private String productName;
    private String productImage;
    private BigDecimal priceSnapshot;
    private Integer quantity;
    private BigDecimal itemTotal;
}
