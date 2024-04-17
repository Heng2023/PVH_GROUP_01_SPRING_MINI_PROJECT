package org.example.expensetracking.repository;

import org.apache.ibatis.annotations.Mapper;
import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;

@Mapper
public interface ExpenseRepository {






    Expense insertExpense(ExpenseRequest expenseRequest);
}
