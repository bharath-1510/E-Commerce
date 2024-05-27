package com.ecommerce.app.repository;

import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.ProductOption;
import com.ecommerce.app.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionsRepo extends JpaRepository<ProductOption,Long> {
    List<ProductOption> findAllByProduct(Product product);
}
