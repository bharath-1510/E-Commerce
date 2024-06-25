package com.ecommerce.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegionDTO {
    private Long id;
    private String code;
    private String name;
    private String currencyCode;
    private Double taxRate;
}
