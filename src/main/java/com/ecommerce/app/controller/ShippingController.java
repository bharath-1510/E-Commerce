package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.dto.ShippingDTO;
import com.ecommerce.app.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShippingController {
    @Autowired
    ShippingService shippingService;

    @GetMapping("/admin/shipping")
    public ResponseEntity<?> getShippingByCode(
            String code
    ) {
        ResponseDTO<?> responseDTO = shippingService.getShippingByCode(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @GetMapping("/admin/shippings")
    public ResponseEntity<?> getAllShipping(

    ) {
        ResponseDTO<?> responseDTO = shippingService.getAllShipping();
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/admin/shipping")
    public ResponseEntity<?> createShipping(
            @RequestBody ShippingDTO shipping
    ) {
        ResponseDTO<?> responseDTO = shippingService.createShipping(shipping);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PutMapping("/admin/shipping")
    public ResponseEntity<?> updateShipping(
            @RequestBody ShippingDTO shipping
    ) {
        ResponseDTO<?> responseDTO = shippingService.updateShipping(shipping);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @DeleteMapping("/admin/shipping")
    public ResponseEntity<?> deleteShipping(
            String code
    ) {
        ResponseDTO<?> responseDTO = shippingService.deleteShipping(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
