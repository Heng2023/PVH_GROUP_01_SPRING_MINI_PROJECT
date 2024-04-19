package org.example.expensetracking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.expensetracking.model.dto.response.ExpenseResponse;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Expense {
    private UUID expenseId;
    private double amount;
    private String description;
    private Date date;
    private User user;
    private Category category;
}
