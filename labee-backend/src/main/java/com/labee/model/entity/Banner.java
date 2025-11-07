package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "banner_id")
    private String bannerId;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String targetUrl; // URL khi click vào banner

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}