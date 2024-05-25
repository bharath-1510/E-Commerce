package com.ecommerce.app.repository;

import com.ecommerce.app.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantsRepo extends JpaRepository<ProductVariant,Long> {
}
