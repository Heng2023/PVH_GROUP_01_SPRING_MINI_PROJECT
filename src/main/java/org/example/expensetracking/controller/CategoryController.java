package org.example.expensetracking.controller;

import jakarta.validation.constraints.Positive;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }
    @GetMapping
    public ResponseEntity<?> getAllCategories(@Positive @RequestParam(defaultValue = "1") Integer page,@Positive @RequestParam(defaultValue = "2")Integer size){
        List<Category> categoryList = categoryService.getAllCategories(page,size);
        return ResponseEntity.ok(new ApiResponse<>(
                "about:blank",
                "You get all categories successfully",
                HttpStatus.OK.value(),
                "/api/v1/category/",
                new Date(),
                null,
                categoryList

        ));
    }
}
