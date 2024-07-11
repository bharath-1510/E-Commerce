package com.ecommerce.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.dialect.BooleanDecoder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShippingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private Boolean deliveryStatus=false;
    @ManyToOne
    @JoinColumn(name = "shippingOption_id")
    private ShippingOption shippingOption;
}
