package com.bocchi.bocchiweb.controller;

import org.springframework.web.bind.annotation.RestController;

import com.bocchi.bocchiweb.dto.category.CategoryDTO;
import com.bocchi.bocchiweb.service.CategoryService;
import com.bocchi.bocchiweb.util.request.category.CategoryFilterRequest;
import com.bocchi.bocchiweb.util.request.category.CreateCategoryRequest;
import com.bocchi.bocchiweb.util.request.category.UpdateCategoryRequest;
import com.bocchi.bocchiweb.util.response.ApiMessage;
import com.bocchi.bocchiweb.util.response.ApiResponse;
import com.bocchi.bocchiweb.util.response.PageResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiMessage("Create Category")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody CreateCategoryRequest request) {
        CategoryDTO dto = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, null, "Create category successful!", dto));
    }

    @GetMapping("/public/categories")
    @ApiMessage("Get All Categories")
    public PageResponse<CategoryDTO> getAllCategory(CategoryFilterRequest request) {
        return categoryService.getAllCategory(request);
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiMessage("Update Category")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long id,
            @RequestBody UpdateCategoryRequest request) {
        CategoryDTO dto = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Updated category successful!", dto));
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiMessage("Delete Category")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Delete category successful!", null));
    }
}
