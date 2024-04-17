package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import org.example.expensetracking.exceptionhandler.CategoryNotFoundException;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.response.ApiErrorResponse;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.CategoryResponse;
import org.example.expensetracking.service.CategoryService;
import org.example.expensetracking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories(@Positive @RequestParam(defaultValue = "1") Integer page, @Positive @RequestParam(defaultValue = "2") Integer size) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findUserByEmail(email);
            List<CategoryResponse> categoryList = categoryService.getAllCategories(user.getUserId(), page, size);
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
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/categories",
                    new Date(),
                    Collections.singletonMap("error", e.getMessage() != null ? e.getMessage() : "Unknown error occurred")
            ));
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        try {
            CategoryResponse categoryResponse = categoryService.getCategoryById(user.getUserId(), categoryId);
            return ResponseEntity.ok(new ApiResponse<>(
                    "about:blank",
                    "Category get successfully",
                    HttpStatus.OK,
                    HttpStatus.OK.value(),
                    "/api/v1/categories/" + categoryId,
                    new Date(),
                    null,
                    categoryResponse
            ));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable UUID categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        try {
            categoryService.deleteCategoryById(user.getUserId(), categoryId);
            return ResponseEntity.ok(new ApiResponse<>(
                    "about:blank",
                    "Category delete successfully",
                    HttpStatus.OK,
                    HttpStatus.OK.value(),
                    "/api/v1/categories/" + categoryId,
                    new Date(),
                    null,
                    null
            ));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
