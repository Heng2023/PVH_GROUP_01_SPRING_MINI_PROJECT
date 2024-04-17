package org.example.expensetracking.service;

import jakarta.validation.Valid;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories(Integer page, Integer size);

    List<Category> insertCategory(@Valid CategoryRequest categoryRequest);


}
