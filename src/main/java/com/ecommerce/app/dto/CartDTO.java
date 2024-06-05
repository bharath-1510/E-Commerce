package com.ecommerce.app.dto;


import lombok.*;

import java.util.List;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CartDTO {
    private Long id;
    private List<CartItemDTO> cart;
}

