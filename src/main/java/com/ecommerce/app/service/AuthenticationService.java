package com.ecommerce.app.service;


import com.ecommerce.app.dto.AuthenticationResponseDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.dto.SigninRequest;
import com.ecommerce.app.dto.SignupRequest;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    @Transactional
    public ResponseDTO<?> signup(SignupRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole()).createdAt(LocalDateTime.now())
                .build();
        user = userRepository.save(user);
        return new ResponseDTO<>(HttpStatus.CREATED, "", user);

    }


    public ResponseDTO<?> signin(
            SigninRequest request
    ) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
            return new ResponseDTO<>(HttpStatus.CREATED, "", response);

        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "No User Exists", null);

    }


}