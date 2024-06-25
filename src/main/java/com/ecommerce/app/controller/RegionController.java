package com.ecommerce.app.controller;

import com.ecommerce.app.dto.DiscountDTO;
import com.ecommerce.app.dto.RegionDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegionController {
    @Autowired
    RegionService regionService;
    @GetMapping("/admin/region")
    public ResponseEntity<?> getRegionByCode(
            String code
    ) {
        ResponseDTO<?> responseDTO = regionService.getRegionByCode(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @GetMapping("/admin/regions")
    public ResponseEntity<?> getAllRegion(

    ) {
        ResponseDTO<?> responseDTO = regionService.getAllRegion();
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PostMapping("/admin/region")
    public ResponseEntity<?> createRegion(
            @RequestBody RegionDTO region
    ) {
        ResponseDTO<?> responseDTO = regionService.createRegion(region);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @PutMapping("/admin/region")
    public ResponseEntity<?> updateRegion(
            @RequestBody RegionDTO region
    ) {
        ResponseDTO<?> responseDTO = regionService.updateRegion(region);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
    @DeleteMapping("/admin/region")
    public ResponseEntity<?> deleteRegion(
            String code
    ) {
        ResponseDTO<?> responseDTO = regionService.deleteRegion(code);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}
