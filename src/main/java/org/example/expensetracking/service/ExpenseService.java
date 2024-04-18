package org.example.expensetracking.service;

import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    Expense insertExpense(ExpenseRequest expenseRequest, UUID userId);

    List<Expense> findAllExpense();
}
