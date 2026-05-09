package account;

import exception.InvalidAmountException;
import model.Transaction;

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
        System.out.printf("  Deposited Rs. %,.2f to %s. New balance: Rs. %,.2f%n",
                amount, accountNumber, balance);
    }

    // ── Each subclass enforces its own withdrawal rules ───────────────────────
    public abstract void withdraw(double amount);

    // ── Each subclass identifies itself ──────────────────────────────────────
    public abstract String getAccountType();

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getAccountNumber()        { return accountNumber; }
    public String getHolderName()           { return holderName; }
    public double getBalance()              { return balance; }
    public List<Transaction> getTransactions() { return transactions; }

    // ── Pretty summary ────────────────────────────────────────────────────────
    public void printSummary() {
        System.out.println("  ┌─────────────────────────────────────────┐");
        System.out.printf ("  │  Account  : %-28s│%n", accountNumber);
        System.out.printf ("  │  Holder   : %-28s│%n", holderName);
        System.out.printf ("  │  Type     : %-28s│%n", getAccountType());
        System.out.printf ("  │  Balance  : Rs. %-25,.2f│%n", balance);
        System.out.println("  └─────────────────────────────────────────┘");
    }

    public void printTransactionHistory() {
        System.out.println("\n  Transaction History — " + accountNumber);
        System.out.println("  " + "─".repeat(65));
        if (transactions.isEmpty()) {
            System.out.println("  No transactions found.");
        } else {
            for (Transaction t : transactions) {
                System.out.println("  " + t);
            }
        }
        System.out.println("  " + "─".repeat(65));
    }
}
