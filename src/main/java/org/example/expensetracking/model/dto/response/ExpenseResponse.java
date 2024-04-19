package org.example.expensetracking.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructorgit
public class ExpenseResponse {
    private UUID expenseId;
    private int amount;
    private String description;
    private Date date;
    private UserResponse user;
    private CategoryResponse category;
}
