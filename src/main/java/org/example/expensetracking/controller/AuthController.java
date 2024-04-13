package org.example.expensetracking.controller;

import lombok.AllArgsConstructor;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.AppUserDTO;
import org.example.expensetracking.security.JwtService;
import org.example.expensetracking.service.MailSenderService;
import org.example.expensetracking.service.OtpService;
import org.example.expensetracking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auths")
@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MailSenderService mailSenderService;
    private final OtpService otpService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        String otp = otpService.generateOTP(6);
        mailSenderService.sendEmail("sokheng.tey03@gmail.com",otp);
        AppUserDTO appUserDTO = appUserService.createUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.builder().title("Successfully register").status(HttpStatus.CREATED).code(201).payload(appUserDTO).build());
    }
}