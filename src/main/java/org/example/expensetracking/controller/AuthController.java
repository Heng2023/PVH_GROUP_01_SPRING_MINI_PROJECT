package org.example.expensetracking.controller;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.expensetracking.exceptionhandler.BlankFieldException;
import org.example.expensetracking.exceptionhandler.RegistrationException;
import org.example.expensetracking.exceptionhandler.UserNotFoundException;
import org.example.expensetracking.model.Otp;
import org.example.expensetracking.model.User;
import org.example.expensetracking.model.dto.request.ForgetRequest;
import org.example.expensetracking.model.dto.request.LoginRequest;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.ApiErrorResponse;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.AuthResponse;
import org.example.expensetracking.repository.OtpRepository;
import org.example.expensetracking.repository.UserRepository;
import org.example.expensetracking.security.JwtService;
import org.example.expensetracking.service.FileService;
import org.example.expensetracking.service.MailSenderService;
import org.example.expensetracking.service.OtpService;
import org.example.expensetracking.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@RequestMapping("/api/v1/auths")
@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final FileService fileService;
    private final OtpService otpService;

    private final MailSenderService mailSenderService;

    // Define a password pattern
    private static final String PASSWORD_PATTERN = ".{6,}";

    // Check if the password matches the defined pattern
    private boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }

    private boolean isValidEmail(String email) {
        // Simple email format validation
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private Date calculateExpirationDate() {
        // Set the OTP to expire in 5 minutes
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        return calendar.getTime();
    }

    @PutMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam("otpCode") String otpCode) {
        try {
            // Get the current time to check if the OTP has expired
            Date currentTime = new Date();

            // Find the OTP by its code and check if it has not expired
            Otp otp = otpService.findOtpByCodeAndNotExpired(otpCode, currentTime);

            if (otp == null) {
                // If the OTP is not found or has expired, return an error response
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", "Invalid or expired OTP code.");
                ApiErrorResponse errorResponse = new ApiErrorResponse(
                        "about:blank",
                        "Bad Request",
                        HttpStatus.BAD_REQUEST,
                        400,
                        "/api/v1/auths/verify",
                        new Date(),
                        errorMap
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Update the OTP's verify field to true in the database
            otpService.updateOtpVerification(otpCode);

            // Return a success response
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .type("about:blank")
                    .message("OTP verified successfully")
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .instance("/api/v1/auths/verify")
                    .timestamp(new Date())
                    .payload("OTP verified successfully")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the unexpected exception
            e.printStackTrace(); // Or use a logging framework
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/auths/verify",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/forget")
    public ResponseEntity<?> forgetPassword(@RequestParam("email") String email, @RequestBody ForgetRequest forgetRequest) {
        try {
            // Convert email to lowercase
            String lowerCaseEmail = email.toLowerCase();

            // Check if email is provided and exists
            if (lowerCaseEmail == null || lowerCaseEmail.isEmpty()) {
                throw new BlankFieldException("Email field is blank");
            }
            // Fetch user by email, converting email to lowercase for comparison
            User user = userService.findUserByEmail(lowerCaseEmail);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            // Check if password and confirm password are provided and match
            String password = forgetRequest.getPassword();
            String confirmPassword = forgetRequest.getConfirmPassword();
            if (password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
                throw new BlankFieldException("Password or Confirm Password field is blank");
            }

            // Validate the password format
            if (!isValidPassword(password)) {
                throw new BadRequestException("Password must contain at least six digits or characters");
            }

            if (!password.equals(confirmPassword)) {
                throw new BadRequestException("Password and Confirm Password do not match");
            }

            // Encrypt the new password
            String encodedPassword = passwordEncoder.encode(password);

            // Update user's password and fetch updated user details
            // Note: Ensure that the updatePasswordByEmail method in your UserRepository also uses the lowerCaseEmail for comparison
            User updatedUserDTO = userService.updatePasswordByEmail(lowerCaseEmail, encodedPassword);

            // Build user response DTO
            ApiResponse<User> response = ApiResponse.<User>builder()
                    .type("about:blank")
                    .message("Password reset successful")
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .instance("/api/v1/auths/forget")
                    .timestamp(new Date())
                    .payload(updatedUserDTO) // Include updated user details in payload
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (BlankFieldException e) {
            // Handle blank field exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "One or more fields is/are blank");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Blank Field Error",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/auths/forget",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (UserNotFoundException e) {
            // Handle user not found exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "User not found");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "User Not Found",
                    HttpStatus.NOT_FOUND,
                    404,
                    "/api/v1/auths/forget",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (BadRequestException e) {
            // Handle bad request exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Bad request");
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
            // Log the unexpected exception
            e.printStackTrace(); // Or use a logging framework
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred");
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
    public ResponseEntity<?> resendOtp(@RequestParam("email") String email) {
        try {
            // Convert email to lowercase
            String lowerCaseEmail = email.toLowerCase();

            // Fetch user DTO by email
            User userDTO = userService.findUserByEmail(lowerCaseEmail);
            System.out.println("Fetched user: " + userDTO); // Debug log

            if (userDTO == null) {
                // If the user is not found, return an error response
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", "User not found.");
                ApiErrorResponse errorResponse = new ApiErrorResponse(
                        "about:blank",
                        "User Not Found",
                        HttpStatus.NOT_FOUND,
                        404,
                        "/api/v1/auths/resend",
                        new Date(),
                        errorMap
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            // Ensure that userId is not null
            if (userDTO.getUserId() == null) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", "User ID is null.");
                ApiErrorResponse errorResponse = new ApiErrorResponse(
                        "about:blank",
                        "Internal Server Error",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        500,
                        "/api/v1/auths/resend",
                        new Date(),
                        errorMap
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

            // Check if the user has already verified (OTP is not required)
            boolean userVerified = otpService.userVerified(userDTO.getUserId());

            // If the user is already verified, return a message indicating that
            if (userVerified) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", "User has already been verified.");
                ApiErrorResponse errorResponse = new ApiErrorResponse(
                        "about:blank",
                        "Bad Request",
                        HttpStatus.BAD_REQUEST,
                        400,
                        "/api/v1/auths/resend",
                        new Date(),
                        errorMap
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Generate a new OTP code using OtpService
            String newOtpCode = otpService.generateOTP(6); // Assuming you want a 6-digit OTP

            // Create a new OTP object
            Otp newOtp = new Otp();
            newOtp.setOtpCode(newOtpCode);
            newOtp.setIssuedAt(new Date());
            newOtp.setExpiration(calculateExpirationDate()); // Implement this method to calculate the expiration date
            newOtp.setUserId(userDTO.getUserId()); // Set the userId from the User object

            // Save the new OTP to the database
            otpService.saveOtp(newOtp);

            // Send the new OTP to the user's email
            mailSenderService.sendEmail(lowerCaseEmail, newOtpCode);

            // Return a success response
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .type("about:blank")
                    .message("New OTP sent successfully.")
                    .code(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .instance("/api/v1/auths/resend")
                    .timestamp(new Date())
                    .payload("New OTP sent successfully.")
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            // Log the unexpected exception
            e.printStackTrace(); // Or use a logging framework
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/auths/resend",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Convert email to lowercase
            String lowerCaseEmail = registerRequest.getEmail().toLowerCase();
            registerRequest.setEmail(lowerCaseEmail);

            // Validate registerRequest...
            // Validate the password format
            if (!isValidPassword(registerRequest.getPassword())) {
                throw new BadRequestException("Password must contain at least six digits or characters");
            }

            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                throw new RegistrationException("Password and Confirm Password do not match");
            }

            if (registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()) {
                throw new RegistrationException("One or more fields is/are blank");
            }

            if (registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()) {
                throw new RegistrationException("One or more fields is/are blank");
            }

            // Check if email format is valid
            if (!isValidEmail(registerRequest.getEmail())) {
                throw new BadRequestException("Invalid email format");
            }

            // Check if the email is already registered
            User existingUser = userService.findUserByEmail(registerRequest.getEmail());
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
            User appUserDTO = userService.createUser(registerRequest);

            // Return ApiResponse for successful registration
            ApiResponse<User> response = ApiResponse.<User>builder()
                    .type("about:blank")
                    .message("Successfully Registered")
                    .code(HttpStatus.CREATED.value())
                    .status(HttpStatus.CREATED)
                    .instance("/api/v1/auths/register")
                    .timestamp(new Date())
                    .payload(appUserDTO)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RegistrationException e) {
            // Handle registration exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Registration failed");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Registration Error",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/auths/register",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (BadRequestException e) {
            // Handle bad request exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Bad request");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Bad Request",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/auths/register",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Log the unexpected exception
            e.printStackTrace(); // Or use a logging framework
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/auths/register",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            // Convert email to lowercase
            String lowerCaseEmail = loginRequest.getEmail().toLowerCase();
            loginRequest.setEmail(lowerCaseEmail);

            // Check if email or password is blank
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                    loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                throw new BlankFieldException("One or more fields is/are blank");
            }

            // Check if email format is valid
            if (!isValidEmail(loginRequest.getEmail())) {
                throw new BadRequestException("Invalid email format");
            }

            // Check if the user exists
            User user = userService.findUserByEmail(loginRequest.getEmail());
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            // Check if the user has already verified via OTP
            boolean userVerified = otpService.userVerified(user.getUserId());
            if (!userVerified) {
                throw new BadRequestException("User has not been verified");
            }

            // Authenticate user
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                throw new BadRequestException("Invalid password");
            }

            // Generate JWT token
            final String token = jwtService.generateToken(userDetails);

            // Create and return authentication response with token
            AuthResponse authResponse = new AuthResponse(token);
            ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                    .type("about:blank")
                    .message("Authentication successful")
                    .code(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .instance("/api/v1/auths/login")
                    .timestamp(new Date())
                    .payload(authResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserNotFoundException e) {
            // Handle user not found exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "User not found");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "User Not Found",
                    HttpStatus.NOT_FOUND,
                    404,
                    "/api/v1/auth/login",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (BadRequestException e) {
            // Handle bad request exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Bad request");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Bad Request",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/auth/login",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (BlankFieldException e) {
            // Handle blank field exception
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "One or more fields is/are blank");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Blank Field Error",
                    HttpStatus.BAD_REQUEST,
                    400,
                    "/api/v1/auth/login",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (DataAccessException e) {
            // Handle database access exceptions
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Database error occurred");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/auth/login",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            // Log the unexpected exception
            e.printStackTrace(); // Or use a logging framework
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred");
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    "about:blank",
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    500,
                    "/api/v1/auth/login",
                    new Date(),
                    errorMap
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}