package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.model.CustomUserDetails;
import org.example.expensetracking.model.Otp;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.AppUserDTO;
import org.example.expensetracking.repository.OtpRepository;
import org.example.expensetracking.repository.UserRepository;
import org.example.expensetracking.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, OtpRepository otpRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUserDTO createUser(RegisterRequest registerRequest) {
        String passwordEncode = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(passwordEncode);
        User appUser = userRepository.createUser(registerRequest);
        return modelMapper.map(appUser, AppUserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        return new CustomUserDetails(user);
    }
}
