package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.repository.ExpenseRepository;
import org.example.expensetracking.service.ExpenseService;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
}
