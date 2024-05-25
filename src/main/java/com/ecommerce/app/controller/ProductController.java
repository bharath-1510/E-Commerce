package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProductDTO;

import com.ecommerce.app.dto.ProductVariantsDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.ProductOption;
import com.ecommerce.app.model.ProductOptionsVariants;
import com.ecommerce.app.model.ProductVariant;
import com.ecommerce.app.repository.ProductOptionsRepo;
import com.ecommerce.app.repository.ProductOptionsVariantsRepo;
import com.ecommerce.app.repository.ProductRepo;
import com.ecommerce.app.repository.ProductVariantsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/store")
public class ProductController {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductVariantsRepo productVariantsRepo;

    @Autowired
    ProductOptionsRepo productOptionsRepo;

    @Autowired
    ProductOptionsVariantsRepo productOptionsVariantsRepo;

    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<List<Product>>> getAllProducts() {
        return ResponseEntity.ok(new ResponseDTO<>(ResponseDTO.ResponseStatus.SUCCESS, "", productRepo.findAll()));
    }

    @GetMapping("/product")
    public ResponseEntity<ResponseDTO> getProduct(@RequestParam String productCode) {
        Product product = productRepo.findByCode(productCode);
        if (product != null)
            return ResponseEntity.ok(new ResponseDTO(ResponseDTO.ResponseStatus.SUCCESS, "", convertModelToDTO(product)));
        else
            return ResponseEntity.status(404).body(new ResponseDTO(ResponseDTO.ResponseStatus.ERROR, "Not Found", null));
    }

    @PostMapping("/products")
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product product = productRepo.save(convertDTOtoModel(productDTO));
            ProductDTO responseDto = convertModelToDTO(product);
            return ResponseEntity.ok(new ResponseDTO<ProductDTO>(ResponseDTO.ResponseStatus.SUCCESS, "", responseDto));
        } catch (DataIntegrityViolationException ex) {

            return ResponseEntity.status(406).body(new ResponseDTO<>(ResponseDTO.ResponseStatus.ERROR, "Duplicate Entry", null));
        }

    }

    @PostMapping("/products/variants")
    public ResponseEntity<ResponseDTO> addVariant(@RequestBody List<ProductVariantsDTO> productVariantsDTO) {
        try {
            Long productId = productVariantsDTO.stream().findFirst().get().getProductId();
            Product product = productRepo.findAll().stream().filter(x -> x.getId().equals(productId)).findFirst().get();
            if (product == null)
                return ResponseEntity.status(404).body(new ResponseDTO<>(ResponseDTO.ResponseStatus.ERROR, "Not Found", null));
            else {
                List<ProductVariant> productVariants = new ArrayList<>();
                productVariantsDTO.stream().forEach(
                        x -> {
                            productVariants.add(productVariantsRepo.save(convertDTOToModel(x, product)));
                        }
                );
                List<ProductOption> productOptions = new ArrayList<>();
                List<String> optionValues = new ArrayList<>();
                productVariantsDTO.forEach(
                        x -> {

                            for (Map.Entry<String, String> map : x.getOptions().entrySet()) {
                                if (!optionValues.contains(map.getKey())) {
                                    ProductOption options = new ProductOption();
                                    options.setProduct(product);
                                    options.setTitle(map.getKey());
                                    optionValues.add(map.getKey());
                                    productOptions.add(productOptionsRepo.save(options));
                                }
                            }

                        }
                );

                for(int i=0;i<productVariants.size();i++){
                    for(int j=0;j<productOptions.size();j++){
                        String value = productVariantsDTO.get(i).getOptions().entrySet().stream().collect(Collectors.toUnmodifiableList()).get(j).getValue();
                        ProductOptionsVariants productOptionsVariants = new ProductOptionsVariants();
                        productOptionsVariants.setOption(productOptions.get(j));
                        productOptionsVariants.setVariant(productVariants.get(i));
                        productOptionsVariants.setValue(value);
                        productOptionsVariantsRepo.save(productOptionsVariants);
                    }
                }
                return ResponseEntity.status(200).body(new ResponseDTO<>(ResponseDTO.ResponseStatus.SUCCESS, "", null));
            }
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(406).body(new ResponseDTO<>(ResponseDTO.ResponseStatus.ERROR, "Duplicate Entry", null));
        }

    }

    private ProductVariant convertDTOToModel(ProductVariantsDTO productVariantsDTO, Product product) {
        ProductVariant productVariants = new ProductVariant();
        productVariants.setProduct(product);
        productVariants.setTitle(productVariantsDTO.getTitle());
        productVariants.setPrice(productVariantsDTO.getPrice());
        productVariants.setSku(productVariantsDTO.getSku());
        productVariants.setInventoryQuantity(productVariantsDTO.getInventoryQuantity());
        return productVariants;
    }

    private ProductDTO convertModelToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCode(product.getCode());
        productDTO.setId(product.getId());
        productDTO.setDescription(product.getDescription());
        productDTO.setHandle(product.getHandle());
        productDTO.setType(product.getType());
        productDTO.setStatus(product.getStatus());
        productDTO.setTitle(product.getTitle());
        return productDTO;
    }

    private Product convertDTOtoModel(ProductDTO productDTO) {
        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setDescription(productDTO.getDescription());
        product.setTitle(productDTO.getTitle());
        product.setHandle(productDTO.getHandle());
        product.setType(productDTO.getType());
        product.setStatus(productDTO.getStatus());
        return product;
    }
}
