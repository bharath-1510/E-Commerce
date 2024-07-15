package com.ecommerce.app.service;

import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.dto.ShippingDTO;
import com.ecommerce.app.model.Provider;
import com.ecommerce.app.model.Region;
import com.ecommerce.app.model.ShippingOption;
import com.ecommerce.app.repository.ProviderRepo;
import com.ecommerce.app.repository.RegionRepo;
import com.ecommerce.app.repository.ShippingOptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingService {
    @Autowired
    ProviderRepo providerRepo;
    @Autowired
    RegionRepo regionRepo;
    @Autowired
    ShippingOptionRepo shippingRepo;

    public ResponseDTO<?> getShippingByCode(String code) {
        try {
            ShippingOption shipping = shippingRepo.findByCode(code).orElse(null);
            if (shipping == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Shipping Not Found", null);
            ShippingDTO shippingFound = new ShippingDTO();
            shippingFound.setId(shipping.getId());
            shippingFound.setCode(shipping.getCode());
            shippingFound.setAmount(shipping.getAmount());
            shippingFound.setName(shipping.getName());
            shippingFound.setRegionCode(shipping.getRegion().getCode());
            shippingFound.setProviderCode(shipping.getProvider().getCode());
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "Shipping Found", shippingFound);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> getAllShipping() {
        try {
            List<ShippingOption> shipping = shippingRepo.findAll();
            if (shipping.isEmpty())
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Shipping Not Found", null);
            List<ShippingDTO> shippingDTOS = getShippingDTOS(shipping);
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "Shipping Found", shippingDTOS);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    private static List<ShippingDTO> getShippingDTOS(List<ShippingOption> shipping) {
        List<ShippingDTO> shippingDTOS = new ArrayList<>();
        for (ShippingOption ship : shipping) {
            ShippingDTO dto = new ShippingDTO();
            dto.setId(ship.getId());
            dto.setCode(ship.getCode());
            dto.setAmount(ship.getAmount());
            dto.setName(ship.getName());
            dto.setRegionCode(ship.getRegion().getCode());
            dto.setProviderCode(ship.getProvider().getCode());
            shippingDTOS.add(dto);
        }
        return shippingDTOS;
    }

    public ResponseDTO<?> createShipping(ShippingDTO shipping) {
        try {
            ShippingOption shippingFound = shippingRepo.findByCode(shipping.getCode()).orElse(new ShippingOption());
            Region region =  regionRepo.findByCode(shipping.getRegionCode()).orElse(null);
            Provider provider = providerRepo.findByCode(shipping.getProviderCode()).orElse(null);
            if (region == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Region not Found", null);
            if (provider == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Provider not Found", null);
            if (shippingFound.getId() != null)
                return new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE, "Shipping Already created", null);
            shippingFound.setCreatedAt(LocalDateTime.now());
            shippingFound.setCode(shipping.getCode());
            shippingFound.setName(shipping.getName());
            shippingFound.setAmount(shipping.getAmount());
            shippingFound.setRegion(region);
            shippingFound.setProvider(provider);
            shippingFound = shippingRepo.save(shippingFound);
            shipping.setId(shippingFound.getId());
            return new ResponseDTO<>(HttpStatus.CREATED, "Shipping Created", shipping);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }

    public ResponseDTO<?> updateShipping(ShippingDTO shipping) {
        try {
            ShippingOption shippingFound = shippingRepo.findByCode(shipping.getCode()).orElse(null);
            Region region =  regionRepo.findByCode(shipping.getRegionCode()).orElse(null);
            Provider provider = providerRepo.findByCode(shipping.getProviderCode()).orElse(null);
            if (region == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Region not Found", null);
            if (provider == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Provider not Found", null);
            if (shippingFound == null)
                return new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE, "Shipping not Found", null);
            shippingFound.setUpdatedAt(LocalDateTime.now());
            shippingFound.setName(shipping.getName());
            shippingFound.setAmount(shipping.getAmount());
            shippingFound.setRegion(region);
            shippingFound.setProvider(provider);
            shippingRepo.save(shippingFound);
            shipping.setId(shippingFound.getId());
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "Shipping Updated", shipping);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> deleteShipping(String code) {
        try {
            ShippingOption shipping = shippingRepo.findByCode(code).orElse(null);
            if (shipping == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Shipping Not Found", null);
            shippingRepo.delete(shipping);
            return new ResponseDTO<>(HttpStatus.ACCEPTED, "Shipping Deleted", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }
}
