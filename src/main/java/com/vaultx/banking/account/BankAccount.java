package com.vaultx.banking.account;

import com.vaultx.banking.exception.InvalidAmountException;
import com.vaultx.banking.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount {

    protected String accountNumber;
    protected String holderName;
    protected double balance;
    protected List<Transaction> transactions;

    public BankAccount(String accountNumber, String holderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialDeposit;
        this.transactions = new ArrayList<>();
        if (initialDeposit > 0) {
            transactions.add(new Transaction("DEPOSIT", initialDeposit, "Account opened"));
        }
    }

    // ── Concrete deposit (same logic for all account types) ──────────────────
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Deposit amount must be positive. You entered: Rs. " + amount);
        }
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, "Deposit"));
    }

    // ── Each subclass enforces its own withdrawal rules ───────────────────────
    public abstract void withdraw(double amount);

    // ── Each subclass identifies itself ──────────────────────────────────────
    public abstract String getAccountType();

    // ── Getters (needed for JSON serialization) ──────────────────────────────
    public String getAccountNumber()        { return accountNumber; }
    public String getHolderName()           { return holderName; }
    public double getBalance()              { return balance; }
    public List<Transaction> getTransactions() { return transactions; }
}
