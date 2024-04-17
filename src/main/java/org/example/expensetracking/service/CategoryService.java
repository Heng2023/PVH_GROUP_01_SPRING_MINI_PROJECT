package org.example.expensetracking.service;

import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.model.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    Category addCategory(CategoryRequest categoryRequest);
    List<CategoryResponse> getAllCategories(UUID userId);
    Category updateCategory(Integer id, CategoryRequest categoryRequest);
    void deleteCategory(Integer id);
    Category getCategoryById(Integer id);
}
