package com.ecommerce.app.service;

import com.ecommerce.app.dto.ProductDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.ProductOption;
import com.ecommerce.app.model.ProductVariant;
import com.ecommerce.app.repository.ProductOptionsRepo;
import com.ecommerce.app.repository.ProductRepo;
import com.ecommerce.app.repository.ProductVariantsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.ecommerce.app.utility.ProductConverter.convertDTOtoModel;
import static com.ecommerce.app.utility.ProductConverter.convertModelToDTO;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductVariantsRepo productVariantsRepo;

    @Autowired
    ProductOptionsRepo productOptionsRepo;

    public ResponseDTO<?> getProducts(Optional<Integer> page, Optional<String> sortBy) {
        try {
            if (page.isEmpty() && sortBy.isEmpty()) {
                List<Product> products = productRepo.findAll();
                List<ProductDTO> responseDTO = new LinkedList<>();
                products.forEach(
                        x -> responseDTO.add(convertModelToDTO(x, productVariantsRepo.findAllByProduct(x), productOptionsRepo.findAllByProduct(x)))
                );
                return new ResponseDTO<>(HttpStatus.OK, "Products are fetched", responseDTO);
            } else {
                Page<Product> productPage = productRepo.findAll(
                        PageRequest.of(page.orElse(0), 10, Sort.by(Sort.Direction.ASC, sortBy.orElse("id")))
                );
                List<ProductDTO> responseDTO = new LinkedList<>();
                productPage.forEach(
                        x -> responseDTO.add(convertModelToDTO(x, productVariantsRepo.findAllByProduct(x), productOptionsRepo.findAllByProduct(x)))
                );
                return new ResponseDTO<>(HttpStatus.OK, "Products are fetched", responseDTO);
            }
        } catch (Exception e) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public ResponseDTO<?> getProduct(String code) {
        try {
            Product product = productRepo.findByCode(code);
            if (product != null) {
                List<ProductVariant> productVariants = productVariantsRepo.findAllByProduct(product);
                List<ProductOption> productOptions = productOptionsRepo.findAllByProduct(product);
                return new ResponseDTO<>(HttpStatus.OK, "Product Fetched", convertModelToDTO(product, productVariants, productOptions));
            } else
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);

        }
    }

    public ResponseDTO<?> createProduct(ProductDTO productDTO) {
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

            return new ResponseDTO<>(HttpStatus.CREATED, "Product Created", productDTO);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }
}
