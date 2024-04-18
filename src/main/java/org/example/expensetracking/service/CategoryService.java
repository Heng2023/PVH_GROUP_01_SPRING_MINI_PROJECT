package org.example.expensetracking.service;

import org.apache.coyote.BadRequestException;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.model.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryResponse> getAllCategories(UUID userId, Integer page, Integer size) throws BadRequestException;
    CategoryResponse getCategoryById(UUID userId, UUID categoryId);
    void deleteCategoryById(UUID userId, UUID categoryId);
    CategoryResponse addCategory(CategoryRequest categoryRequest, UUID userId);
    CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest);
}
