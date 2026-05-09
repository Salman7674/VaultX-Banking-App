package com.vaultx.banking.service;

import com.vaultx.banking.account.BankAccount;
import com.vaultx.banking.account.CurrentAccount;
import com.vaultx.banking.account.SavingsAccount;
import com.vaultx.banking.model.Transaction;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankService {

    private final Map<String, BankAccount> accounts = new HashMap<>();
    private int accountCounter = 1000;

    // ── Account creation ──────────────────────────────────────────────────────

    public BankAccount createSavingsAccount(String holderName, double initialDeposit) {
        String accNo = "SAV" + (++accountCounter);
        BankAccount account = new SavingsAccount(accNo, holderName, initialDeposit);
        accounts.put(accNo, account);
        return account;
    }

    public BankAccount createCurrentAccount(String holderName, double initialDeposit) {
        String accNo = "CUR" + (++accountCounter);
        BankAccount account = new CurrentAccount(accNo, holderName, initialDeposit);
        accounts.put(accNo, account);
        return account;
    }

    // ── Deposit ───────────────────────────────────────────────────────────────

    public BankAccount deposit(String accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        account.deposit(amount);
        return account;
    }

    // ── Withdraw ──────────────────────────────────────────────────────────────

    public BankAccount withdraw(String accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        account.withdraw(amount);
        return account;
    }

    // ── Transfer (synchronized to prevent race conditions) ────────────────────

    public synchronized void transfer(String fromAccNo, String toAccNo, double amount) {
        if (fromAccNo.equals(toAccNo)) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }
        BankAccount from = getAccount(fromAccNo);
        BankAccount to   = getAccount(toAccNo);

        from.withdraw(amount);
        to.deposit(amount);

        // Replace generic notes with transfer-specific ones
        List<Transaction> fromTxns = from.getTransactions();
        fromTxns.remove(fromTxns.size() - 1);
        fromTxns.add(new Transaction("TRANSFER-OUT", amount,
                "Transfer to " + toAccNo + " (" + to.getHolderName() + ")"));

        List<Transaction> toTxns = to.getTransactions();
        toTxns.remove(toTxns.size() - 1);
        toTxns.add(new Transaction("TRANSFER-IN", amount,
                "Transfer from " + fromAccNo + " (" + from.getHolderName() + ")"));
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public BankAccount getAccount(String accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }

    public List<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public double getTotalBalance() {
        return accounts.values().stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
    }

    public int getTotalTransactionCount() {
        return accounts.values().stream()
                .mapToInt(a -> a.getTransactions().size())
                .sum();
    }

    public long getSavingsCount() {
        return accounts.values().stream()
                .filter(a -> a instanceof SavingsAccount)
                .count();
    }

    public long getCurrentCount() {
        return accounts.values().stream()
                .filter(a -> a instanceof CurrentAccount)
                .count();
    }
}
