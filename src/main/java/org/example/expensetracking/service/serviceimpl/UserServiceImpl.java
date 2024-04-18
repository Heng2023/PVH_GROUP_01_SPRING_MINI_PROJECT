package org.example.expensetracking.service.serviceimpl;

import org.apache.coyote.BadRequestException;
import org.example.expensetracking.exceptionhandler.RegistrationException;
import org.example.expensetracking.model.CustomUserDetails;
import org.example.expensetracking.model.Otp;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.repository.OtpRepository;
import org.example.expensetracking.repository.UserRepository;
import org.example.expensetracking.service.FileService;
import org.example.expensetracking.service.MailSenderService;
import org.example.expensetracking.service.OtpService;
import org.example.expensetracking.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpService otpService;
    private final MailSenderService mailSenderService;
    private final FileService fileService;
    public UserServiceImpl(UserRepository userRepository, OtpRepository otpRepository, OtpService otpService, MailSenderService mailSenderService, FileService fileService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
        this.mailSenderService = mailSenderService;
        this.fileService = fileService;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        return new CustomUserDetails(user);
    }

    @Override
    public User createUser(RegisterRequest registerRequest) throws BadRequestException {
        // Check if profile image is provided and exists
        if (registerRequest.getProfileImage() != null && !registerRequest.getProfileImage().isEmpty()) {
            // Check if the profile image is already used by another user
            User existingUserWithSameProfileImage = userRepository.findUserByProfileImage(registerRequest.getProfileImage());
            if (existingUserWithSameProfileImage != null) {
                throw new RegistrationException("Profile image already exists for another user");
            }
        } else {
            throw new BadRequestException("Profile image cannot be blank");
        }

        // Generate OTP
        String otp = otpService.generateOTP(6); // Assuming 6-digit OTP

        // Proceed with user registration and retrieve the user ID
        User savedUser = userRepository.saveUser(registerRequest);
        UUID userId = savedUser.getUserId();
        System.out.println(userId);

        // Calculate expiration time for OTP (2 minutes from now)
        Date expiration = new Date(System.currentTimeMillis() + (2 * 60 * 1000)); // Current time + 2 minutes

        // Save OTP in the database
        Otp otpObject = new Otp();
        otpObject.setOtpCode(otp);
        otpObject.setIssuedAt(new Date());
        otpObject.setExpiration(expiration); // Set expiration time
        otpObject.setVerify(false); // Initially set as unverified
        otpObject.setUserId(userId); // Set user ID
        otpRepository.saveOtp(otpObject);

        // Send OTP via email
        mailSenderService.sendEmail(registerRequest.getEmail(), otp);

        // Return saved user details
        return savedUser;
    }

    @Override
    public User updatePasswordByEmail(String email, String encodedPassword) {
        return userRepository.updatePasswordByEmail(email, encodedPassword);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
