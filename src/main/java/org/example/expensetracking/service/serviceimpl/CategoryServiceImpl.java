package org.example.expensetracking.service.serviceimpl;

import org.apache.coyote.BadRequestException;
import org.example.expensetracking.exceptionhandler.BlankFieldException;
import org.example.expensetracking.exceptionhandler.CategoryNotFoundException;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.model.dto.response.CategoryResponse;
import org.example.expensetracking.repository.CategoryRepository;
import org.example.expensetracking.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryResponse> getAllCategories(UUID userId, Integer page, Integer size) throws BadRequestException {
        if (page == null || size == null) {
            throw new BlankFieldException("Page and Size must not be blank");
        }
        if (page <= 0 || size <= 0) {
            throw new BlankFieldException("Page and Size must be greater than zero");
        }
        // Check if page and size are within Integer bounds
        if (page > Integer.MAX_VALUE || size > Integer.MAX_VALUE) {
            throw new BadRequestException("Page and Size must be within Integer bounds");
        }

        List<Category> categoryList = categoryRepository.getAllCategories(userId, page, size);
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
            categoryResponses.add(categoryResponse);
        }
        return categoryResponses;
    }

    @Override
    public CategoryResponse getCategoryById(UUID userId, UUID categoryId) {
        Category category = categoryRepository.getCategoryById(categoryId, userId);
        if (category == null) {
            // If the category does not exist, throw a CategoryNotFoundException
            throw new CategoryNotFoundException("Category with UUID " + categoryId + " not found");
        }
        if (category != null) {
            return modelMapper.map(category, CategoryResponse.class);
        } else {

            return null;
        }
    }

    @Override
    public void deleteCategoryById(UUID userId, UUID categoryId) {
        Category category = categoryRepository.getCategoryById(categoryId, userId);
        if (category == null) {
            // If the category does not exist, throw a CategoryNotFoundException
            throw new CategoryNotFoundException("Category with UUID " + categoryId + " not found");
        }
        if (category != null) {
            categoryRepository.deleteCategoryById(categoryId, userId);
        }
    }

    @Override
    public CategoryResponse updateCategory(UUID id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.UpdateCategory(categoryRequest, id);
        if (category == null) {
            // If the category does not exist, throw a CategoryNotFoundException
            throw new CategoryNotFoundException("Category with UUID " + id + " not found");
        }
        // Map the updated category to CategoryResponse
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest, UUID userId) {
        Category category = categoryRepository.insertCategory(categoryRequest, userId);
        // Map the inserted category to CategoryResponse
        return modelMapper.map(category, CategoryResponse.class);
    }
}
