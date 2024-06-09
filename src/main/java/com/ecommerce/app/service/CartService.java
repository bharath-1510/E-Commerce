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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            Cart cart = cartRepository.findByUser(user).orElse(null);
            if (cart != null) {
                CartDTO singleCart = new CartDTO();
                singleCart.setId(cart.getId());
                singleCart.setCart(new ArrayList<>());
                List<CartItem> cartItems = cart.getCartItems();
                for (CartItem cartItem : cartItems) {
                    CartItemDTO cartItemDTO = new CartItemDTO();
                    cartItemDTO.setQuantity(cartItem.getQuantity());
                    cartItemDTO.setVariantId(cartItem.getId());
                    singleCart.getCart().add(cartItemDTO);
                }
                return new ResponseDTO<>(HttpStatus.OK, "", singleCart);
            } else
                return new ResponseDTO<>(HttpStatus.NO_CONTENT, "No Cart Found", null);


        } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);

    }

    @Transactional
    public ResponseDTO<?> createCart(HttpServletRequest request, CartDTO cartRequest) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user != (null)) {
            Cart cart = cartRepository.findByUser(user).orElse(null);
            if(cart!=null){
                return new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE, "Cart Already Created", null);
            }
            cart=new Cart();
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUser(user);
            cart = cartRepository.save(cart);
            cartRequest.setId(cart.getId());
            for (int i = 0; i < cartRequest.getCart().size(); i++) {
                CartItem cartItem = new CartItem();
                ProductVariant variant = variantsRepository.findById(cartRequest.getCart().get(i).getVariantId()).orElse(null);
                if (variant != null) {
                    cartItem.setCart(cart);

                    cartItem.setVariant(variant);
                    cartItem.setPrice(variant.getPrice());
                    cartItem.setQuantity(cartRequest.getCart().get(i).getQuantity());
                    variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
                    variantsRepository.save(variant);
                    cartItemRepository.save(cartItem);
                } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Variant Not Found", null);
            }
            return new ResponseDTO<>(HttpStatus.CREATED, "Cart Created", cartRequest);
        } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);

    }

    public ResponseDTO<?> deleteCart(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user != null) {
            Cart cart = cartRepository.findByUser(user).orElse(null);
            if (cart != null) {
                List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElse(null);
                if (cartItems != null) {
                    for (CartItem cartItem : cartItems)
                        cartItemRepository.delete(cartItem);
                }
                cartRepository.delete(cart);
                return new ResponseDTO<>(HttpStatus.OK, "Deleted Successfully", null);
            } else
                return new ResponseDTO<>(HttpStatus.NOT_MODIFIED, "No Cart Found", null);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
    }

    public ResponseDTO<?> updateCart(HttpServletRequest request, CartDTO cart) {
        return null;
    }
}
