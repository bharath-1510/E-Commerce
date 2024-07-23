package com.ecommerce.app.dto;

import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private String code;
    private Integer quantity;
    private Double price;
}
