package org.example.expensetracking.service;

import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    User createUser(RegisterRequest registerRequest);
    User updatePasswordByEmail(String email, String encodedPassword);
}
