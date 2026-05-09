package com.vaultx.banking.dto;

public class TransferRequest {
    private String fromAccount;
    private String toAccount;
    private double amount;

    public String getFromAccount()   { return fromAccount; }
    public String getToAccount()     { return toAccount; }
    public double getAmount()        { return amount; }

    public void setFromAccount(String fromAccount)   { this.fromAccount = fromAccount; }
    public void setToAccount(String toAccount)       { this.toAccount = toAccount; }
    public void setAmount(double amount)             { this.amount = amount; }
}
