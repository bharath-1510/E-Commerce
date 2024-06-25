package com.ecommerce.app.service;

import com.ecommerce.app.dto.RegionDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Region;
import com.ecommerce.app.repository.RegionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegionService {

    @Autowired
    RegionRepo regionRepo;

    public ResponseDTO<?> getRegionByCode(String code) {
        try {
            Region region = regionRepo.findByCode(code).orElse(null);
            if (region != null) {
                RegionDTO regionDTO = new RegionDTO();
                regionDTO.setCode(region.getCode());
                regionDTO.setId(region.getId());
                regionDTO.setCurrencyCode(region.getCurrencyCode());
                regionDTO.setName(region.getName());
                regionDTO.setTaxRate(region.getTaxRate());
                return new ResponseDTO<>(HttpStatus.OK, "Region Found", regionDTO);
            }
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Region Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> getAllRegion() {
        try {
            List<Region> regions = regionRepo.findAll();
            if (!regions.isEmpty())
                return new ResponseDTO<>(HttpStatus.OK, "Region Found", regions);
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Region Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> createRegion(RegionDTO region) {
        try {
            Region regionFound = regionRepo.findByCode(region.getCode()).orElse(new Region());
            if (regionFound.getId() == null) {
                regionFound.setCreatedAt(LocalDateTime.now());
                regionFound.setTaxRate(region.getTaxRate());
                regionFound.setName(region.getName());
                regionFound.setCurrencyCode(region.getCurrencyCode());
                regionFound.setCode(region.getCode());
                regionRepo.save(regionFound);
                region.setId(regionFound.getId());
                return new ResponseDTO<>(HttpStatus.CREATED, "Region Created", region);
            }
            return new ResponseDTO<>(HttpStatus.CONFLICT, "Region Already Exists", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> updateRegion(RegionDTO region) {
        try {
            Region regionFound = regionRepo.findByCode(region.getCode()).orElse(null);
            if (regionFound != null) {
                regionFound.setTaxRate(region.getTaxRate());
                regionFound.setName(region.getName());
                regionFound.setCurrencyCode(region.getCurrencyCode());
                regionFound.setUpdatedAt(LocalDateTime.now());
                regionRepo.save(regionFound);
                region.setId(regionFound.getId());
                return new ResponseDTO<>(HttpStatus.ACCEPTED, "Region Updated", region);
            }
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Region Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }

    public ResponseDTO<?> deleteRegion(String code) {
        try {
            Region region = regionRepo.findByCode(code).orElse(null);
            if (region == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Region Not Found", null);
            regionRepo.delete(region);
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "Region Deleted", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }
}
