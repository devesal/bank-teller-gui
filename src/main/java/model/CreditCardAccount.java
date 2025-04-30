package model;

import model.BankAccount;

import java.util.ArrayList;

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
     * @param lastName the account holder's last name
     */
    public CreditCardAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.creditLimit = 100_000;
        this.charges = 0.0;
    }

    /**
     * Constructs a new credit card account with specified credit limit.
     *
     * @param firstName the account holder's first name
     * @param lastName the account holder's last name
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
     */
    public void chargeToCard(double amount) {
        if (!"Active".equals(getStatus())) {
            System.out.println("❌ Cannot charge a closed account.");
            return;
        }
        double available = creditLimit - charges;
        if (available >= amount) {
            charges += amount;
            System.out.println("Charge successful! New balance: ₱" + charges);
        } else {
            System.out.println("❌ Not enough credit. Available: ₱" + available);
        }
    }

    /**
     * Makes a payment towards the credit card balance.
     * Payment cannot exceed the current charges.
     *
     * @param amount The amount to pay
     */
    public void payCard(double amount) {
        if (!"Active".equals(getStatus())) {
            System.out.println("❌ Cannot pay a closed account.");
            return;
        }
        if (amount > charges) {
            System.out.println("❌ Payment exceeds total charges");
        } else {
            charges -= amount;
            System.out.println("Payment successful! Remaining balance: ₱" + charges);
        }
    }

    /**
     * Displays the available credit on the account.
     * Available credit is calculated as credit limit minus current charges.
     *
     * @return
     */
    public double inquireAvailableCredit() {
        double available = creditLimit - charges;
        System.out.println("Your available credit is: ₱" + available);
        return available;
    }

    /**
     * Gets a cash advance from the credit card account.
     * Cash advance is limited to 50% of available credit.
     *
     * @param amount The amount of cash advance requested
     */
    public void getCashAdvance(double amount) {
        if (!"Active".equals(getStatus())) {
            System.out.println("❌ Cannot advance from a closed account.");
            return;
        }
        double available = (creditLimit - charges) * 0.5;
        if (amount <= available) {
            charges += amount;
            System.out.println("Cash advance approved! Charged: ₱" + amount);
        } else {
            System.out.println("❌ Transaction declined: Requested advance exceeds ₱" + available);
        }
    }

    /**
     * Closes the credit card account if all charges are paid.
     * Account remains in list but becomes inactive.
     */
    @Override
    public void closeAccount() {
        if (!"Active".equals(getStatus())) {
            System.out.println("❌ Account is already closed.");
            return;
        }
        if (charges == 0) {
            super.closeAccount();
        } else {
            System.out.println("❌ Please settle your remaining balance before closing your account");
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
     */
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nCredit Limit: ₱" + creditLimit +
                "\nCharges: ₱" + charges;
    }
}
