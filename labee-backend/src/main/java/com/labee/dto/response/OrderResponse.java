package com.labee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String orderNumber;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String recipientName;
    private String recipientPhone;
    private LocalDateTime orderedAt;
    private List<OrderItemResponse> items;
}
