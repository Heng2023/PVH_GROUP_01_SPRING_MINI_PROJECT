package org.example.expensetracking.service;

import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.example.expensetracking.model.dto.response.ExpenseResponse;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest expenseRequest, UUID userId);

    List<ExpenseResponse> getAllExpenses();
    ExpenseResponse getExpenseById(UUID expenseId);

    void deleteExpense(UUID expenseId);
    ExpenseResponse updateExpense(UUID expenseId, ExpenseRequest expenseRequest);

}
