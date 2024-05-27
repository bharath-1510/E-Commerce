package com.ecommerce.app.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String code;
    private Long id;
    private String title;
    private String description;
    private Boolean status;
    private String type;
    private List<ProductVariantsDTO> variants;
}
