package com.ecommerce.app.dto;

import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDTO {
    private Long id;
    private String name;
    private String code;
    private Double amount;
    private String regionCode;
    private String providerCode;
}
