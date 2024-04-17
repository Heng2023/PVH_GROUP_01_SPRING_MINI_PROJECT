package org.example.expensetracking.exceptionhandler;

import org.apache.coyote.BadRequestException;
import org.example.expensetracking.model.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error occurred");

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "about:blank",
                "Bad Request",
                HttpStatus.BAD_REQUEST,
                400,
                "/api/v1/files/upload",
                new Date(),
                errorMap
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage() != null ? e.getMessage() : "File not found");

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "about:blank",
                "File Not Found",
                HttpStatus.NOT_FOUND,
                404,
                "/api/v1/files",
                new Date(),
                errorMap
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error occurred");

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "about:blank",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                500,
                "/api/v1/files",
                new Date(),
                errorMap
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<?> handleRegistrationException(RegistrationException e) {
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
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Authentication failed");

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "about:blank",
                "Authentication Error",
                HttpStatus.UNAUTHORIZED,
                401,
                "/api/v1/auth/login",
                new Date(),
                errorMap
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
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
    }

    @ExceptionHandler(BlankFieldException.class)
    public ResponseEntity<?> handleBlankFieldException(BlankFieldException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage() != null ? e.getMessage() : "One or more fields are blank");

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
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Bad request");

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "about:blank",
                "Bad Request",
                HttpStatus.BAD_REQUEST,
                400,
                "/api/v1/",
                new Date(),
                errorMap
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> handleCategoryNotFoundException(CategoryNotFoundException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Category not found");

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "about:blank",
                "Category Not Found",
                HttpStatus.NOT_FOUND,
                404,
                "/api/v1/categories",
                new Date(),
                errorMap
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}