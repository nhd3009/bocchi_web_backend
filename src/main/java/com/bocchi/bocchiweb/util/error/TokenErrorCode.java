package com.bocchi.bocchiweb.util.error;

import lombok.Getter;

@Getter
public enum TokenErrorCode implements BaseErrorCode {
    UNAUTHORIZED("Unauthorized access", 401),
    INVALID_TOKEN("Invalid Token", 401),
    MISSING_TOKEN("Refresh token does not match stored token", 401),
    // 5xx: Server Errors
    INTERNAL_SERVER_ERROR("Something went wrong", 500);

    private final String defaultMessage;
    private final int status;

    TokenErrorCode(String defaultMessage, int status) {
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
}
