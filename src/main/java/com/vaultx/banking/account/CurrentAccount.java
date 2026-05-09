package com.vaultx.banking.account;

import com.vaultx.banking.exception.InsufficientFundsException;
import com.vaultx.banking.exception.InvalidAmountException;
import com.vaultx.banking.model.Transaction;

public class CurrentAccount extends BankAccount {

    private static final double DEFAULT_OVERDRAFT_LIMIT = 5000.0;
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String holderName, double initialDeposit) {
        super(accountNumber, holderName, initialDeposit);
        this.overdraftLimit = DEFAULT_OVERDRAFT_LIMIT;
    }

    public CurrentAccount(String accountNumber, String holderName,
                          double initialDeposit, double overdraftLimit) {
        super(accountNumber, holderName, initialDeposit);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be positive. You entered: Rs. " + amount);
        }
        double balanceAfter = balance - amount;
        if (balanceAfter < -overdraftLimit) {
            double maxWithdrawable = balance + overdraftLimit;
            throw new InsufficientFundsException(
                String.format("Cannot withdraw Rs. %,.2f. " +
                    "Current account overdraft limit is Rs. %,.2f. " +
                    "Maximum you can withdraw right now: Rs. %,.2f.",
                    amount, overdraftLimit, maxWithdrawable)
            );
        }
        balance -= amount;
        String note = balance < 0 ? "Withdrawal (overdraft)" : "Withdrawal";
        transactions.add(new Transaction("WITHDRAW", amount, note));
    }

    @Override
    public String getAccountType() { return "Current"; }

    public double getOverdraftLimit() { return overdraftLimit; }
}
