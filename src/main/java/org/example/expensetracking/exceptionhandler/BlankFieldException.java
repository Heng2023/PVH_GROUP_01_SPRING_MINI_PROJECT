package org.example.expensetracking.exceptionhandler;

public class BlankFieldException extends RuntimeException {
    public BlankFieldException(String message) {
        super(message);
    }
}
