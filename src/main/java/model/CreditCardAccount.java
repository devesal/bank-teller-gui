package model;

import model.BankAccount;
import util.Exceptions.AccountClosedException;
import util.Exceptions.InsufficientFundsException;
import util.Exceptions.TransactionLimitException;

import java.util.ArrayList;

/**
 * Represents a credit card account in the Co-Pals Bank System.
 * This account type allows for charging, payment, and cash advance functionalities,
 * with enforcement of a credit limit.
 *
 * Created on: 3/21/2025
 *
 * @version 2.1
 * @author Aquino, Theo James Coroneza
 * @author Arellano, Clendrick Joshua Mangonon
 * @author Mangonon, John Cedrick Garcia
 * @author Ong, Ron Miguel Cau
 * @author Ramos, Ricky Marc Salazar
 * @author Rosana, Jeaven Vincent Yojan Operia
 */
public class CreditCardAccount extends BankAccount {

    /** The maximum credit limit for this account */
    private double creditLimit;
    /** The current charges/balance on the credit card */
    private double charges;

    /**
     * Constructs a new credit card account with default credit limit of 100,000 and zero charges.
     * Uses superclass constructor to generate account number and set names.
     *
     * @param firstName the account holder's first name
     * @param lastName  the account holder's last name
     */
    public CreditCardAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.creditLimit = 100_000;
        this.charges = 0.0;
    }

    /**
     * Constructs a new credit card account with specified credit limit.
     *
     * @param firstName   the account holder's first name
     * @param lastName    the account holder's last name
     * @param creditLimit the maximum credit limit for this account
     */
    public CreditCardAccount(String firstName, String lastName, double creditLimit) {
        super(firstName, lastName);
        this.creditLimit = creditLimit;
        this.charges = 0.0;
    }

    /**
     * Gets the credit limit of the account.
     *
     * @return The maximum credit limit
     */
    public double getCreditLimit() {
        return creditLimit;
    }

    /**
     * Returns the currently available credit balance.
     *
     * @return the available credit (credit limit minus charges)
     */
    @Override
    public double inquireBalance() {
        return inquireAvailableCredit();
    }

    /**
     * Gets the current charges on the account.
     *
     * @return The current charges/balance
     */
    public double getCharges() {
        return charges;
    }

    /**
     * Applies a new charge to the credit card if sufficient credit is available.
     *
     * @param amount The amount to charge
     * @throws AccountClosedException    if the account is closed
     * @throws TransactionLimitException if the charge exceeds the available credit
     */
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

    /**
     * Makes a payment towards the credit card balance.
     * Payment cannot exceed the current charges.
     *
     * @param amount The amount to pay
     * @throws AccountClosedException    if the account is closed
     * @throws TransactionLimitException if payment exceeds current charges
     */
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

    /**
     * Displays the available credit on the account.
     * Available credit is calculated as credit limit minus current charges.
     *
     * @return the available credit
     */
    public double inquireAvailableCredit() {
        double available = creditLimit - charges;
        System.out.println("Your available credit is: \u20b1" + available);
        return available;
    }

    /**
     * Gets a cash advance from the credit card account.
     * Cash advance is limited to 50% of available credit.
     *
     * @param amount The amount of cash advance requested
     * @throws AccountClosedException    if the account is closed
     * @throws TransactionLimitException if requested amount exceeds 50% of available credit
     */
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

    /**
     * Closes the credit card account if all charges are paid.
     * Account remains in list but becomes inactive.
     *
     * @throws InsufficientFundsException if base account cannot handle closing
     * @throws AccountClosedException     if account is already closed
     * @throws TransactionLimitException  if base account logic encounters limits
     */
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

    /**
     * Returns the type of account as a string.
     *
     * @return A string indicating this is a credit card account
     */
    @Override
    public String displayAccountType() {
        return "Credit Card Account";
    }

    /**
     * Provides a string representation of the account details.
     *
     * @return A formatted string of the account's state
     */
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nCredit Limit: \u20b1" + creditLimit +
                "\nCharges: \u20b1" + charges;
    }
}
