package com.bocchi.bocchiweb.util.mapper;

import com.bocchi.bocchiweb.dto.category.CategoryDTO;
import com.bocchi.bocchiweb.entity.Category;

public class CategoryMapper {
    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName());
    }

    public static Category toCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }
}
