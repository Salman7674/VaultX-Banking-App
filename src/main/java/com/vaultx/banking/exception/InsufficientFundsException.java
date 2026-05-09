package com.vaultx.banking.exception;

public class InsufficientFundsException extends RuntimeException {

    private double shortfall;

    public InsufficientFundsException(String message, double shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public InsufficientFundsException(String message) {
        super(message);
        this.shortfall = 0;
    }

    public double getShortfall() { return shortfall; }
}
