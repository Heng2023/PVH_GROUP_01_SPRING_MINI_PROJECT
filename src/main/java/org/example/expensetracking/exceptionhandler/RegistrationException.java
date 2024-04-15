package org.example.expensetracking.exceptionhandler;

public class RegistrationException extends RuntimeException{
    public RegistrationException(String message) {
        super(message);
    }
}
