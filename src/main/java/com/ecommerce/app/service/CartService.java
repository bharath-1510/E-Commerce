package com.ecommerce.app.service;

import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.dto.CartItemDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            List<Cart> carts = cartRepository.findAllByUser(user);
            List<CartDTO> cartResponse = new ArrayList<>();
            for (Cart cart:carts){
                CartDTO singleCart = new CartDTO();
                singleCart.setId(cart.getId());
                singleCart.setCart(new ArrayList<>());
                List<CartItem> cartItems = cart.getCartItems();
                for(CartItem cartItem:cartItems){
                    CartItemDTO cartItemDTO = new CartItemDTO();
                    cartItemDTO.setQuantity(cartItem.getQuantity());
                    cartItemDTO.setVariantId(cartItem.getId());
                    singleCart.getCart().add(cartItemDTO);
                }
                cartResponse.add(singleCart);
            }

            return new ResponseDTO<>(HttpStatus.OK, "", cartResponse);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);

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
                ProductVariant variant = variantsRepository.findById(cartRequest.getCart().get(i).getVariantId()).orElse(null);
                if(variant!=null) {
                    cartItem.setCart(cart);

                    cartItem.setVariant(variant);
                    cartItem.setPrice(variant.getPrice());
                    cartItem.setQuantity(cartRequest.getCart().get(i).getQuantity());
                    variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
                    variantsRepository.save(variant);
                    cartItemRepository.save(cartItem);
                }
                else
                    return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Variant Not Found", null);
            }
            return new ResponseDTO<>(HttpStatus.CREATED, "Cart Created", cartRequest);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);

    }
}
