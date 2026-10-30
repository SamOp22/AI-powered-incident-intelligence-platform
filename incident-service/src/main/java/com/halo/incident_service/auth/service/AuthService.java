package com.halo.incident_service.auth.service;

import com.halo.incident_service.auth.dto.*;
import com.halo.incident_service.config.JwtProperties;
import com.halo.incident_service.user.dto.UserResponse;
import com.halo.incident_service.user.entity.Role;
import com.halo.incident_service.user.entity.User;
import com.halo.incident_service.user.repository.UserRepository;
import com.halo.incident_service.user.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    public AuthService(UserService userService, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, TokenService tokenService,
                       JwtProperties jwtProperties) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.jwtProperties = jwtProperties;
    }

    public UserResponse register(RegisterRequest request) {
        // role is decided HERE, never by the client
        User user = userService.register(request.name(), request.email(),
                request.password(), Role.ENGINEER);
        return UserResponse.from(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(UserService.normalizeEmail(request.email()))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new LoginResponse(tokenService.issue(user), "Bearer",
                jwtProperties.expiry().toSeconds(), UserResponse.from(user));
    }
}