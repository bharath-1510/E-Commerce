package com.ecommerce.app.dto;

import com.ecommerce.app.model.Product;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductVariantsDTO {
    private Long id;
    private String sku;
    private Double price;
    private Long stockQuantity;
    private List<ProductOptionsDTO> options;
}
