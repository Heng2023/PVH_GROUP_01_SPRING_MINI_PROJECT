package org.example.expensetracking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        AppUserDTO user1 = userService.findUserById(user.getUserId());
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        AppUserDTO user1 = userService.findUserById(user.getUserId());
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

    }


}
