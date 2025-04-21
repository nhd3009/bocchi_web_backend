package com.bocchi.bocchiweb.util.error;

public interface BaseErrorCode {
    String getDefaultMessage();

    int getStatus();

    String name();
}
