package org.example.expensetracking.service;

import org.example.expensetracking.model.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryResponse> getAllCategories(UUID userId, Integer page, Integer size);
    CategoryResponse getCategoryById(UUID userId, UUID categoryId);

    void deleteCategoryById(UUID userId, UUID categoryId);
}
