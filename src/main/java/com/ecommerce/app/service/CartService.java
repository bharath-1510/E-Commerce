package com.ecommerce.app.service;

import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.CartItemRepo;
import com.ecommerce.app.repository.CartRepo;
import com.ecommerce.app.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public ResponseDTO<?> createCart(HttpServletRequest request, CartDTO cart) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user != (null)) {
            System.out.println(cart);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST, "", null);
    }
}
