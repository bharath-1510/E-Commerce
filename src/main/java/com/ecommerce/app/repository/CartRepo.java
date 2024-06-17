package com.ecommerce.app.repository;

import com.ecommerce.app.model.Cart;
import com.ecommerce.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,Long> {



    Optional<Cart> findByUser(User user);
}
