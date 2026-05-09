package com.vaultx.banking.dto;

public class CreateAccountRequest {
    private String holderName;
    private String accountType;  // "savings" or "current"
    private double initialDeposit;

    public String getHolderName()      { return holderName; }
    public String getAccountType()     { return accountType; }
    public double getInitialDeposit()  { return initialDeposit; }

    public void setHolderName(String holderName)         { this.holderName = holderName; }
    public void setAccountType(String accountType)       { this.accountType = accountType; }
    public void setInitialDeposit(double initialDeposit) { this.initialDeposit = initialDeposit; }
}
