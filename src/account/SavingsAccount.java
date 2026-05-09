package account;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Transaction;

public class SavingsAccount extends BankAccount {

    private static final double DEFAULT_MIN_BALANCE = 1000.0;
    private double minBalance;

    public SavingsAccount(String accountNumber, String holderName, double initialDeposit) {
        super(accountNumber, holderName, initialDeposit);
        this.minBalance = DEFAULT_MIN_BALANCE;
    }

    public SavingsAccount(String accountNumber, String holderName,
                          double initialDeposit, double minBalance) {
        super(accountNumber, holderName, initialDeposit);
        this.minBalance = minBalance;
    }

    @Override
    public void withdraw(double amount) {
        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be positive. You entered: Rs. " + amount);
        }
        // Enforce minimum balance rule
        double balanceAfter = balance - amount;
        if (balanceAfter < minBalance) {
            double shortfall = minBalance - balanceAfter;
            throw new InsufficientFundsException(
                String.format("Cannot withdraw Rs. %,.2f. " +
                    "Savings account must maintain a minimum balance of Rs. %,.2f. " +
                    "You are short by Rs. %,.2f.",
                    amount, minBalance, shortfall),
                shortfall
            );
        }
        balance -= amount;
        transactions.add(new Transaction("WITHDRAW", amount, "Withdrawal"));
        System.out.printf("  Withdrawn Rs. %,.2f from %s. New balance: Rs. %,.2f%n",
                amount, accountNumber, balance);
    }

    @Override
    public String getAccountType() {
        return "Savings (min balance: Rs. " + String.format("%,.0f", minBalance) + ")";
    }

    public double getMinBalance() { return minBalance; }
}
