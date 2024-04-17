package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.AppUserDTO;
import org.example.expensetracking.model.dto.response.CategoryResponse;
import org.example.expensetracking.service.CategoryService;
import org.example.expensetracking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("asdgaysgdiagdiuagd"+auth.toString());
        String email = auth.getName();
        System.out.println("asdasdasdads"+email);
        User user = userService.findUserByEmail(email);
        AppUserDTO user1 = userService.findUserById(user.getUserId());
        System.out.println("User:"+user1);
        System.out.println("adssasadadqw"+user.toString());
        List<CategoryResponse> categoryList = categoryService.getAllCategories(user.getUserId(),page, size);
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
}
