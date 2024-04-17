package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.model.Category;
import org.example.expensetracking.repository.CategoryRepository;
import org.example.expensetracking.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories(Integer page, Integer size) {
        return categoryRepository.getAllCategories(page,size);
    }
}
