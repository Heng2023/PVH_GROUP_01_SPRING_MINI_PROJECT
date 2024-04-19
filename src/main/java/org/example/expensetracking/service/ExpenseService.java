package org.example.expensetracking.service;

import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    ExpenseRequest insertExpense(ExpenseRequest expenseRequest, UUID userId);

    List<Expense> findAllExpense();

    Expense updateExpanse(UUID userId, ExpenseRequest expenseRequest);

    void deleteExpanse(UUID userId, UUID expanseId);

    Expense findExpenseById(UUID userId, UUID id);
}
