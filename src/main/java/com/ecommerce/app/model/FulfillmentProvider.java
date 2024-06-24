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
public class FulfillmentProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "fulfillmentProvider")
    private List<ShippingOption> shippingOptions;
    @OneToMany(mappedBy = "fulfillmentProvider")
    private List<ProviderOption> providerOptions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
