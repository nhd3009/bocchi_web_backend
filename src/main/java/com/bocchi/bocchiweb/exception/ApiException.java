package com.bocchi.bocchiweb.exception;

import com.bocchi.bocchiweb.util.error.BaseErrorCode;

public class ApiException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public ApiException(BaseErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ApiException(BaseErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public BaseErrorCode getErrorCode() {
        return errorCode;
    }
}
