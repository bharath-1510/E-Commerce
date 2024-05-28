package com.ecommerce.app.repository;

import com.ecommerce.app.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Long> {
}
