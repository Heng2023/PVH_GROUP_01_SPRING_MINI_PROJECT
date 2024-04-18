package org.example.expensetracking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.expensetracking.model.dto.response.AppUserDTO;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private UUID categoryID;
    private String name;
    private String description;
    private User user;
}
