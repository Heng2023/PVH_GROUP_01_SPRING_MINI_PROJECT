package org.example.expensetracking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Expense {
    private UUID expenseId;
    private int amount;
    private String description;
    private Date date;
    private UUID userId;
    private UUID categoryId;
}
