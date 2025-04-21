package com.bocchi.bocchiweb.util.error;

import lombok.Getter;

@Getter
public enum CategoryErrorCode implements BaseErrorCode {
    CATEGORY_ALREADY_EXISTS("Category already exists", 400),
    CATEGORY_NOT_FOUND("Category not found", 404),
    INTERNAL_SERVER_ERROR("Something went wrong", 500);

    private final String defaultMessage;
    private final int status;

    CategoryErrorCode(String defaultMessage, int status) {
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
}
