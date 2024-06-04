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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.ecommerce.app.converter.ProductConverter.convertDTOtoModel;
import static com.ecommerce.app.converter.ProductConverter.convertModelToDTO;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductVariantsRepo productVariantsRepo;

    @Autowired
    ProductOptionsRepo productOptionsRepo;

    public ResponseDTO getProducts(Optional<Integer> page, Optional<String> sortBy) {
        if (page.isEmpty()) {
            List<Product> products = productRepo.findAll();
            List<ProductDTO> responseDTO = new LinkedList<>();
            products.forEach(
                    x -> responseDTO.add(convertModelToDTO(x, productVariantsRepo.findAllByProduct(x), productOptionsRepo.findAllByProduct(x)))
            );
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "", responseDTO);
        }
        else{
            Page<Product> productPage = productRepo.findAll(
                    PageRequest.of(page.get(),3, Sort.by(Sort.Direction.ASC, sortBy.orElse("id")))
            ) ;
            List<ProductDTO> responseDTO = new LinkedList<>();
            productPage.forEach(
                    x -> responseDTO.add(convertModelToDTO(x, productVariantsRepo.findAllByProduct(x), productOptionsRepo.findAllByProduct(x)))
            );
            return new ResponseDTO<>(HttpStatus.OK, "", responseDTO);
        }
    }

    public ResponseDTO getProduct(String code) {
        Product product = productRepo.findByCode(code);
        if (product != null) {
            List<ProductVariant> productVariants = productVariantsRepo.findAllByProduct(product);
            List<ProductOption> productOptions = productOptionsRepo.findAllByProduct(product);
            return new ResponseDTO(HttpStatus.OK, "", convertModelToDTO(product, productVariants, productOptions));
        } else
            return new ResponseDTO(HttpStatus.NOT_FOUND, "Not Found", null);
    }

    public ResponseDTO createProduct(ProductDTO productDTO) {
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

            return new ResponseDTO<>(HttpStatus.CREATED, "", productDTO);
        } catch (DataIntegrityViolationException ex) {

            return new ResponseDTO<>(HttpStatus.CONFLICT, "Duplicate Entry", null);
        }
    }
}
