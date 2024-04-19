package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.ExpenseResponse;
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
@RequestMapping("/api/v1/expense/")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.findUserByEmail(email);
            UUID currentUserId = user.getUserId();

            ExpenseResponse expenseResponse = expenseService.createExpense(expenseRequest, currentUserId);

            ApiResponse<ExpenseResponse> response = ApiResponse.<ExpenseResponse>builder()
                    .type("about:blank")
                    .message("Expense created successfully")
                    .payload(expenseResponse)
                    .status(HttpStatus.CREATED)
                    .code(201)
                    .instance("/api/v1/expenses")
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllExpenses() {
        try {
            List<ExpenseResponse> allExpenses = expenseService.getAllExpenses();
            ApiResponse<List<ExpenseResponse>> response = ApiResponse.<List<ExpenseResponse>>builder()
                    .type("about:blank")
                    .message("All expenses retrieved successfully")
                    .payload(allExpenses)
                    .status(HttpStatus.OK)
                    .code(200)
                    .instance("/api/v1/expenses")
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable UUID expenseId) {
        try {
            // Call the ExpenseService method to delete the expense
            expenseService.deleteExpense(expenseId);

            // Construct the response
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .type("about:blank")
                    .message("Expense deleted successfully")
                    .status(HttpStatus.OK)
                    .code(200)
                    .instance("/api/v1/expense/" + expenseId)
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle case where the expense with the given ID does not exist
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .type("about:blank")
                    .message("Expense with ID " + expenseId + " not found")
                    .status(HttpStatus.NOT_FOUND)
                    .code(404)
                    .instance("/api/v1/expense/" + expenseId)
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<?> getExpenseById(@PathVariable UUID expenseId) {
        try {
            // Call the ExpenseService method to retrieve the expense by ID
            ExpenseResponse expenseResponse = expenseService.getExpenseById(expenseId);

            // Construct the response
            ApiResponse<ExpenseResponse> response = ApiResponse.<ExpenseResponse>builder()
                    .type("about:blank")
                    .message("Expense retrieved successfully")
                    .payload(expenseResponse)
                    .status(HttpStatus.OK)
                    .code(200)
                    .instance("/api/v1/expense/" + expenseId)
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle case where the expense with the given ID does not exist
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .type("about:blank")
                    .message("Expense with ID " + expenseId + " not found")
                    .status(HttpStatus.NOT_FOUND)
                    .code(404)
                    .instance("/api/v1/expense/" + expenseId)
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<?> updateExpense(@PathVariable UUID expenseId, @Valid @RequestBody ExpenseRequest expenseRequest) {
        try {
            // Call the ExpenseService method to update the expense
            ExpenseResponse updatedExpense = expenseService.updateExpense(expenseId, expenseRequest);

            // Construct the response
            ApiResponse<ExpenseResponse> response = ApiResponse.<ExpenseResponse>builder()
                    .type("about:blank")
                    .message("Expense updated successfully")
                    .payload(updatedExpense)
                    .status(HttpStatus.OK)
                    .code(200)
                    .instance("/api/v1/expense/" + expenseId)
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Handle case where the expense with the given ID does not exist
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .type("about:blank")
                    .message("Expense with ID " + expenseId + " not found")
                    .status(HttpStatus.NOT_FOUND)
                    .code(404)
                    .instance("/api/v1/expense/" + expenseId)
                    .timestamp(new Date())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
