package com.ecommerce.app.controller;

import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartService cartService;
    @GetMapping("/user/cart")
    public ResponseEntity<?> getCartDetails(
            HttpServletRequest request
    ) {
        ResponseDTO<?> responseDTO = cartService.getCartDetails(request);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PostMapping("/user/cart")
    public ResponseEntity<?> createCart(
            HttpServletRequest request,@RequestBody CartDTO cart
            ) {
        ResponseDTO<?> responseDTO = cartService.createCart(request,cart);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PutMapping("/user/cart")
    public ResponseEntity<?> updateCart(
            HttpServletRequest request,@RequestBody CartDTO cart
    ) {
        ResponseDTO<?> responseDTO = cartService.updateCart(request,cart);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @DeleteMapping("/user/cart")
    public ResponseEntity<?> deleteCart(
            HttpServletRequest request
    ) {
        ResponseDTO<?> responseDTO = cartService.deleteCart(request);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
