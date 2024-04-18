package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.expensetracking.exceptionhandler.BlankFieldException;
import org.example.expensetracking.exceptionhandler.CategoryNotFoundException;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.CategoryRequest;
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

import java.util.*;

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

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        // Check if the category name is blank
        if (categoryRequest.getName() == null || categoryRequest.getName().trim().isEmpty()) {
            throw new BlankFieldException("Category name can't be blank");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        UUID currentUserId = user.getUserId();

        CategoryResponse category = categoryService.addCategory(categoryRequest, currentUserId);
        System.out.println(category);
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>builder()
                .type("about:blank")
                .message("You created the category successfully")
                .payload(category)
                .status(HttpStatus.CREATED)
                .code(201)
                .instance("/api/v1/categories")
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable String id) {
        // Manually validate the UUID
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            // Return a custom error response if the UUID is invalid
            return ResponseEntity.badRequest().body(new ApiErrorResponse(
                    "about:blank",
                    "Invalid UUID format",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/categories/" + id,
                    new Date(),
                    Collections.singletonMap("error", "Invalid UUID format")
            ));
        }

        // Check if the category name is blank
        if (categoryRequest.getName() == null || categoryRequest.getName().trim().isEmpty()) {
            throw new BlankFieldException("Category name can't be blank");
        }

        try {
            CategoryResponse category = categoryService.updateCategory(uuid, categoryRequest);

            ApiResponse<?> response = ApiResponse.builder()
                    .type("about:blank")
                    .message("Updated category successfully")
                    .payload(category)
                    .status(HttpStatus.OK)
                    .code(200)
                    .instance("/api/v1/categories/" + uuid)
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.ok(response);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(
                    "about:blank",
                    "Category Not Found",
                    HttpStatus.NOT_FOUND,
                    404,
                    "/api/v1/categories/" + uuid,
                    new Date(),
                    Collections.singletonMap("error", "Category not found")
            ));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "2") Integer size) {
        // Check if page and size are blank
        if (page == null || size == null) {
            return ResponseEntity.badRequest().body(new ApiErrorResponse(
                    "about:blank",
                    "Invalid Parameters",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/categories",
                    new Date(),
                    Collections.singletonMap("error", "Page and Size must not be blank")
            ));
        }

        if (page <= 0 || size <= 0) {
            // Invalid parameter values
            return ResponseEntity.badRequest().body(new ApiErrorResponse(
                    "about:blank",
                    "Invalid Parameters",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/categories",
                    new Date(),
                    Collections.singletonMap("error", "Page and Size must be greater than zero")
            ));
        }

        // Validate if page and size are within integer bounds
        if (page > Integer.MAX_VALUE || size > Integer.MAX_VALUE) {
            return ResponseEntity.badRequest().body(new ApiErrorResponse(
                    "about:blank",
                    "Invalid Parameters",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/categories",
                    new Date(),
                    Collections.singletonMap("error", "Page and Size must be within Integer bounds")
            ));
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findUserByEmail(email);
            List<CategoryResponse> categoryList = categoryService.getAllCategories(user.getUserId(), page, size);

            if (categoryList.isEmpty()) {
                // No categories found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(
                        "about:blank",
                        "No categories found",
                        HttpStatus.NOT_FOUND,
                        404,
                        "/api/v1/categories",
                        new Date(),
                        Collections.singletonMap("error", "No categories found")
                ));
            }

            return ResponseEntity.ok(new ApiResponse<>(
                    "about:blank",
                    "Get all categories successfully",
                    HttpStatus.OK,
                    HttpStatus.OK.value(),
                    "/api/v1/categories?page=" + page + "&size=" + size,
                    new Date(),
                    null,
                    categoryList
            ));
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
    public ResponseEntity<?> getCategoryById(@Valid @PathVariable String categoryId) {
        // Manually validate the UUID
        UUID uuid;
        try {
            uuid = UUID.fromString(categoryId);
        } catch (IllegalArgumentException e) {
            // Return a custom error response if the UUID is invalid
            return ResponseEntity.badRequest().body(new ApiErrorResponse(
                    "about:blank",
                    "Invalid UUID format",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/categories/" + categoryId,
                    new Date(),
                    Collections.singletonMap("error", "Invalid UUID format")
            ));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        try {
            CategoryResponse categoryResponse = categoryService.getCategoryById(user.getUserId(), uuid);
            return ResponseEntity.ok(new ApiResponse<>(
                    "about:blank",
                    "Category get successfully",
                    HttpStatus.OK,
                    HttpStatus.OK.value(),
                    "/api/v1/categories/" + uuid,
                    new Date(),
                    null,
                    categoryResponse
            ));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(
                    "about:blank",
                    "Category Not Found",
                    HttpStatus.NOT_FOUND,
                    404,
                    "/api/v1/categories/" + uuid,
                    new Date(),
                    Collections.singletonMap("error", "Category not found")
            ));
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/categories/" + uuid,
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@Valid @PathVariable String categoryId) {
        // Manually validate the UUID
        UUID uuid;
        try {
            uuid = UUID.fromString(categoryId);
        } catch (IllegalArgumentException e) {
            // Return a custom error response if the UUID is invalid
            return ResponseEntity.badRequest().body(new ApiErrorResponse(
                    "about:blank",
                    "Invalid UUID format",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/categories/" + categoryId,
                    new Date(),
                    Collections.singletonMap("error", "Invalid UUID format")
            ));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        try {
            categoryService.deleteCategoryById(user.getUserId(), uuid);
            return ResponseEntity.ok(new ApiResponse<>(
                    "about:blank",
                    "Category delete successfully",
                    HttpStatus.OK,
                    HttpStatus.OK.value(),
                    "/api/v1/categories/" + uuid,
                    new Date(),
                    null,
                    null
            ));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(
                    "about:blank",
                    "Category Not Found",
                    HttpStatus.NOT_FOUND,
                    404,
                    "/api/v1/categories/" + uuid,
                    new Date(),
                    Collections.singletonMap("error", "Category not found")
            ));
        }
    }
}
