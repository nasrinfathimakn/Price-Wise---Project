package com.pricewise.service;

import com.pricewise.dto.AuthResponseDTO;
import com.pricewise.dto.LoginRequestDTO;
import com.pricewise.dto.RegisterRequestDTO;
import com.pricewise.dto.UserResponseDTO;
import com.pricewise.entity.User;
import com.pricewise.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserService userService,
            JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail().toLowerCase().trim(),
                        loginRequest.getPassword()
                )
        );

        String token = tokenProvider.generateToken(authentication);
        UserResponseDTO userResponse = userService.getUserProfile(loginRequest.getEmail().toLowerCase().trim());

        return new AuthResponseDTO(token, userResponse);
    }

    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        User user = userService.registerUser(registerRequest);
        String token = tokenProvider.generateTokenFromEmail(user.getEmail());

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getEnabled(),
                user.getCreatedAt()
        );

        return new AuthResponseDTO(token, userResponse);
    }
}

