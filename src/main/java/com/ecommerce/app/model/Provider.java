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
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String code;
    @OneToMany(mappedBy = "provider")
    private List<ShippingOption> shippingOptions;
    @OneToMany(mappedBy = "provider")
    private List<ProviderOption> providerOptions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
