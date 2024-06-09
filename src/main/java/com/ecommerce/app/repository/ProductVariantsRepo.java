package com.ecommerce.app.repository;

import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantsRepo extends JpaRepository<ProductVariant,Long> {
    List<ProductVariant> findAllByProduct(Product product);
    Optional<ProductVariant> findById(Long id);
}
