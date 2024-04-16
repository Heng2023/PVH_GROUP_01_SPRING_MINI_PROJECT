package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.repository.CategoryRepository;
import org.example.expensetracking.repository.UserRepository;
import org.example.expensetracking.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }


    @Override
    public Category addCategory(CategoryRequest categoryRequest) {
        Category category = mapper.map(categoryRequest, Category.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        category.setUser(currentUser);
        System.out.println("Current User: " + currentUser);

        return categoryRepository.insertCategory(categoryRequest);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Integer id, CategoryRequest categoryRequest) {
        return categoryRepository.updateCategory(id, categoryRequest);
    }

    @Override
    public void deleteCategory(Integer id) {

    }

    @Override
    public Category getCategoryById(Integer id) {
        return null;
    }
}
