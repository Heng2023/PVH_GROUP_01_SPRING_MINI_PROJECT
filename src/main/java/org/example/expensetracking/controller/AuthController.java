package org.example.expensetracking.controller;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.expensetracking.exceptionhandler.BlankFieldException;
import org.example.expensetracking.exceptionhandler.RegistrationException;
import org.example.expensetracking.exceptionhandler.UserNotFoundException;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.ForgetRequest;
import org.example.expensetracking.model.dto.request.LoginRequest;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.ApiErrorResponse;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.AppUserDTO;
import org.example.expensetracking.model.dto.response.AuthResponse;
import org.example.expensetracking.repository.UserRepository;
import org.example.expensetracking.security.JwtService;
import org.example.expensetracking.service.FileService;
import org.example.expensetracking.service.MailSenderService;
import org.example.expensetracking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/v1/auths")
@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final FileService fileService;

    private final MailSenderService mailSenderService;

    private boolean isValidEmail(String email) {
        // Simple email format validation
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    @PutMapping("/verify")
    public String verify() {
        return "verify";
    }

    @PostMapping("/forget")
    public ResponseEntity<?> forgetPassword(@RequestParam("email") String email, @RequestBody ForgetRequest forgetRequest) {
        try {
            // Check if email is provided and exists
            if (email == null || email.isEmpty()) {
                throw new BlankFieldException("Email field is blank");
            }
            User user = userRepository.findUserByEmail(email);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            // Check if password and confirm password are provided and match
            String password = forgetRequest.getPassword();
            String confirmPassword = forgetRequest.getConfirmPassword();
            if (password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
                throw new BlankFieldException("Password or Confirm Password field is blank");
            }
            if (!password.equals(confirmPassword)) {
                throw new BadRequestException("Password and Confirm Password do not match");
            }

            // Encrypt the new password
            String encodedPassword = passwordEncoder.encode(password);

            // Update user's password
            userRepository.updatePasswordByEmail(email, encodedPassword);

            // Return success response
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .type("about:blank")
                    .message("Password reset successful")
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .instance("/api/v1/auths/forget")
                    .timestamp(new Date())
                    .payload(null)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserNotFoundException | BlankFieldException | BadRequestException e) {
            // Handle specific exceptions with ApiErrorResponse
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error occurred");

            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Bad Request",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/auths/forget",
                    new Date(),
                    errorMap
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Handle other exceptions with ApiErrorResponse
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error occurred");

            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/auths/forget",
                    new Date(),
                    errorMap
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("resend")
    public String resend() {
        return "resend";
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws BadRequestException {
        // Validate registerRequest...
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RegistrationException("Password and Confirm Password do not match");
        }

        if (registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()) {
            throw new RegistrationException("One or more fields are blank");
        }

        if (registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()) {
            throw new RegistrationException("One or more fields are blank");
        }

        // Check if email format is valid
        if (!isValidEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Invalid email format");
        }

        // Check if the email is already registered
        User existingUser = userRepository.findUserByEmail(registerRequest.getEmail());
        if (existingUser != null) {
            throw new RegistrationException("Email is already registered");
        }

        // Check if profile image is provided and exists
        if (registerRequest.getProfileImage() != null && !registerRequest.getProfileImage().isEmpty()) {
            try {
                fileService.getFileByFileName(registerRequest.getProfileImage());
            } catch (FileNotFoundException e) {
                throw new BadRequestException("Profile image does not exist");
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while checking profile image", e);
            }
        } else {
            throw new BadRequestException("Profile image cannot be blank");
        }

        // If all validations pass, proceed with registration
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(encodedPassword);
        AppUserDTO appUserDTO = userService.createUser(registerRequest);

        // Return ApiResponse for successful registration
        ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder()
                .type("about:blank")
                .message("Successfully Registered")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .instance("/api/v1/venues/" + appUserDTO.getUserId())
                .timestamp(new Date())
                .payload(appUserDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) throws BadRequestException {
        try {
            // Check if email format is valid
            if (!isValidEmail(loginRequest.getEmail())) {
                throw new BadRequestException("Invalid email format");
            }

            // Check if email or password is blank
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                    loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                throw new BlankFieldException("One or more fields are blank");
            }

            // Check if the user exists
            User user = userRepository.findUserByEmail(loginRequest.getEmail());
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            // Authenticate user
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                throw new BadRequestException("Invalid password");
            }

            // Generate JWT token
            final String token = jwtService.generateToken(userDetails);

            // Create and return authentication response
            AuthResponse authResponse = new AuthResponse(token);
            return ResponseEntity.ok(authResponse);
        } catch (UserNotFoundException | BadRequestException | BlankFieldException e) {
            throw e; // Let the GlobalExceptionHandler handle the exception
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred"); // Let Spring handle other unexpected exceptions
        }
    }
}