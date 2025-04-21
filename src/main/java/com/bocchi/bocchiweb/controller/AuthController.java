package com.bocchi.bocchiweb.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bocchi.bocchiweb.dto.user.UserDTO;
import com.bocchi.bocchiweb.exception.ApiException;
import com.bocchi.bocchiweb.service.AuthService;
import com.bocchi.bocchiweb.service.UserService;
import com.bocchi.bocchiweb.util.error.TokenErrorCode;
import com.bocchi.bocchiweb.util.request.login.LoginRequest;
import com.bocchi.bocchiweb.util.request.user.RegisterUserRequest;
import com.bocchi.bocchiweb.util.response.ApiMessage;
import com.bocchi.bocchiweb.util.response.ApiResponse;
import com.bocchi.bocchiweb.util.response.TokenResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register User")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserRequest request) {
        UserDTO response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, null, "Register User Successful!", response));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.login(request.getEmail(), request.getPassword());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        TokenResponse responseBody = new TokenResponse(
                tokenResponse.getAccessToken(),
                null,
                tokenResponse.getUser());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new ApiResponse<>(200, null, "Login Successful!", responseBody));
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new ApiException(TokenErrorCode.MISSING_TOKEN);
        }

        TokenResponse tokenResponse = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Access Token Refreshed!", tokenResponse));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response,
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        if (refreshToken != null) {
            authService.clearRefreshToken(refreshToken);
        }

        ResponseCookie deletedCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deletedCookie.toString());

        return ResponseEntity.ok(new ApiResponse<>(200, null, "Logout successful!", null));
    }
}
