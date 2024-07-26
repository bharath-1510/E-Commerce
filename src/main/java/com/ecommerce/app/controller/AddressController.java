package com.ecommerce.app.controller;

import com.ecommerce.app.dto.AddressDTO;
import com.ecommerce.app.dto.DiscountDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping("/address")
    public ResponseEntity<?> getAllAddress(
            HttpServletRequest request
    ) {
        ResponseDTO<?> responseDTO = addressService.getAllAddress(request);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/address")
    public ResponseEntity<?> addAddress(
            HttpServletRequest request,  @RequestBody AddressDTO address
    ) {
        ResponseDTO<?> responseDTO = addressService.addAddress(request,address);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PutMapping("/address")
    public ResponseEntity<?> updateAddress(
            HttpServletRequest request,@RequestBody AddressDTO address
    ) {
        ResponseDTO<?> responseDTO = addressService.updateAddress(request,address);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @DeleteMapping("/address")
    public ResponseEntity<?> deleteAddress(
            Long addressId
    ) {
        ResponseDTO<?> responseDTO = addressService.deleteAddress(addressId);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
