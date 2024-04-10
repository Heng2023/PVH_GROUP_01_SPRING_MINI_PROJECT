package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.repository.CategoryRepository;
import org.example.expensetracking.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
