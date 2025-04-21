package com.bocchi.bocchiweb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bocchi.bocchiweb.dto.category.CategoryDTO;
import com.bocchi.bocchiweb.entity.Category;
import com.bocchi.bocchiweb.exception.ApiException;
import com.bocchi.bocchiweb.repository.CategoryRepository;
import com.bocchi.bocchiweb.util.error.CategoryErrorCode;
import com.bocchi.bocchiweb.util.mapper.CategoryMapper;
import com.bocchi.bocchiweb.util.request.category.CategoryFilterRequest;
import com.bocchi.bocchiweb.util.request.category.CreateCategoryRequest;
import com.bocchi.bocchiweb.util.request.category.UpdateCategoryRequest;
import com.bocchi.bocchiweb.util.response.PageResponse;
import com.bocchi.bocchiweb.util.specification.CategorySpecification;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public PageResponse<CategoryDTO> getAllCategory(CategoryFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("name"));
        Specification<Category> specification = CategorySpecification.filterByName(request.getName());

        Page<Category> page = categoryRepository.findAll(specification, pageable);
        List<CategoryDTO> data = page.getContent().stream().map(CategoryMapper::toDTO).toList();
        return new PageResponse<>(data, page.getNumber(), page.getSize(), page.getTotalPages(),
                page.getTotalElements());
    }

    public CategoryDTO updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        if (!category.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByName(request.getName())) {
            throw new ApiException(CategoryErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        category.setName(request.getName());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toDTO(updatedCategory);
    }

    public CategoryDTO createCategory(CreateCategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ApiException(CategoryErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = new Category();
        category.setName(categoryRequest.getName());

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDTO(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ApiException(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }
}
