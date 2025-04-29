package model;

import java.util.ArrayList;

public abstract class BankAccount {

    private static int nextAccountNumber = 100000000;
    private int accountNo;
    private String accountName;
    private double balance;
    private String status;

    public BankAccount() {
        this.accountNo = nextAccountNumber++;
        accountName = "";
        balance = 0.0;
        status = "Active";
    }

    public BankAccount(int accountNo, String accountName) {
        this.accountNo = accountNo;
        this.accountName = accountName;
        balance = 0.0;
        status = "Active";
    }

    public BankAccount(int accountNo, String accountName, String status) {
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.status = status;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public double inquireBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void closeAccount(ArrayList<BankAccount> bankAccounts) {
        if (balance > 0) {
            System.out.println("Balance must be withdrawn first");
        }
        else {
            System.out.println("❌ There is no balance to withdraw");
        }
        bankAccounts.remove(this);
        status = "Closed";
        System.out.println("This account has been closed");
    }

    public String displayAccountType() {
        return this.getClass().toString();
    }

    public String toString() {
        return accountName + "\n#" + accountNo + "\nStatus: " + status;
    }
}