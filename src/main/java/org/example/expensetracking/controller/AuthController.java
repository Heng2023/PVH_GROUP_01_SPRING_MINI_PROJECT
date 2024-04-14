package org.example.expensetracking.controller;

import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.expensetracking.model.dto.request.LoginRequest;
import org.example.expensetracking.model.dto.request.RegisterRequest;
import org.example.expensetracking.model.dto.response.ApiResponse;
import org.example.expensetracking.model.dto.response.AppUserDTO;
import org.example.expensetracking.model.dto.response.AuthResponse;
import org.example.expensetracking.security.JwtService;
import org.example.expensetracking.service.MailSenderService;
import org.example.expensetracking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auths")
@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final MailSenderService mailSenderService;


    @PutMapping("/verify")
    public String verify() {
        return "verify";
    }

    @PutMapping("/forget")
    public String forget() {
        return "forget";
    }

    @PostMapping("resend")
    public String resend() {
        return "resend";
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        registerRequest.setPassword(encodedPassword);
        AppUserDTO appUserDTO = userService.createUser(registerRequest);

        ApiResponse<AppUserDTO> response = ApiResponse.<AppUserDTO>builder().message("Successfully Registered").code(201).
                status(HttpStatus.CREATED).payload(appUserDTO).build();
        System.out.println(appUserDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(authResponse);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            UserDetails userDetails = userService.loadUserByUsername(email);
            System.out.println("UserDetail : " + userDetails);
            if (userDetails == null) {
                throw new BadRequestException("User not found");
            }
            System.out.println("UserDetail is not null");
            System.out.println("Password : " + password);
            System.out.println("UserDetail password : " + userDetails.getPassword());
            System.out.println(passwordEncoder.matches(password, userDetails.getPassword()));
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("User disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }
}