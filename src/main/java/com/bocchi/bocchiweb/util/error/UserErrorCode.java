package com.bocchi.bocchiweb.util.error;

import lombok.Getter;

@Getter
public enum UserErrorCode implements BaseErrorCode {
    // 4xx: Client Errors
    USER_NOT_FOUND("User not found", 404),
    EMAIL_ALREADY_EXISTS("Email already exists", 400),
    DEFAULT_ROLE_NOT_FOUND("Default role not found", 404),
    INVALID_INPUT("Invalid input data", 400),
    UNAUTHORIZED("Unauthorized access", 401),
    FORBIDDEN("You do not have permission", 403),

    // 5xx: Server Errors
    INTERNAL_SERVER_ERROR("Something went wrong", 500);

    private final String defaultMessage;
    private final int status;

    UserErrorCode(String defaultMessage, int status) {
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
}
