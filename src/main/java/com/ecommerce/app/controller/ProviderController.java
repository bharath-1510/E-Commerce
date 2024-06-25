package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ProviderDTO;
import com.ecommerce.app.dto.RegionDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ProviderController {
    @Autowired
    ProviderService providerService;
    @GetMapping("/admin/provider")
    public ResponseEntity<?> getProviderByCode(
            String code
    ) {
        ResponseDTO<?> responseDTO = providerService.getProviderByCode(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @GetMapping("/admin/providers")
    public ResponseEntity<?> getAllProvider(

    ) {
        ResponseDTO<?> responseDTO = providerService.getAllProvider();
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PostMapping("/admin/provider")
    public ResponseEntity<?> createProvider(
            @RequestBody ProviderDTO provider
    ) {
        ResponseDTO<?> responseDTO = providerService.createProvider(provider);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PutMapping("/admin/provider")
    public ResponseEntity<?> updateProvider(
            @RequestBody ProviderDTO provider
    ) {
        ResponseDTO<?> responseDTO = providerService.updateProvider(provider);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @DeleteMapping("/admin/provider")
    public ResponseEntity<?> deleteProvider(
            String code
    ) {
        ResponseDTO<?> responseDTO = providerService.deleteProvider(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
