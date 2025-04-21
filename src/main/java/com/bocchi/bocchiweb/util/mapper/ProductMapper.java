package com.bocchi.bocchiweb.util.mapper;

import com.bocchi.bocchiweb.dto.product.ProductDTO;
import com.bocchi.bocchiweb.entity.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        if (product == null)
            return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImgUrl(product.getImgUrl());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        return dto;
    }
}
