package com.ecommerce.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false)
    private String currencyCode;
    private Double taxRate;
    @OneToMany(mappedBy = "region")
    private List<ShippingOption> shippingOptions;
    @Column(unique = true)
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
