package com.ecommerce.app.repository;

import com.ecommerce.app.model.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepo extends JpaRepository<ProductVariants,Long> {
}
