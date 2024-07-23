package com.ecommerce.app.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderDTO {
    private AddressDTO address;
    private String discountCode;
    private String shippingCode;
    private List<OrderDTO> orderItems;
}
