package org.example.expensetracking.service;

import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.model.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    Category addCategory(CategoryRequest categoryRequest, UUID userId);
    List<CategoryResponse> getAllCategories(UUID userId, Integer page, Integer pageSize);
    Category updateCategory(UUID id, CategoryRequest categoryRequest);

    Category findCategoryById(UUID categoryId);

    void deleteCategory(UUID id);
    Category getCategoryById(UUID userId, UUID categoryId);
    Category findCategoryById(UUID userId, UUID categoryId);
}
