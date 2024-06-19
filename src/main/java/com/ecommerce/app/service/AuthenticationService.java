package com.ecommerce.app.service;


import com.ecommerce.app.dto.AuthenticationResponseDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.dto.SigninRequest;
import com.ecommerce.app.dto.SignupRequest;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.UserRepo;
import com.ecommerce.app.utility.UtilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    UtilityService utilityService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public ResponseDTO<?> signup(SignupRequest request) {
        Optional<User> userCheck = userRepository.findByEmail(request.getEmail());
        if (userCheck.isEmpty()) {
            if (utilityService.isValidEmail(request.getEmail())) {
                if (utilityService.isValidPassword(request.getPassword())) {
                    var user = User.builder()
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .role(request.getRole()).createdAt(LocalDateTime.now())
                            .build();
                    user = userRepository.save(user);
                    request.setId(user.getId());
                    return new ResponseDTO<>(HttpStatus.CREATED, "Account Created", request);
                } else
                    return new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE, "Password doesn't match the format", null);
            } else
                return new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE, "Email doesn't match the format", null);
        } else
            return new ResponseDTO<>(HttpStatus.CONFLICT, "Account Already Exists", null);

    }


    public ResponseDTO<?> signin(
            SigninRequest request
    ) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);

                AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build();
                return new ResponseDTO<>(HttpStatus.CREATED, "Token Regenerated", response);
            } else
                return new ResponseDTO<>(HttpStatus.UNAUTHORIZED, "Check the Credentials", null);


        } else
            return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Account not found", null);

    }


}