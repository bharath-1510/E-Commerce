package com.ecommerce.app.service;


import com.ecommerce.app.dto.AuthenticationResponseDTO;
import com.ecommerce.app.dto.ResponseDTO;
import com.ecommerce.app.dto.SigninRequest;
import com.ecommerce.app.dto.SignupRequest;
import com.ecommerce.app.model.Token;
import com.ecommerce.app.model.User;
import com.ecommerce.app.repository.TokenRepo;
import com.ecommerce.app.repository.UserRepo;
import com.ecommerce.app.utility.UtilityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    @Autowired
    private TokenRepo tokenRepository;

    @Transactional
    public ResponseDTO<?> signup(SignupRequest request) {
        try {
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
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }


    public ResponseDTO<?> signin(
            SigninRequest request
    ) {
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (user != null) {
                if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    var jwtToken = jwtService.generateToken(user);
                    var refreshToken = jwtService.generateRefreshToken(user);
                    saveUserToken(user, jwtToken);
                    AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .build();
                    return new ResponseDTO<>(HttpStatus.CREATED, "Token Generated", response);
                } else
                    return new ResponseDTO<>(HttpStatus.UNAUTHORIZED, "Check the Credentials", null);


            } else
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Account not found", null);
        } catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public ResponseDTO<?> refresh(HttpServletRequest request) {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String refreshToken;
            final String userEmail;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Token not found", null);
            }
            refreshToken = authHeader.substring(7);
            userEmail = jwtService.extractUsername(refreshToken);
            if (userEmail != null) {
                var user = userRepository.findByEmail(userEmail)
                        .orElse(null);
                if (user != null) {
                    if (jwtService.isTokenValid(refreshToken, user)) {
                        var accessToken = jwtService.generateToken(user);
                        revokeAllUserTokens(user);
                        saveUserToken(user, accessToken);
                        refreshToken = jwtService.generateRefreshToken(user);
                        AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build();
                        return new ResponseDTO<>(HttpStatus.CREATED, "Token Refreshed", response);
                    } else
                        return new ResponseDTO<>(HttpStatus.FORBIDDEN, "Token Expired", null);
                } else
                    return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Account not found", null);

            } else
                return new ResponseDTO<>(HttpStatus.NOT_FOUND, "Account not found", null);
        }
        catch (Exception ex) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        }

    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}