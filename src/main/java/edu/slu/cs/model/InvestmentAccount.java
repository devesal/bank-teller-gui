package edu.slu.cs.model;

/**
 * Represents an investment account in the Co-Pals Bank System.
 * This account type provides investment functionality with interest accrual
 * and minimum balance requirements. The account earns interest on the invested
 * amount and requires maintaining a minimum balance.
 *
 * Created on: 3/21/2025
 *
 * @author Aquino, Theo James Coroneza
 * @author Arellano, Theo James Coroneza
 * @author Mangonon, John Cedrick Garcia
 * @author Ong, Ron Miguel Cau
 * @author Ramos, Ricky Marc Salazar
 * @author Rosana, Jeaven Vincent Yojan Operia
 * @version 1.1
 */
public class InvestmentAccount extends BankAccount {

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
    public void addInvestment(double amount) {
        if (!"Active".equals(getStatus())) {
            System.out.println("❌ Cannot invest to a closed account.");
            return;
        }
        super.deposit(amount);
    }

    /**
     * Calculates the total value of the investment including accrued interest.
     *
     * @return the investment value = principal * (1 + interestRate)
     */
    public double inquireInvestmentValue() {
        double principal = super.inquireBalance();
        return principal * (1 + interestRate);
    }

    /**
     * Calculates accrued interest on current balance.
     *
     * @return the amount of interest earned
     */
    public double calculateEarnedInterest() {
        double principal = super.inquireBalance();
        return principal * interestRate;
    }

    /**
     * Closes the investment account, withdrawing principal + interest if above minimum.
     * The account remains in list but becomes inactive.
     */
    @Override
    public void closeAccount() {
        if (!"Active".equals(getStatus())) {
            System.out.println("❌ Account is already closed.");
            return;
        }
        double principal = super.inquireBalance();
        double total = principal * (1 + interestRate);
        if (principal >= minimumBalance) {
            System.out.println("Withdrawing invested amount of ₱" + String.format("%.2f", total));
            System.out.println("Earned interest: ₱" + String.format("%.2f", calculateEarnedInterest()));
            // empty the balance
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

    /**
     * Provides a string representation of the account details.
     */
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nMinimum Balance: ₱" + minimumBalance +
                "\nInterest Rate: " + (interestRate * 100) + "%";
    }
}
