package com.ecommerce.app.dto;

import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String street;
    private String phoneNumber;
    private String city;
    private String postalCode;
    private String country;
}
