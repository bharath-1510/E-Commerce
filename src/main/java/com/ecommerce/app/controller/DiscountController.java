package com.ecommerce.app.controller;

import com.ecommerce.app.dto.CartDTO;
import com.ecommerce.app.dto.DiscountDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.service.CartService;
import com.ecommerce.app.service.DiscountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class DiscountController {
    @Autowired
    DiscountService discountService;
    @GetMapping("/discount")
    public ResponseEntity<?> getDiscountByCode(
            String code
    ) {
        ResponseDTO<?> responseDTO = discountService.getDiscountByCode(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @GetMapping("/admin/discount")
    public ResponseEntity<?> getAllDiscount(
            
    ) {
        ResponseDTO<?> responseDTO = discountService.getAllDiscount();
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PostMapping("/admin/discount")
    public ResponseEntity<?> createDiscount(
            @RequestBody DiscountDTO discount
    ) {
        ResponseDTO<?> responseDTO = discountService.createDiscount(discount);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PutMapping("/admin/discount")
    public ResponseEntity<?> updateDiscount(
            @RequestBody DiscountDTO discount
    ) {
        ResponseDTO<?> responseDTO = discountService.updateDiscount(discount);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @DeleteMapping("/admin/discount")
    public ResponseEntity<?> deleteDiscount(
            String code
    ) {
        ResponseDTO<?> responseDTO = discountService.deleteDiscount(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
