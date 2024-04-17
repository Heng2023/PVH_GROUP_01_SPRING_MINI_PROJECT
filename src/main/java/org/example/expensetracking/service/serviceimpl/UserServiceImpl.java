package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.model.CustomUserDetails;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.AppUserDTO;
import org.example.expensetracking.repository.UserRepository;
import org.example.expensetracking.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        return new CustomUserDetails(user);
    }

    @Override
    public AppUserDTO createUser(RegisterRequest registerRequest) {
        return userRepository.saveUser(registerRequest);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
