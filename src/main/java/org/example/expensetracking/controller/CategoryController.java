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

    //        return ResponseEntity.ok(new ApiResponse<>(
//                "about:blank",
//                "You get all categories successfully",
//                HttpStatus.OK,
//                HttpStatus.OK.value(),
//                "/api/v1/categories"+categoryId,
//                new Date(),
//                null,
//                categoryResponse
//        ));
//
//    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        AppUserDTO user1 = userService.findUserById(user.getUserId());
        CategoryResponse categoryResponse = categoryService.getCategoryById(user.getUserId(), categoryId);
        if (categoryResponse != null) {
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    ///Delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable UUID categoryId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findUserByEmail(email);
        AppUserDTO user1 = userService.findUserById(user.getUserId());

        CategoryResponse categoryResponse = categoryService.getCategoryById(user.getUserId(), categoryId);
        if (categoryResponse != null) {
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
        } else {
            return ResponseEntity.notFound().build();
        }


    }
}