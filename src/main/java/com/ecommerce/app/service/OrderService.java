package com.ecommerce.app.service;

import com.ecommerce.app.dto.*;
import com.ecommerce.app.model.*;
import com.ecommerce.app.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepo userRepository;
    @Autowired
    ShippingOptionRepo shippingOptionRepo;
    @Autowired
    ProductVariantsRepo productVariantsRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private DiscountRepo discountRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private ShippingDetailRepo shippingDetailRepo;

    public ResponseDTO<?> getOrder(Long id) {
        try {
            Order order = orderRepo.findById(id).orElse(null);
            if (order == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Order not Found", null);

            return new ResponseDTO<>(HttpStatus.OK, "Order Found", convertModelToDTO(order));
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }

    private static List<OrderItemDTO> getOrderItemDTOS(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemDTO> orderItemList = new ArrayList<>();
        orderItems.forEach(
                orderItem -> {
                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setCode(orderItem.getCode());
                    dto.setPrice(orderItem.getPrice());
                    dto.setQuantity(orderItem.getQuantity());
                    orderItemList.add(dto);
                }
        );
        return orderItemList;
    }

    public ResponseDTO<?> placeOrder(HttpServletRequest request, OrderDTO order) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            Order orderCreate = new Order();
            String discountCode = order.getDiscountCode();
            String discountType = null;
            Double discountAmount=0.0;
            Double totalPrice= Double.valueOf(0);
            List<OrderItemDTO> orderItems = order.getOrderItems();
            List<OrderItem> items = new ArrayList<>();
            for (OrderItemDTO dto : orderItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setCode(dto.getCode());
                orderItem.setPrice(dto.getPrice());
                orderItem.setQuantity(dto.getQuantity());
                orderItem = orderItemRepo.save(orderItem);
                items.add(orderItem);
                totalPrice += dto.getPrice() * dto.getQuantity();
            }
            if(!discountCode.isEmpty())
            {
                Discount discount = discountRepo.findByCode(discountCode).get();
                Integer usageLimit = discount.getUsageLimit();

                if(discount.getExpiresAt().isBefore(LocalDateTime.now()) && usageLimit<=0)
                    return new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE, "Discount Expired",null);
                discountType = discount.getDiscountType().toString();
                discountAmount = discount.getValue();
                discount.setUsageLimit(--usageLimit);
                orderCreate.setDiscount(discount);
            }

            ShippingOption shippingOption = shippingOptionRepo.findByCode(order.getShippingCode()).get();
            Double shippingAmount = shippingOption.getAmount();
            ShippingDetail shippingDetail = new ShippingDetail();
            shippingDetail.setShippingOption(shippingOption);
            shippingDetail.setDeliveryStatus(false);
            orderCreate.setShippingDetail(shippingDetail);
            if(discountType!=null )
            {
                if( discountType.equals("FREE_SHIPPING"))
                {
                    shippingAmount= Double.valueOf(0);
                }
                if( discountType.equals("PERCENTAGE"))
                {
                    Double discountPrice = totalPrice*(discountAmount/100);
                    totalPrice = totalPrice - discountPrice;
                }
                if( discountType.equals("FIXED_AMOUNT"))
                {
                    totalPrice-=discountAmount;
                }
            }
            totalPrice+=shippingAmount;
            totalPrice = totalPrice<=0?0:totalPrice;
            orderCreate.setTotalPrice(totalPrice);
            orderCreate.setOrderDate(LocalDateTime.now());
            orderCreate.setUser(user);
            orderCreate.setAddress(
                    addressRepo.findAll().stream().filter(address -> address.getUser().getId().equals(user != null ? user.getId() : null))
                            .toList().stream().filter(address -> address.getStreet().equals(order.getAddress().getStreet())).findFirst().get()
            );
            orderCreate.setOrderItems(items);
            orderRepo.save(orderCreate);
            return new ResponseDTO<>(HttpStatus.OK, "Order Placed",null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> deleteOrder(Long id) {
        try {
            Order order = orderRepo.findById(id).orElse(null);
            if (order == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Order not Found", null);
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(
                    orderItem -> {
                        String code = orderItem.getCode();
                        ProductVariant productVariant = productVariantsRepo.findByCode(code).get();
                        productVariant.setStockQuantity(productVariant.getStockQuantity()+orderItem.getQuantity());
                        productVariant.setUpdatedAt(LocalDateTime.now());
                        productVariantsRepo.save(productVariant);
                        orderItemRepo.delete(orderItem);
                    }
            );
            shippingDetailRepo.delete(order.getShippingDetail());
            if(order.getDiscount()!=null) {
                Discount discount = discountRepo.findByCode(order.getDiscount().getCode()).get();
                discount.setUpdatedAt(LocalDateTime.now());
                discount.setUsageLimit(discount.getUsageLimit() + 1);
                discountRepo.save(discount);
            }
            orderRepo.delete(order);
            return new ResponseDTO<>(HttpStatus.OK, "Order Deleted", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> getAllOrder(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            List<Order> ordersFound = orderRepo.findAll().stream().filter(
                    orderFound -> orderFound.getUser().getId().equals(user.getId())
            ).toList();
            if (ordersFound.isEmpty())
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Order not Found", null);
            List<OrderDTO> orders = new ArrayList<>();
            ordersFound.stream().forEach(
                    order -> {
                        orders.add(convertModelToDTO(order));
                    }
            );
            return new ResponseDTO<>(HttpStatus.OK, "Order Found", orders);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    private OrderDTO convertModelToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDiscountCode(order.getDiscount().getCode());
        orderDTO.setShippingCode(order.getShippingDetail().getShippingOption().getCode());
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(order.getAddress().getCity());
        addressDTO.setStreet(order.getAddress().getStreet());
        addressDTO.setCountry(order.getAddress().getCountry());
        addressDTO.setPostalCode(order.getAddress().getPostalCode());
        addressDTO.setPhoneNumber(order.getAddress().getPhoneNumber());
        List<OrderItemDTO> orderItemList = getOrderItemDTOS(order);
        orderDTO.setOrderItems(orderItemList);
        orderDTO.setAddress(addressDTO);
        return orderDTO;
    }
}
