package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProductDTO;

import com.ecommerce.app.dto.ProductOptionsDTO;
import com.ecommerce.app.dto.ProductVariantsDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.ProductOption;
import com.ecommerce.app.model.ProductVariant;
import com.ecommerce.app.repository.ProductOptionsRepo;
import com.ecommerce.app.repository.ProductRepo;
import com.ecommerce.app.repository.ProductVariantsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductVariantsRepo productVariantsRepo;

    @Autowired
    ProductOptionsRepo productOptionsRepo;


    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<?>> getAllProducts(
            @RequestParam Optional<Integer> page,@RequestParam Optional<String> sortBy
    ) {
        if (page.isEmpty()) {
            List<Product> products = productRepo.findAll();
            List<ProductDTO> responseDTO = new LinkedList<>();
            products.forEach(
                    x -> responseDTO.add(convertModelToDTO(x, productVariantsRepo.findAllByProduct(x), productOptionsRepo.findAllByProduct(x)))
            );
            return ResponseEntity.ok(new ResponseDTO<>(ResponseDTO.ResponseStatus.SUCCESS, "", responseDTO));
        }
        else{
           Page<Product> productPage = productRepo.findAll(
                   PageRequest.of(page.get(),3, Sort.by(Sort.Direction.ASC, sortBy.orElse("id")))
           ) ;
            List<ProductDTO> responseDTO = new LinkedList<>();
            productPage.forEach(
                    x -> responseDTO.add(convertModelToDTO(x, productVariantsRepo.findAllByProduct(x), productOptionsRepo.findAllByProduct(x)))
            );
            return ResponseEntity.ok(new ResponseDTO<>(ResponseDTO.ResponseStatus.SUCCESS, "", responseDTO));
        }
    }

    @GetMapping("/product")
    public ResponseEntity<ResponseDTO> getProduct(@RequestParam String code) {
        Product product = productRepo.findByCode(code);
        if (product != null) {
            List<ProductVariant> productVariants = productVariantsRepo.findAllByProduct(product);
            List<ProductOption> productOptions = productOptionsRepo.findAllByProduct(product);
            return ResponseEntity.ok(new ResponseDTO(ResponseDTO.ResponseStatus.SUCCESS, "", convertModelToDTO(product, productVariants, productOptions)));
        } else
            return ResponseEntity.status(404).body(new ResponseDTO(ResponseDTO.ResponseStatus.ERROR, "Not Found", null));
    }

    private ProductDTO convertModelToDTO(Product product, List<ProductVariant> productVariants, List<ProductOption> productOptions) {
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

    @PostMapping("/products")
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product product = productRepo.save(convertDTOtoModel(productDTO));
            productDTO.setId(product.getId());
            for (int i = 0; i < productDTO.getVariants().size(); i++) {
                ProductVariant productVariant = productVariantsRepo.save(convertDTOtoModel(productDTO.getVariants().get(i), product));
                productDTO.getVariants().get(i).setId(productVariant.getId());
                for (int j = 0; j < productDTO.getVariants().get(i).getOptions().size(); j++) {
                    String variantCategory = productDTO.getVariants().get(i).getOptions().get(j).getVariantCategory();
                    String variantValue = productDTO.getVariants().get(i).getOptions().get(j).getVariantValue();
                    ProductOption productOption = new ProductOption();
                    productOption.setProduct(product);
                    productOption.setCreatedAt(LocalDateTime.now());
                    productOption.setCategory(variantCategory);
                    productOption.setValue(variantValue);
                    productOption.setVariant(productVariant);
                    productOptionsRepo.save(productOption);

                }
            }

            return ResponseEntity.ok(new ResponseDTO<>(ResponseDTO.ResponseStatus.SUCCESS, "", productDTO));
        } catch (DataIntegrityViolationException ex) {

            return ResponseEntity.status(406).body(new ResponseDTO<>(ResponseDTO.ResponseStatus.ERROR, "Duplicate Entry", null));
        }

    }

    private ProductVariant convertDTOtoModel(ProductVariantsDTO productVariantsDTO, Product product) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setProduct(product);
        productVariant.setCreatedAt(LocalDateTime.now());
        productVariant.setSku(productVariantsDTO.getSku());
        productVariant.setStockQuantity(productVariantsDTO.getStockQuantity());
        productVariant.setPrice(productVariantsDTO.getPrice());
        return productVariant;
    }


    private Product convertDTOtoModel(ProductDTO productDTO) {
        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setDescription(productDTO.getDescription());
        product.setTitle(productDTO.getTitle());
        product.setType(productDTO.getType());
        product.setStatus(productDTO.getStatus());
        return product;
    }
}
