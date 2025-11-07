package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id")
    private String orderItemId;

    @Column(nullable = false)
    private Integer quantity;

    // Snapshot giá tại thời điểm đặt hàng
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAtOrder;

    // Snapshot thông tin sản phẩm
    @Column(nullable = false, length = 200)
    private String productName;

    @Column(length = 500)
    private String productImageUrl;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}