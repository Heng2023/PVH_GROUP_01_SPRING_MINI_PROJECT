package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.model.Expense;
import org.example.expensetracking.model.dto.request.ExpenseRequest;
import org.example.expensetracking.repository.ExpenseRepository;
import org.example.expensetracking.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseRequest insertExpense(ExpenseRequest expenseRequest, UUID userId) {
        return expenseRepository.saveExpense(expenseRequest, userId);
    }

    @Override
    public List<Expense> findAllExpense() {
        return expenseRepository.findAllExpense();
    }

    @Override
    public Expense updateExpanse(UUID userId, ExpenseRequest expenseRequest) {
        return expenseRepository.updateExpense(userId, expenseRequest);
    }

    @Override
    public void deleteExpanse(UUID userId, UUID expanseId) {
        expenseRepository.deleteExpense(userId, expanseId);
    }

    @Override
    public Expense findExpenseById(UUID userId, UUID expanseId) {
        return expenseRepository.getExpenseById(userId, expanseId);
    }
}
