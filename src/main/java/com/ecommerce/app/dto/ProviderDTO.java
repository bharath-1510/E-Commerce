package com.ecommerce.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProviderDTO {
    private Long id;
    private String name;
    private String code;
    private List<ProviderOptionDTO> providerOptions;
}
