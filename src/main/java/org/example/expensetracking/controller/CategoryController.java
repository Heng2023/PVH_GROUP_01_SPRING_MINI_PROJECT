package org.example.expensetracking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }
    //Get All Category
    @GetMapping
    public ResponseEntity<?> getAllCategories(@Positive @RequestParam(defaultValue = "1") Integer page, @Positive @RequestParam(defaultValue = "2") Integer size) {
        List<Category> categoryList = categoryService.getAllCategories(page, size);
        return ResponseEntity.ok(new ApiResponse<>(
                "about:blank",
                "You get all categories successfully",
                HttpStatus.OK,
                HttpStatus.OK.value(),
                "/api/v1/categories?page=" + page + "&size=" + size,
                new Date(),
                null,
                categoryList
        ));

    }

    //Insert Category
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> insertCategory(@Valid @RequestBody CategoryRequest categoryRequest ) {
        Category category = (Category) (Category) categoryService.insertCategory(categoryRequest);
        ApiResponse<Category> apiResponse = new ApiResponse<>(
                "about:blank",
                "You insert categories successfully",
                HttpStatus.OK,
                HttpStatus.OK.value(),
                "/api/v1/categories?page",
                new Date(),
                null,
                category
        );
        return ResponseEntity.ok(apiResponse);
    }
}
