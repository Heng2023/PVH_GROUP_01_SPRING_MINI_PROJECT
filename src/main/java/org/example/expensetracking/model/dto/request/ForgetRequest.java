package org.example.expensetracking.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgetRequest {
    private BCryptPasswordEncoder password;
    private BCryptPasswordEncoder confirmPassword;
}
