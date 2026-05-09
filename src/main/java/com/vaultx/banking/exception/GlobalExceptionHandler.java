package com.vaultx.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Catches exceptions thrown by the service layer and returns
 * clean JSON error responses instead of stack traces.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAmount(InvalidAmountException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientFunds(InsufficientFundsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "error", e.getMessage(),
                    "shortfall", e.getShortfall()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }
}
