package org.example.expensetracking.exceptionhandler;

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
}