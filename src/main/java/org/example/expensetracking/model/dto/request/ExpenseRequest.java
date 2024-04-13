package org.example.expensetracking.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {
    private double amount;
    private String description;
    private Date date;
    private UUID categoryId;
}
