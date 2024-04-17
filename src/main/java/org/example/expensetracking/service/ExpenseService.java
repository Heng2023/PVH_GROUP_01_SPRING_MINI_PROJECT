package org.example.expensetracking.service;

import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

public interface ExpenseService {


    Expense insertExpense(ExpenseRequest expenseRequest);

}
