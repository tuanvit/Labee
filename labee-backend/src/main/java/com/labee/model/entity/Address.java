package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private String addressId;

    @Column(nullable = false, length = 100)
    private String recipientName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "address_line", nullable = false, length = 300)
    private String detailAddress; // Số nhà, tên đường

    @Column(length = 100)
    private String ward; // Phường/Xã

    @Column(length = 100)
    private String district; // Quận/Huyện

    @Column(name = "city", nullable = false, length = 100)
    private String province; // Thành phố/Tỉnh

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}