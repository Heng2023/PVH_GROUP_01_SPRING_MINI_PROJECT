package org.example.expensetracking.controller;

import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expense/")
public class ExpenseController {

    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody ExpenseRequest expenseRequest) {

        return null;
    }
}
