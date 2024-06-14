package com.ecommerce.app.dto;

import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private Long id;
    private String code;
    private String description;
    private Double value;
    private DiscountType discountType;
    private Integer usageLimit;
}
