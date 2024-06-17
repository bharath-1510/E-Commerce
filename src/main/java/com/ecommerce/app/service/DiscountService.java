package com.ecommerce.app.service;

import com.ecommerce.app.dto.DiscountDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Discount;
import com.ecommerce.app.repository.DiscountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    DiscountRepo discountRepository;

    public ResponseDTO<?> getDiscountByCode(String code) {
        Optional<Discount> discount = discountRepository.findByCode(code);
        if (discount.isPresent()) {
            DiscountDTO dto = new DiscountDTO();
            Discount discount1 = discount.get();
            dto.setCode(discount1.getCode());
            dto.setDiscountType(discount1.getDiscountType());
            dto.setId(discount1.getId());
            dto.setValue(discount1.getValue());
            dto.setDescription(discount1.getDescription());
            return new ResponseDTO<>(HttpStatus.OK, "", dto);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Discount Not Found", null);
    }

    public ResponseDTO<?> getAllDiscount() {
        List<Discount> discounts = discountRepository.findAll();
        if (!discounts.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.OK, "", discounts);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Create Discount First", null);
    }

    public ResponseDTO<?> createDiscount(DiscountDTO dto) {
        Discount discount = discountRepository.findByCode(dto.getCode()).orElse(new Discount());
        if (discount.getId() == null) {
            discount.setDiscountType(dto.getDiscountType());
            discount.setCode(dto.getCode());
            discount.setValue(dto.getValue());
            discount.setDescription(dto.getDescription());
            discount.setExpiresAt(LocalDateTime.now().plusDays(45));
            discount.setUsageLimit(dto.getUsageLimit());
            discount = discountRepository.save(discount);
            dto.setId(discount.getId());
            return new ResponseDTO<>(HttpStatus.CREATED, "Discount Created", dto);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Discount Already Exists", null);
    }

    public ResponseDTO<?> updateDiscount(DiscountDTO dto) {
        Discount discount = discountRepository.findByCode(dto.getCode()).orElse(new Discount());
        if (discount.getId() != null) {
            discount.setDiscountType(dto.getDiscountType());
            discount.setValue(dto.getValue());
            discount.setDescription(dto.getDescription());
            discount.setExpiresAt(LocalDateTime.now().plusDays(45));
            discount.setUsageLimit(dto.getUsageLimit());
            discount = discountRepository.save(discount);
            dto.setId(discount.getId());
            return new ResponseDTO<>(HttpStatus.CREATED, "Discount Updated", dto);
        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Discount Not Created", null);
    }

    public ResponseDTO<?> deleteDiscount(String code) {
        Discount discount = discountRepository.findByCode(code).orElse(null);
        if (discount == null)
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Discount Not Found", null);
        else {
            discountRepository.delete(discount);
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Discount Deleted", null);
        }

    }
}
