package com.bocchi.bocchiweb.util.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterRequest {
    private String username;
    private int page = 0;
    private int size = 10;
}
