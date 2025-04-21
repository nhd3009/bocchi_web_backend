package com.bocchi.bocchiweb.util.error;

import lombok.Getter;

@Getter
public enum ProductErrorCode implements BaseErrorCode {
    PRODUCT_ALREADY_EXISTS("Product already exists", 400),
    PRODUCT_NOT_FOUND("Product not found", 404),
    INTERNAL_SERVER_ERROR("Something went wrong", 500);

    private final String defaultMessage;
    private final int status;

    ProductErrorCode(String defaultMessage, int status) {
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
}
