package org.example.expensetracking.controller;

import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.example.expensetracking.model.dto.response.ExpenseResponse;
import org.example.expensetracking.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/expense/")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    // Add expense from client
    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody ExpenseRequest expenseRequest) {
        ExpenseResponse<Expense> response = ExpenseResponse.<Expense>builder()
                .message("The expense has been successfully created.")
                .payload(expenseService.insertExpense(expenseRequest))
                .status(HttpStatus.CREATED)
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
