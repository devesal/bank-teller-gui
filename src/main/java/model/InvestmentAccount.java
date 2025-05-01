package model;

import util.Exceptions.AccountClosedException;
import util.Exceptions.InsufficientFundsException;
import util.Exceptions.TransactionLimitException;

import java.io.Serializable;

public class InvestmentAccount extends BankAccount implements Serializable {
    /** The minimum balance that must be maintained in the account */
    private final double minimumBalance;
    /** The annual interest rate applied to the investment (as decimal) */
    private final double interestRate;

    /**
     * Constructs a new investment account with zero minimum balance and zero interest.
     *
     * @param firstName the account holder's first name
     * @param lastName the account holder's last name
     */
    public InvestmentAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.minimumBalance = 0.0;
        this.interestRate = 0.0;
    }

    /**
     * Constructs a new investment account with specified minimum balance and interest rate.
     *
     * @param firstName the account holder's first name
     * @param lastName the account holder's last name
     * @param minimumBalance the minimum balance requirement
     * @param interestRate the annual interest rate (e.g. 0.05 for 5%)
     */
    public InvestmentAccount(String firstName, String lastName, double minimumBalance, double interestRate) {
        super(firstName, lastName);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    /**
     * Gets the minimum balance requirement for this account.
     *
     * @return the minimum balance that must be maintained
     */
    public double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * Gets the annual interest rate of this account.
     *
     * @return the interest rate as a decimal
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Adds funds to the investment via deposit.
     *
     * @param amount the amount to invest
     */
    public void addInvestment(double amount) throws AccountClosedException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("❌ Cannot invest to a closed account.");
        }
        super.deposit(amount);
    }

    /**
     * Calculates the total value of the investment including accrued interest.
     *
     * @return the investment value = principal * (1 + interestRate)
     */
    public void applyMonthlyInterest() throws AccountClosedException {
        if (!"Active".equals(getStatus())) return;
        double monthlyRate = interestRate / 12.0;
        double currentBalance = super.inquireBalance() + minimumBalance;
        double interest = currentBalance * monthlyRate;
        super.deposit(interest); // Add earned interest to balance
    }

    public double calculateEarnedInterest() {
        double monthlyRate = interestRate / 12.0;
        return super.inquireBalance() * monthlyRate;
    }

    public double inquireInvestmentValue() {
        return super.inquireBalance()+minimumBalance;
    }

    /**
     * Closes the investment account, withdrawing principal + interest if above minimum.
     * The account remains in list but becomes inactive.
     */
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

    /**
     * Returns the type of account as a string.
     *
     * @return a string indicating this is an investment account
     */
    @Override
    public String displayAccountType() {
        return "Investment Account";
    }

    @Override
    public double inquireBalance() {
        return super.inquireBalance() + minimumBalance; // Now reflects actual balance including monthly compound
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nMinimum Balance: ₱" + minimumBalance +
                "\nInterest Rate: " + (interestRate * 100) + "%";
    }
}
