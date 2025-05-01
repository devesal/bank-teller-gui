package model;

import util.Exceptions.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;


public class BankAccount implements Serializable {

    private static final Random RANDOM = new Random();
    private final int accountNo;
    private String firstName;
    private String lastName;
    private double balance;
    private String status;
    public String customerId;

    public BankAccount(String firstName, String lastName) {
        this.accountNo = generateAccountNumber();
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = 0.0;
        this.status = "Active";
    }


    private int generateAccountNumber() {
        return 100_000_000 + RANDOM.nextInt(900_000_000);
    }


    public int getAccountNo() {
        return accountNo;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public void deposit(double amount) throws AccountClosedException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Cannot deposit to a closed account.");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Cannot withdraw from a closed account.");
        }
        if (balance >= amount) {
            this.balance -= amount;
        } else {
            throw new InsufficientFundsException("Insufficient balance. Transaction terminated");
        }
    }


    public double inquireBalance() {
        return balance;
    }


    public void transferMoney(int AccountNo, double amount, ArrayList<BankAccount> bankAccounts) throws AccountClosedException, InvalidAmountException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Cannot transfer from a closed account.");
        }
        if (amount > balance) {
            throw new InvalidAmountException("Invalid amount. Transaction terminated");
        }
        for (BankAccount recipient : bankAccounts) {
            if (recipient.getAccountNo() == AccountNo) {
                this.balance -= amount;
                recipient.deposit(amount);
                return;
            }
        }
    }


    public void closeAccount() throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Account is already closed.");
        }
        if (balance > 0) {
            withdraw(balance);
        } else {
            System.out.println("There is no balance to withdraw");
        }
        setStatus("Closed");
    }


    public void reopenAccount() {
        if ("Active".equals(status)) {
            System.out.println("❌ Account is already active.");
            return;
        }
        setStatus("Active");
    }


    public String displayAccountType() {
        return "Savings Account";
    }


    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public String getCustomerId() {
        return customerId;
    }


    @Override
    public String toString() {
        return firstName + " " + lastName + "\n#" + accountNo + "\nStatus: " + status;
    }
}
