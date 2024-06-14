package com.ecommerce.app.repository;

import com.ecommerce.app.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepo extends JpaRepository<Discount,Long> {
    Optional<Discount> findByCode(String code);
}
