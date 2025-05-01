package model;

import util.Exceptions.AccountClosedException;
import util.Exceptions.InsufficientFundsException;
import util.Exceptions.TransactionLimitException;

import java.io.Serializable;

/**
 * Represents an investment account within the Co-Pals Bank System.
 * Investment accounts earn interest and require a minimum balance to remain active.
 * This class extends {@link BankAccount} and includes interest-related operations.
 *
 * Created on: 3/21/2025
 *
 * @version 1.1
 * @author Aquino, Theo James Coroneza
 * @author Arellano, Clendrick Joshua Mangonon
 * @author Mangonon, John Cedrick Garcia
 * @author Ong, Ron Miguel Cau
 * @author Ramos, Ricky Marc Salazar
 * @author Rosana, Jeaven Vincent Yojan Operia
 */
public class InvestmentAccount extends BankAccount implements Serializable {

    /** The minimum balance that must be maintained in the account */
    private final double minimumBalance;

    /** The annual interest rate applied to the investment (as decimal) */
    private final double interestRate;

    /**
     * Constructs a new investment account with zero minimum balance and zero interest.
     *
     * @param firstName the account holder's first name
     * @param lastName  the account holder's last name
     */
    public InvestmentAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.minimumBalance = 0.0;
        this.interestRate = 0.0;
    }

    /**
     * Constructs a new investment account with specified minimum balance and interest rate.
     *
     * @param firstName      the account holder's first name
     * @param lastName       the account holder's last name
     * @param minimumBalance the minimum balance requirement
     * @param interestRate   the annual interest rate (e.g. 0.05 for 5%)
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
     * @return the interest rate as a decimal (e.g., 0.05 for 5%)
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Adds funds to the investment account.
     *
     * @param amount the amount to invest
     * @throws AccountClosedException if the account is closed
     */
    public void addInvestment(double amount) throws AccountClosedException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("❌ Cannot invest in a closed account.");
        }
        super.deposit(amount);
    }

    /**
     * Applies monthly interest to the account balance.
     *
     * @throws AccountClosedException if the account is closed
     */
    public void applyMonthlyInterest() throws AccountClosedException {
        if (!"Active".equals(getStatus())) return;
        double monthlyRate = interestRate / 12.0;
        double currentBalance = super.inquireBalance() + minimumBalance;
        double interest = currentBalance * monthlyRate;
        super.deposit(interest); // Add earned interest to balance
    }

    /**
     * Calculates the interest earned for the current month.
     *
     * @return the monthly earned interest
     */
    public double calculateEarnedInterest() {
        double monthlyRate = interestRate / 12.0;
        return (super.inquireBalance()+ minimumBalance) * monthlyRate;
    }

    /**
     * Returns the total investment value (balance + minimum).
     *
     * @return the total investment value
     */
    public double inquireInvestmentValue() {
        return super.inquireBalance() + minimumBalance;
    }

    /**
     * Closes the investment account and withdraws all available funds
     * if the balance is above the minimum requirement.
     *
     * @throws InsufficientFundsException if withdrawal conditions aren't met
     * @throws AccountClosedException     if the account is already closed
     * @throws TransactionLimitException  if withdrawal exceeds allowed limits
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
     * Returns the type of the account.
     *
     * @return the string "Investment Account"
     */
    @Override
    public String displayAccountType() {
        return "Investment Account";
    }

    /**
     * Gets the current balance including the minimum balance.
     *
     * @return the full balance value of the investment
     */
    @Override
    public double inquireBalance() {
        return super.inquireBalance() + minimumBalance;
    }

    /**
     * Returns a string representation of the investment account details.
     *
     * @return a formatted string with account holder and investment info
     */
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nMinimum Balance: ₱" + minimumBalance +
                "\nInterest Rate: " + (interestRate * 100) + "%";
    }
}
