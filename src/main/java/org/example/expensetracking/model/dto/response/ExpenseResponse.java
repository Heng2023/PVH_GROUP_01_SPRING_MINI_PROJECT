package org.example.expensetracking.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.expensetracking.model.Category;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private UUID expenseId;
    private double amount;
    private String description;
    private Date date;
    private UserResponse user;
    private CategoryExpenseResponse category;
}
