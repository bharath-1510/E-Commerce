package com.ecommerce.app.controller;

import com.ecommerce.app.dto.SiginRequest;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.dto.SignupRequest;
import com.ecommerce.app.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
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
public class AuthController {

    @Autowired
    private AuthenticationService service;
    @PostMapping("/signin")
    public ResponseEntity<ResponseDTO> signin(
            @RequestBody SiginRequest request
    ) {
        ResponseDTO responseDTO = service.signin(request);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(
            SignupRequest request
    ) throws IOException {
        ResponseDTO responseDTO = service.signup(request);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

}
