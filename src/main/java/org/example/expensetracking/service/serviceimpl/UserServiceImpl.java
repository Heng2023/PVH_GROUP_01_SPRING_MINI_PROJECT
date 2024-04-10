package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.repository.UserRepository;
import org.example.expensetracking.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
