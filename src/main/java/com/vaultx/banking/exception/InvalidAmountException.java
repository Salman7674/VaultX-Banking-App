package com.vaultx.banking.exception;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
