package com.bocchi.bocchiweb.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import com.bocchi.bocchiweb.config.SecurityConfig;
import com.bocchi.bocchiweb.config.UserDetailsImpl;
import com.bocchi.bocchiweb.entity.User;
import com.bocchi.bocchiweb.exception.ApiException;
import com.bocchi.bocchiweb.repository.UserRepository;
import com.bocchi.bocchiweb.util.error.TokenErrorCode;
import com.bocchi.bocchiweb.util.error.UserErrorCode;
import com.bocchi.bocchiweb.util.mapper.UserMapper;
import com.bocchi.bocchiweb.util.response.TokenResponse;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiry;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiry;

    public AuthService(UserRepository userRepository,
            JwtEncoder jwtEncoder, JwtDecoder jwtDecoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.authenticationManager = authenticationManager;
    }

    public TokenResponse login(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        String accessToken = generateToken(user, accessTokenExpiry);
        String refreshToken = generateToken(user, refreshTokenExpiry);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponse(accessToken, refreshToken, UserMapper.toDto(user));
    }

    private String generateToken(User user, long expiryInSeconds) {
        Instant now = Instant.now();
        Instant expiry = now.plus(expiryInSeconds, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(user.getEmail())
                .claim("roles", List.of(user.getRole().getName()))
                .build();

        JwsHeader header = JwsHeader.with(SecurityConfig.JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        Jwt decodedToken;
        try {
            decodedToken = jwtDecoder.decode(refreshToken);
        } catch (JwtException e) {
            throw new ApiException(TokenErrorCode.INVALID_TOKEN);
        }

        String email = decodedToken.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new ApiException(TokenErrorCode.MISSING_TOKEN);
        }

        String newAccessToken = generateToken(user, accessTokenExpiry);
        return new TokenResponse(newAccessToken, null, UserMapper.toDto(user));
    }

    public void clearRefreshToken(String refreshToken) {
        Optional<User> userOptional = userRepository.findByRefreshToken(refreshToken);
        userOptional.ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
