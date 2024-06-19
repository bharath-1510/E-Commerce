package com.ecommerce.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Role role;

}