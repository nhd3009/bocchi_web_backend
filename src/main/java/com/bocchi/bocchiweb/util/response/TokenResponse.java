package com.bocchi.bocchiweb.util.response;

import com.bocchi.bocchiweb.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}
