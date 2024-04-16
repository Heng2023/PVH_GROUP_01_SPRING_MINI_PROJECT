package org.example.expensetracking.controller;

import jakarta.validation.Valid;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.addCategory(categoryRequest);

        ApiResponse<?> response = ApiResponse.builder()
                .message("Category have been successfully created.")
                .payload(category)
                .status(HttpStatus.CREATED)
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(response);
    }
}
