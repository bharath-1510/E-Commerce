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
public class ShippingOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String code;
    private Double amount;
    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @OneToMany(mappedBy = "shippingOption")
    private List<ShippingDetail> shippingDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
