package com.bocchi.bocchiweb.util.request.cart;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateListCartItemsRequest {
    private List<UpdateCartItemRequest> items;
}
