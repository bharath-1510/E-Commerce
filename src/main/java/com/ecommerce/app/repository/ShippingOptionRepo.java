package com.ecommerce.app.repository;

import com.ecommerce.app.model.ShippingOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingOptionRepo extends JpaRepository<ShippingOption,Long> {
    Optional<ShippingOption> findByCode(String code);
}
