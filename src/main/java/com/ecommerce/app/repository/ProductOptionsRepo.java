package com.ecommerce.app.repository;

import com.ecommerce.app.model.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionsRepo extends JpaRepository<ProductOption,Long> {
}
