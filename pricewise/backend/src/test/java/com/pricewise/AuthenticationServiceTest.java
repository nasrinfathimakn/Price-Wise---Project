package com.pricewise;

import com.pricewise.dto.AuthResponseDTO;
import com.pricewise.dto.LoginRequestDTO;
import com.pricewise.dto.RegisterRequestDTO;
import com.pricewise.dto.UserResponseDTO;
import com.pricewise.entity.User;
import com.pricewise.enums.Role;
import com.pricewise.security.JwtTokenProvider;
import com.pricewise.service.AuthenticationService;
import com.pricewise.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider tokenProvider;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(authenticationManager, userService, tokenProvider);
    }

    @Test
    @DisplayName("User registration issues token and returns user profile")
    void testRegisterSuccess() {
        RegisterRequestDTO req = new RegisterRequestDTO("Alice Smith", "alice@example.com", "Password@123");
        User savedUser = new User(10L, "Alice Smith", "alice@example.com", "hashed_pass", Role.USER, true);

        when(userService.registerUser(req)).thenReturn(savedUser);
        when(tokenProvider.generateTokenFromEmail("alice@example.com")).thenReturn("mock-jwt-token-123");

        AuthResponseDTO response = authenticationService.register(req);

        assertNotNull(response);
        assertEquals("mock-jwt-token-123", response.getToken());
        assertEquals("alice@example.com", response.getUser().getEmail());
        assertEquals(Role.USER, response.getUser().getRole());
    }

    @Test
    @DisplayName("User login authenticates credentials and returns valid session token")
    void testLoginSuccess() {
        LoginRequestDTO req = new LoginRequestDTO("alice@example.com", "Password@123");
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("login-jwt-token");
        when(userService.getUserProfile("alice@example.com")).thenReturn(
                new UserResponseDTO(10L, "Alice Smith", "alice@example.com", Role.USER, true, OffsetDateTime.now())
        );

        AuthResponseDTO response = authenticationService.login(req);

        assertNotNull(response);
        assertEquals("login-jwt-token", response.getToken());
        assertEquals("Alice Smith", response.getUser().getName());
    }
}

