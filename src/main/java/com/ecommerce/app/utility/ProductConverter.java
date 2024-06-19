package com.ecommerce.app.utility;

import com.ecommerce.app.dto.ProductDTO;
import com.ecommerce.app.dto.ProductOptionsDTO;
import com.ecommerce.app.dto.ProductVariantsDTO;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.ProductOption;
import com.ecommerce.app.model.ProductVariant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {
    public static ProductDTO convertModelToDTO(Product product, List<ProductVariant> productVariants, List<ProductOption> productOptions) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle(product.getTitle());
        productDTO.setStatus(product.getStatus());
        productDTO.setType(product.getType());
        productDTO.setCode(product.getCode());
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        List<ProductVariantsDTO> variants = new ArrayList<>();
        for (int i = 0; i < productVariants.size(); i++) {
            ProductVariantsDTO dto = new ProductVariantsDTO();
            dto.setId(productVariants.get(i).getId());
            dto.setSku(productVariants.get(i).getSku());
            dto.setStockQuantity(productVariants.get(i).getStockQuantity());
            dto.setPrice(productVariants.get(i).getPrice());
            int finalI = i;
            List<ProductOption> option = productOptions.stream().filter(x -> x.getProduct().getId().equals(product.getId())).filter(x -> x.getVariant().getId().equals(productVariants.get(finalI).getId())).collect(Collectors.toList());
            List<ProductOptionsDTO> optionDto = new ArrayList<>();
            option.forEach(
                    x -> {
                        optionDto.add(new ProductOptionsDTO(x.getCategory(), x.getValue()));
                    }
            );
            dto.setOptions(optionDto);
            variants.add(dto);
        }
        productDTO.setVariants(variants);
        return productDTO;
    }
    public static ProductVariant convertDTOtoModel(ProductVariantsDTO productVariantsDTO, Product product) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setProduct(product);
        productVariant.setCreatedAt(LocalDateTime.now());
        productVariant.setSku(productVariantsDTO.getSku());
        productVariant.setStockQuantity(productVariantsDTO.getStockQuantity());
        productVariant.setPrice(productVariantsDTO.getPrice());
        return productVariant;
    }


    public static Product convertDTOtoModel(ProductDTO productDTO) {
        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setDescription(productDTO.getDescription());
        product.setTitle(productDTO.getTitle());
        product.setType(productDTO.getType());
        product.setStatus(productDTO.getStatus());
        return product;
    }
}
