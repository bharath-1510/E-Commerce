package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProductDTO;

import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/store")
public class ProductController {
    @Autowired
    ProductRepo productRepo;
    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<List<Product>>> getAllProducts(){
        return ResponseEntity.ok(new ResponseDTO<>(ResponseDTO.ResponseStatus.SUCCESS,"",productRepo.findAll()));
    }
    @GetMapping("/product")
    public ResponseEntity<ResponseDTO> getProduct(@RequestParam String productCode){
        Product product = productRepo.findByProductCode(productCode);
        if(product!=null)
        return ResponseEntity.ok(new ResponseDTO(ResponseDTO.ResponseStatus.SUCCESS,"",convertModelToDTO(product)));
        else
            return ResponseEntity.status(404).body(new ResponseDTO(ResponseDTO.ResponseStatus.ERROR,"Not Found",null));
    }
    @PostMapping("/products")
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody ProductDTO productDTO){
        try {
            Product product = productRepo.save(convertDTOtoModel(productDTO));
            ProductDTO responseDto = convertModelToDTO(product);
            return ResponseEntity.ok(new ResponseDTO<ProductDTO>(ResponseDTO.ResponseStatus.SUCCESS,"",responseDto));
        }
        catch (DataIntegrityViolationException ex){

            return ResponseEntity.status(406).body(new ResponseDTO<>(ResponseDTO.ResponseStatus.ERROR,"Duplicate Entry",null));
        }

    }

    private ProductDTO convertModelToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductCode(product.getProductCode());
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
        product.setProductCode(productDTO.getProductCode());
        product.setDescription(productDTO.getDescription());
        product.setTitle(productDTO.getTitle());
        product.setHandle(productDTO.getHandle());
        product.setType(productDTO.getType());
        product.setStatus(productDTO.getStatus());
        return product;
    }
}
