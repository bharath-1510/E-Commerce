package com.ecommerce.app.service;

import com.ecommerce.app.dto.AddressDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.model.Address;
import com.ecommerce.app.model.Discount;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.AddressRepo;
import com.ecommerce.app.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepo userRepository;

    public ResponseDTO<?> getAllAddress(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);

            List<Address> addresses = user != null ? user.getAddresses() : null;
            if (addresses != null ) {
                List<AddressDTO> addressList = new ArrayList<>();
                addresses.stream().forEach(
                        address -> {
                            AddressDTO dto = new AddressDTO();
                            dto.setCity(address.getCity());
                            dto.setStreet(address.getStreet());
                            dto.setCountry(address.getCountry());
                            dto.setPostalCode(address.getPostalCode());
                            dto.setPhoneNumber(address.getPhoneNumber());
                            addressList.add(dto);
                        }
                );
                return new ResponseDTO<>(HttpStatus.OK, "Addresses Found", addressList);
            }
            else
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Address not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }

    public ResponseDTO<?> addAddress(HttpServletRequest request, AddressDTO address) {
        try {

            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != (null)) {
                Address addressFound = addressRepo.findAll().stream().filter(add ->
                        add.getUser().getId().equals(user.getId())
                ).toList().stream().filter(add->add.getStreet().equals(add.getStreet())).findFirst().orElse(null);
                if(addressFound==null)
                {
                    Address newAddress = new Address();
                    newAddress.setCountry(address.getCountry());
                    newAddress.setCity(address.getCity());
                    newAddress.setStreet(address.getStreet());
                    newAddress.setPhoneNumber(address.getPhoneNumber());
                    newAddress.setPostalCode(address.getPostalCode());
                    newAddress.setUser(user);
                    addressRepo.save(newAddress);
                    return new ResponseDTO<>(HttpStatus.CREATED, "Address Created", null);
                }
                return new ResponseDTO<>(HttpStatus.CONFLICT, "Address Already Exists", null);
            } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> updateAddress(HttpServletRequest request, AddressDTO address) {
        try {

            String authHeader = request.getHeader("Authorization");
            String jwt = authHeader.substring(7);
            String userEmail = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user != (null)) {
                Address addressFound = addressRepo.findAll().stream().filter(add ->
                        add.getUser().getId().equals(user.getId())
                ).toList().stream().filter(add->add.getStreet().equals(add.getStreet())).findFirst().orElse(null);
                if(addressFound!=null)
                {
                    addressFound.setCountry(address.getCountry());
                    addressFound.setCity(address.getCity());
                    addressFound.setStreet(address.getStreet());
                    addressFound.setPhoneNumber(address.getPhoneNumber());
                    addressFound.setPostalCode(address.getPostalCode());
                    addressRepo.save(addressFound);
                    return new ResponseDTO<>(HttpStatus.CREATED, "Address Updated", addressFound);
                }
                return new ResponseDTO<>(HttpStatus.CONFLICT, "Address Not Found", null);
            } else return new ResponseDTO<>(HttpStatus.NOT_FOUND, "User Not Found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }

    public ResponseDTO<?> deleteAddress(Long id) {
        try {
            Address address = addressRepo.findById(id).orElse(null);
            if (address == null)
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Discount Not Found", null);
            else {
                addressRepo.delete(address);
                return new ResponseDTO<>(HttpStatus.OK, "Discount Deleted", null);
            }
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }
    }
}
