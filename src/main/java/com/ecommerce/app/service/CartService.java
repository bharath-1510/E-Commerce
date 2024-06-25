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
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != (null)) {
                Cart cart = cartRepository.findByUser(user).orElse(null);
                if (cart != null) {
                    CartDTO singleCart = new CartDTO();
                    singleCart.setId(cart.getId());
                    singleCart.setCartItems(new ArrayList<>());
                    List<CartItem> cartItems = cart.getCartItems();
                    for (CartItem cartItem : cartItems) {
                        CartItemDTO cartItemDTO = new CartItemDTO();
                        cartItemDTO.setQuantity(cartItem.getQuantity());
                        cartItemDTO.setVariantId(cartItem.getId());
                        singleCart.getCartItems().add(cartItemDTO);
                    }
                    return new ResponseDTO<>(HttpStatus.OK, "Cart Found", singleCart);
                } else
                    return new ResponseDTO<>(HttpStatus.NOT_FOUND, "No Cart Found", null);
            } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    @Transactional
    public ResponseDTO<?> createCart(HttpServletRequest request, CartDTO cartRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != (null)) {
                Cart cart = cartRepository.findByUser(user).orElse(null);
                if (cart != null) {
                    return new ResponseDTO<>(HttpStatus.CONFLICT, "Cart Already Exists", null);
                }
                cart = new Cart();
                cart.setCreatedAt(LocalDateTime.now());
                cart.setUser(user);
                cart = cartRepository.save(cart);
                cartRequest.setId(cart.getId());
                for (int i = 0; i < cartRequest.getCartItems().size(); i++) {
                    CartItem cartItem = new CartItem();
                    ProductVariant variant = variantsRepository.findById(cartRequest.getCartItems().get(i).getVariantId()).orElse(null);
                    if (variant != null) {
                        cartItem.setCart(cart);
                        cartItem.setVariant(variant);
                        cartItem.setPrice(variant.getPrice());
                        cartItem.setQuantity(cartRequest.getCartItems().get(i).getQuantity());
                        variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
                        variantsRepository.save(variant);
                        cartItemRepository.save(cartItem);
                    } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Variant Not Found", null);
                }
                return new ResponseDTO<>(HttpStatus.CREATED, "Cart Created", cartRequest);
            } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> deleteCart(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != null) {
                Cart cart = cartRepository.findByUser(user).orElse(null);
                if (cart != null) {
                    List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElse(null);
                    if (cartItems != null) {
                        for (CartItem cartItem : cartItems) {
                            ProductVariant variant = variantsRepository.findById(cartItem.getVariant().getId()).orElse(null);
                            if (variant != null) {
                                variant.setStockQuantity(variant.getStockQuantity() + cartItem.getQuantity());
                                variantsRepository.save(variant);
                            }
                            cartItemRepository.delete(cartItem);
                        }
                    }
                    cartRepository.delete(cart);
                    return new ResponseDTO<>(HttpStatus.ACCEPTED, "Deleted Successfully", null);
                } else
                    return new ResponseDTO<>(HttpStatus.NOT_FOUND, "No Cart Found", null);
            } else
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> updateCart(HttpServletRequest request, CartDTO cartRequest) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != null) {
                Cart cart = cartRepository.findByUser(user).orElse(null);
                if (cart != null) {
                    List<CartItem> cartItems = cartItemRepository.findAllByCart(cart).orElse(null);
                    if (cartItems != null) {
                        List<Long> variantIds = new ArrayList<>();
                        for (CartItem cartItem : cartItems) {
                            variantIds.add(cartItem.getVariant().getId());
                        }
                        List<CartItemDTO> cartItems1 = cartRequest.getCartItems();
                        for (int i = 0; i < cartItems1.size(); i++) {
                            if (variantIds.contains(cartItems1.get(i).getVariantId())) {
                                int finalI = i;
                                CartItem cartItem = cartItems.stream().filter(x -> x.getVariant().getId().equals(cartItems1.get(finalI).getVariantId())).findFirst().get();
                                ProductVariant variant = variantsRepository.findById(cartItems1.get(i).getVariantId()).orElse(null);
                                if (variant != null) {
                                    variant.setStockQuantity(variant.getStockQuantity() + cartItem.getQuantity());
                                    cartItem.setQuantity(cartItems1.get(i).getQuantity());
                                    variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
                                    variantsRepository.save(variant);
                                    cartItemRepository.save(cartItem);
                                } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Variant Not Found", null);

                            } else {
                                CartItem cartItem = new CartItem();
                                ProductVariant variant = variantsRepository.findById(cartItems1.get(i).getVariantId()).orElse(null);
                                if (variant != null) {
                                    cartItem.setCart(cart);
                                    cartItem.setVariant(variant);
                                    cartItem.setPrice(variant.getPrice());
                                    cartItem.setQuantity(cartItems1.get(i).getQuantity());
                                    variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());
                                    variantsRepository.save(variant);
                                    cartItemRepository.save(cartItem);
                                } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Variant Not Found", null);
                            }
                        }
                    }
                    cart.setUpdatedAt(LocalDateTime.now());
                    cartRepository.save(cart);
                    return new ResponseDTO<>(HttpStatus.ACCEPTED, "Updated Successfully", null);
                } else
                    return new ResponseDTO<>(HttpStatus.NOT_MODIFIED, "No Cart Found", null);
            } else
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }
}
