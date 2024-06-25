package com.ecommerce.app.repository;

import com.ecommerce.app.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepo extends JpaRepository<Provider,Long> {
    Optional<Provider> findByCode(String code);
}
