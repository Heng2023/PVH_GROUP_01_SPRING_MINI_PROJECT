package org.example.expensetracking.service;

import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;

import java.util.List;

public interface CategoryService {

    Category addCategory(CategoryRequest categoryRequest);
    List<Category> getAllCategories();
    Category updateCategory(Integer id, CategoryRequest categoryRequest);
    void deleteCategory(Integer id);
    Category getCategoryById(Integer id);
}
