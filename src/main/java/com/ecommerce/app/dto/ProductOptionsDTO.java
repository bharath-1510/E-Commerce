package com.ecommerce.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ProductOptionsDTO {
    private String variantCategory;
    private String variantValue;
}
