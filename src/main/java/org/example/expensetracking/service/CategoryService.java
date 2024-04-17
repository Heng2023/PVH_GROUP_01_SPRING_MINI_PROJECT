package org.example.expensetracking.service;

import org.example.expensetracking.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories(Integer page, Integer size);
}
