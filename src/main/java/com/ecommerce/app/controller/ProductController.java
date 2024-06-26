package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProductDTO;

import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.ProductOption;
import com.ecommerce.app.model.ProductVariant;
import com.ecommerce.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.ecommerce.app.converter.ProductConverter.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;
    @GetMapping("/user/products")
    public ResponseEntity getAllProducts(
            @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy
    ) {
        ResponseDTO responseDTO = productService.getProducts(page,sortBy);
       return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @GetMapping("/product")
    public ResponseEntity<ResponseDTO> getProduct(@RequestParam String code) {
        ResponseDTO responseDTO = productService.getProduct(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }



    @PostMapping("/admin/products")
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ResponseDTO responseDTO = productService.createProduct(productDTO);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }


}
