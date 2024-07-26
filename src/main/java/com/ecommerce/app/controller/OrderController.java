package com.ecommerce.app.controller;


import com.ecommerce.app.dto.DiscountDTO;
import com.ecommerce.app.dto.OrderDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.service.OrderService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrder(HttpServletRequest request) {
        ResponseDTO<?> responseDTO = orderService.getAllOrder(request);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @GetMapping("/order")
    public ResponseEntity<?> getOrder(Long id) {
        ResponseDTO<?> responseDTO = orderService.getOrder(id);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            HttpServletRequest request, @RequestBody OrderDTO order
            ) {
        ResponseDTO<?> responseDTO = orderService.placeOrder(request,order);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @DeleteMapping("/order")
    public ResponseEntity<?> deleteOrder(Long id) {
        ResponseDTO<?> responseDTO = orderService.deleteOrder(id);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
