package com.ecommerce.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productCode;
    private Long productId;
    private String title;
    private String description;
    private String handle;
    private Boolean status;
    private String type;
}
