package com.ecommerce.app.repository;

import com.ecommerce.app.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepo extends JpaRepository<Region,Long> {
    Optional<Region> findByCode(String code);
}
