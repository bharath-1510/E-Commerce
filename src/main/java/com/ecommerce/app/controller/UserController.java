package com.ecommerce.app.controller;

import com.ecommerce.app.dto.RegisterRequestDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(
            @RequestBody RegisterRequestDTO request
    ) {
        return ResponseEntity.ok(new ResponseDTO(ResponseDTO.ResponseStatus.SUCCESS,"",service.register(request)));
    }

    @PostMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

}
