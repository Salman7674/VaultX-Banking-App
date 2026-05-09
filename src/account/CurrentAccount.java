package account;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Transaction;

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
        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be positive. You entered: Rs. " + amount);
        }
        // Overdraft: balance can go negative but not below -overdraftLimit
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
        System.out.printf("  Withdrawn Rs. %,.2f from %s. New balance: Rs. %,.2f%s%n",
                amount, accountNumber, balance, balance < 0 ? "  ⚠ Overdraft active" : "");
    }

    @Override
    public String getAccountType() {
        return "Current (overdraft: Rs. " + String.format("%,.0f", overdraftLimit) + ")";
    }

    public double getOverdraftLimit() { return overdraftLimit; }
}
