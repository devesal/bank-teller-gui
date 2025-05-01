package model;

import util.Exceptions.AccountClosedException;
import util.Exceptions.InsufficientFundsException;
import util.Exceptions.TransactionLimitException;

import java.io.Serializable;

public class InvestmentAccount extends BankAccount implements Serializable {

    private final double minimumBalance;

    private final double interestRate;

    public InvestmentAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.minimumBalance = 0.0;
        this.interestRate = 0.0;
    }


    public InvestmentAccount(String firstName, String lastName, double minimumBalance, double interestRate) {
        super(firstName, lastName);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }


    public double getMinimumBalance() {
        return minimumBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void addInvestment(double amount) throws AccountClosedException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("❌ Cannot invest in a closed account.");
        }
        super.deposit(amount);
    }


    public void applyMonthlyInterest() throws AccountClosedException {
        if (!"Active".equals(getStatus())) return;
        double monthlyRate = interestRate / 12.0;
        double currentBalance = super.inquireBalance() + minimumBalance;
        double interest = currentBalance * monthlyRate;
        super.deposit(interest); // Add earned interest to balance
    }


    public double calculateEarnedInterest() {
        double monthlyRate = interestRate / 12.0;
        return (super.inquireBalance()+ minimumBalance) * monthlyRate;
    }


    public double inquireInvestmentValue() {
        return super.inquireBalance() + minimumBalance;
    }


    @Override
    public void closeAccount() throws InsufficientFundsException, AccountClosedException, TransactionLimitException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("❌ Account is already closed.");
        }

        double principal = super.inquireBalance();
        double total = principal * (1 + interestRate);

        if (principal >= minimumBalance) {
            System.out.println("Withdrawing invested amount of ₱" + String.format("%.2f", principal));
            System.out.println("Last month’s interest earned: ₱" + String.format("%.2f", calculateEarnedInterest()));
            super.withdraw(principal);
            setStatus("Closed");
            System.out.println("Investment account closed.");
        } else {
            System.out.println("❌ Cannot close: balance ₱" + String.format("%.2f", principal) +
                    " is below minimum ₱" + minimumBalance);
        }
    }


    @Override
    public String displayAccountType() {
        return "Investment Account";
    }


    @Override
    public double inquireBalance() {
        return super.inquireBalance() + minimumBalance;
    }


    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nMinimum Balance: ₱" + minimumBalance +
                "\nInterest Rate: " + (interestRate * 100) + "%";
    }
}
