package org.example.expensetracking.service;

import org.apache.coyote.BadRequestException;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    User createUser(RegisterRequest registerRequest) throws BadRequestException;
    User updatePasswordByEmail(String email, String encodedPassword);
    User findUserById(UUID userId);
}
