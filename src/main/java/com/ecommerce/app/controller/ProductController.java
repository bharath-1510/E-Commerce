package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProductDTO;

import com.ecommerce.app.model.Product;
import com.ecommerce.app.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/store")
public class ProductController {
    @Autowired
    ProductRepo productRepo;
    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){
        Product product = productRepo.save(convertDTOtoModel(productDTO));
        ProductDTO responseDto = convertModelToDTO(product);
        return ResponseEntity.ok(responseDto);
    }

    private ProductDTO convertModelToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setDescription(product.getDescription());
        productDTO.setHandle(product.getHandle());
        productDTO.setType(product.getType());
        productDTO.setStatus(product.getStatus());
        productDTO.setTitle(product.getTitle());
        return productDTO;
    }

    private Product convertDTOtoModel(ProductDTO productDTO){
        Product product = new Product();
        product.setDescription(productDTO.getDescription());
        product.setTitle(productDTO.getTitle());
        product.setHandle(productDTO.getHandle());
        product.setType(productDTO.getType());
        product.setStatus(productDTO.getStatus());
        return product;
    }
}
