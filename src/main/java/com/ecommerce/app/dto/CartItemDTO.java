package com.ecommerce.app.dto;

import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO{
    private Long variantId;
    private int quantity;

}
