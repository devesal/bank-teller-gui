package model;

import model.BankAccount;
import util.Exceptions.AccountClosedException;
import util.Exceptions.InsufficientFundsException;
import util.Exceptions.TransactionLimitException;

import java.util.ArrayList;

public class CreditCardAccount extends BankAccount {

    private double creditLimit;
    private double charges;

    public CreditCardAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.creditLimit = 100_000;
        this.charges = 0.0;
    }


    public CreditCardAccount(String firstName, String lastName, double creditLimit) {
        super(firstName, lastName);
        this.creditLimit = creditLimit;
        this.charges = 0.0;
    }


    public double getCreditLimit() {
        return creditLimit;
    }


    @Override
    public double inquireBalance() {
        return inquireAvailableCredit();
    }

    public double getCharges() {
        return charges;
    }


    public void chargeToCard(double amount) throws AccountClosedException, TransactionLimitException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("Cannot charge to a closed account.");
        }
        double available = creditLimit - charges;
        if (available >= amount) {
            charges += amount;
        } else {
            throw new TransactionLimitException("Not enough credit. Available: \u20b1" + available);
        }
    }


    public void payCard(double amount) throws AccountClosedException, TransactionLimitException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("Cannot pay to a closed account.");
        }
        if (amount > charges) {
            throw new TransactionLimitException("Payment exceeds total charges");
        } else {
            charges -= amount;
            System.out.println("Payment successful! Remaining balance: \u20b1" + charges);
        }
    }


    public double inquireAvailableCredit() {
        double available = creditLimit - charges;
        System.out.println("Your available credit is: \u20b1" + available);
        return available;
    }


    public void getCashAdvance(double amount) throws AccountClosedException, TransactionLimitException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("Cannot advance to a closed account.");
        }
        double available = (creditLimit - charges) * 0.5;
        if (amount <= available) {
            charges += amount;
        } else {
            throw new TransactionLimitException("Transaction declined: Requested advance exceeds \u20b1" + available);
        }
    }


    @Override
    public void closeAccount() throws InsufficientFundsException, AccountClosedException, TransactionLimitException {
        if (!"Active".equals(getStatus())) {
            System.out.println("Account is already closed.");
            return;
        }
        if (charges == 0) {
            super.closeAccount();
        } else {
            System.out.println("Please settle your remaining balance before closing your account");
        }

    }


    @Override
    public String displayAccountType() {
        return "Credit Card Account";
    }


    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nCredit Limit: \u20b1" + creditLimit +
                "\nCharges: \u20b1" + charges;
    }
}
