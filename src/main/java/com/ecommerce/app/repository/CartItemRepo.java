package com.ecommerce.app.repository;

import com.ecommerce.app.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {
}
