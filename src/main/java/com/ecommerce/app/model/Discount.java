package com.ecommerce.app.model;

import com.ecommerce.app.dto.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true,nullable = false)
    private  String code;
    @Column
    private String description;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    @Column(nullable = false)
    private Double value;
    @Column
    private Integer usageLimit;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

}
