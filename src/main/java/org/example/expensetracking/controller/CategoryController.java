package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.CategoryRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.CategoryResponse;
import org.example.expensetracking.service.CategoryService;
import org.example.expensetracking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAllCategories(@Positive
                                                  @RequestParam(defaultValue = "1")
                                                  Integer page, @Positive @RequestParam(defaultValue = "2") Integer size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        User user1 = userService.findUserById(user.getUserId());
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

    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        UUID currentUserId = user.getUserId();

        Category category = categoryService.addCategory(categoryRequest, currentUserId);

        ApiResponse<?> response = ApiResponse.builder()
                .message("You Created category successfully")
                .payload(category)
                .status(HttpStatus.CREATED)
                .instance("/api/v1/categories")
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(response);
    }


   /* @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        User user1 = userService.findUserById(user.getUserId());
        CategoryResponse categoryResponse = categoryService.getCategoryById(user.getUserId(),categoryId);
        return ResponseEntity.ok(new ApiResponse<>(
                "about:blank",
                "You get all categories successfully",
                HttpStatus.OK,
                HttpStatus.OK.value(),
                "/api/v1/categories",
                new Date(),
                null,
                categoryResponse
        ));

    }*/


}
