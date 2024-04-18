package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.expensetracking.model.Category;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
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
    public ResponseEntity<?> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest, UUID categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);


        Expense expense = expenseService.insertExpense(expenseRequest, user.getUserId());

        ApiResponse<?> response = ApiResponse
                .builder()
                .message("Successfully added expense")
                .payload(expense)
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllExpenses() {
        List<Expense> expenses = expenseService.findAllExpense();

        ApiResponse<?> response = ApiResponse
                .builder()
                .message("Successfully get expense")
                .payload(expenses)
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(response);
    }
}
