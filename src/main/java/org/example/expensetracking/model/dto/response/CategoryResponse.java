package org.example.expensetracking.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.expensetracking.model.User;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponse {
    private UUID categoryID;
    private String name;
    private String description;
    private UserResponse user;
}
