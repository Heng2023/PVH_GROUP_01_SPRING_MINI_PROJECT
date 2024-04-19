package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.UserResponse;
import org.example.expensetracking.service.ExpenseService;
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
@RequestMapping("/api/v1/expenses")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.findUserByEmail(email);

            Expense expense = expenseService.insertExpense(expenseRequest, user.getUserId());
            System.out.println(expense);

            ApiResponse<?> response = ApiResponse
                    .builder()
                    .message("Successfully added expense")
                    .payload(expense)
                    .status(HttpStatus.CREATED)
                    .code(HttpStatus.CREATED.value())
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error adding expense: " + e.getMessage());
            ApiResponse<?> errorResponse = ApiResponse
                    .builder()
                    .message("Error adding expense: " + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllExpenses() {
        try {
            List<Expense> expenses = expenseService.findAllExpense();
            System.out.println(expenses);

            ApiResponse<?> response = ApiResponse
                    .builder()
                    .message("Successfully get expense")
                    .payload(expenses)
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception
            System.err.println("Error getting expenses: " + e.getMessage());
            ApiResponse<?> errorResponse = ApiResponse
                    .builder()
                    .message("Error getting expenses: " + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .timestamp(new Date())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable("id") UUID expenseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        UUID userId = user.getUserId();

        Expense expenses = expenseService.findExpenseById(userId, expenseId);
        System.out.println(userId+ " "+ expenseId);
        System.out.println(expenses);

        if (expenses != null) {
            return ResponseEntity.ok(new ApiResponse<>(
                    "about:blank",
                    "Expense get successfully",
                    HttpStatus.OK,
                    HttpStatus.OK.value(),
                    "/api/v1/expense/" + expenses,
                    new Date(),
                    null,
                    expenses
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable("id") UUID expenseId,
                                           @Valid @RequestBody ExpenseRequest expenseRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        UUID userId = user.getUserId();

        Expense expense = expenseService.updateExpanse(userId, expenseRequest);
        System.out.println(expense);

        ApiResponse<?> response = ApiResponse
                .builder()
                .message("Successfully get expense")
                .payload(expense)
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable("id") UUID expenseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        UUID userId = user.getUserId();

        expenseService.deleteExpanse(userId, expenseId);

        ApiResponse<?> response = ApiResponse
                .builder()
                .message("Successfully get expense")
                .payload(null)
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(response);

    }

}