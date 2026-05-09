package service;

import account.BankAccount;
import account.CurrentAccount;
import account.SavingsAccount;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Transaction;

import java.util.HashMap;
import java.util.Map;

public class BankService {

    private Map<String, BankAccount> accounts = new HashMap<>();
    private int accountCounter = 1000;

    // ── Account creation ──────────────────────────────────────────────────────

    public BankAccount createSavingsAccount(String holderName, double initialDeposit) {
        String accNo = "SAV" + (++accountCounter);
        BankAccount account = new SavingsAccount(accNo, holderName, initialDeposit);
        accounts.put(accNo, account);
        System.out.println("  ✔ Savings account created: " + accNo + " for " + holderName);
        return account;
    }

    public BankAccount createCurrentAccount(String holderName, double initialDeposit) {
        String accNo = "CUR" + (++accountCounter);
        BankAccount account = new CurrentAccount(accNo, holderName, initialDeposit);
        accounts.put(accNo, account);
        System.out.println("  ✔ Current account created: " + accNo + " for " + holderName);
        return account;
    }

    // ── Deposit ───────────────────────────────────────────────────────────────

    public void deposit(String accountNumber, double amount) {
        try {
            BankAccount account = getAccount(accountNumber);
            account.deposit(amount);
        } catch (InvalidAmountException e) {
            System.out.println("  ✘ Invalid deposit: " + e.getMessage());
        }
    }

    // ── Withdraw ──────────────────────────────────────────────────────────────

    public void withdraw(String accountNumber, double amount) {
        try {
            BankAccount account = getAccount(accountNumber);
            account.withdraw(amount);
        } catch (InsufficientFundsException e) {
            System.out.println("  ✘ Withdrawal failed: " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("  ✘ Invalid withdrawal: " + e.getMessage());
        }
    }

    // ── Transfer (synchronized to prevent race conditions) ────────────────────

    public synchronized void transfer(String fromAccNo, String toAccNo, double amount) {
        String threadName = Thread.currentThread().getName();
        System.out.println("\n  [" + threadName + "] Initiating transfer: "
                + fromAccNo + " → " + toAccNo + "  Rs. " + String.format("%,.2f", amount));
        try {
            BankAccount from = getAccount(fromAccNo);
            BankAccount to   = getAccount(toAccNo);

            // Withdraw from source (may throw)
            from.withdraw(amount);

            // Deposit to destination
            to.deposit(amount);

            // Log transfer note on both sides
            from.getTransactions().get(from.getTransactions().size() - 1);
            to.getTransactions().add(
                new Transaction("TRANSFER-IN", amount,
                    "Transfer from " + fromAccNo + " [" + threadName + "]"));
            // Replace last transaction note on source side
            from.getTransactions().add(
                new Transaction("TRANSFER-OUT", amount,
                    "Transfer to " + toAccNo + " [" + threadName + "]"));

            System.out.println("  [" + threadName + "] ✔ Transfer complete.");

        } catch (InsufficientFundsException e) {
            System.out.println("  [" + threadName + "] ✘ Transfer failed: " + e.getMessage());
        } catch (InvalidAmountException e) {
            System.out.println("  [" + threadName + "] ✘ Invalid transfer: " + e.getMessage());
        }
    }

    // ── Transaction history ───────────────────────────────────────────────────

    public void printHistory(String accountNumber) {
        try {
            getAccount(accountNumber).printTransactionHistory();
        } catch (IllegalArgumentException e) {
            System.out.println("  ✘ " + e.getMessage());
        }
    }

    // ── Summary ───────────────────────────────────────────────────────────────

    public void printSummary(String accountNumber) {
        try {
            getAccount(accountNumber).printSummary();
        } catch (IllegalArgumentException e) {
            System.out.println("  ✘ " + e.getMessage());
        }
    }

    public void printAllAccounts() {
        System.out.println("\n  All Accounts:");
        System.out.println("  " + "─".repeat(55));
        for (BankAccount acc : accounts.values()) {
            System.out.printf("  %-10s  %-20s  Rs. %,12.2f  [%s]%n",
                acc.getAccountNumber(),
                acc.getHolderName(),
                acc.getBalance(),
                acc.getAccountType().split(" ")[0]);   // "Savings" / "Current"
        }
        System.out.println("  " + "─".repeat(55));
    }

    // ── Multithreading demo ───────────────────────────────────────────────────

    public void runThreadDemo(String acc1, String acc2) {
        System.out.println("\n  ══════════════════════════════════════════════");
        System.out.println("  MULTITHREADING DEMO — two simultaneous transfers");
        System.out.println("  ══════════════════════════════════════════════");

        Runnable task1 = () -> transfer(acc1, acc2, 500);
        Runnable task2 = () -> transfer(acc2, acc1, 300);

        Thread t1 = new Thread(task1, "Thread-A");
        Thread t2 = new Thread(task2, "Thread-B");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("  Thread interrupted.");
        }

        System.out.println("\n  Both threads finished. Final balances:");
        printSummary(acc1);
        printSummary(acc2);
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    private BankAccount getAccount(String accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException(
                "Account not found: " + accountNumber);
        }
        return account;
    }
}
