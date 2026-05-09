package com.vaultx.banking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private String type;
    private double amount;
    private LocalDateTime timestamp;
    private String note;

    public Transaction(String type, double amount, String note) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.timestamp = LocalDateTime.now();
    }

    // ── Getters (needed for JSON serialization) ──────────────────────────────
    public String getType()             { return type; }
    public double getAmount()           { return amount; }
    public LocalDateTime getTimestamp()  { return timestamp; }
    public String getNote()             { return note; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("[%s] %-10s  Rs. %,.2f  | %s",
                timestamp.format(fmt), type, amount, note);
    }
}
