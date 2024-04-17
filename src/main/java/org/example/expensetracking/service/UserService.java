package org.example.expensetracking.service;

import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.AppUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUserDTO createUser(RegisterRequest registerRequest);
    AppUserDTO findUserById(UUID userId);
    User findUserByEmail(String email);
}
