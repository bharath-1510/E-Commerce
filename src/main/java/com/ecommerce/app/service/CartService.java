package com.ecommerce.app.service;

import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Cart;
import com.ecommerce.app.model.CartItem;
import com.ecommerce.app.model.ProductVariant;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.CartItemRepo;
import com.ecommerce.app.repository.CartRepo;
import com.ecommerce.app.repository.ProductVariantsRepo;
import com.ecommerce.app.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartService {
    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepo userRepository;
    @Autowired
    CartRepo cartRepository;
    @Autowired
    CartItemRepo cartItemRepository;
    @Autowired
    ProductVariantsRepo variantsRepository;

    public ResponseDTO<?> getCartDetails(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user != (null)) {
            System.out.println(cartRepository.findAllByUser(user));
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST, "", null);
    }

    public ResponseDTO<?> createCart(HttpServletRequest request, CartDTO cartRequest) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user != (null)) {
            Cart cart = new Cart();
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUser(user);
            cart = cartRepository.save(cart);
            cartRequest.setId(cart.getId());
            for (int i = 0; i < cartRequest.getCart().size(); i++) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                ProductVariant variant = variantsRepository.findById(cartRequest.getCart().get(i).getVariantId()).get();
                cartItem.setVariant(variant);
                cartItem.setPrice(variant.getPrice());
                cartItem.setQuantity(cartRequest.getCart().get(i).getQuantity());
                variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
                variantsRepository.save(variant);
                cartItemRepository.save(cartItem);
            }
            return new ResponseDTO<>(HttpStatus.CREATED, "Cart Created", cartRequest);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);

    }
}
