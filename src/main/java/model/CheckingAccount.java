package model;
/**
 * Represents a checking account in the Co-Pals Bank System.
 * This account type maintains a minimum balance requirement and provides check encashment functionality.
 * Extends the base BankAccount class to provide specialized checking account operations.
 *
 * Created on: 3/21/2025
 *
 * @author Aquino, Theo James Coroneza
 * @author Arellano, Clendrick Joshua Mangonon
 * @author Mangonon, John Cedrick Garcia
 * @author Ong, Ron Miguel Cau
 * @author Ramos, Ricky Marc Salazar
 * @author Rosana, Jeaven Vincent Yojan Operia
 * @version 1.1
 */
import util.Exceptions.*;


import java.io.Serializable;
public class CheckingAccount extends BankAccount implements Serializable{
    /** The minimum balance that must be maintained in the account */
    private final double minimumBalance;

    /**
     * Constructs a new checking account with default minimum balance of 500.0.
     * Uses superclass constructor to generate account number and set names.
     *
     * @param firstName the account holder's first name
     * @param lastName the account holder's last name
     */
    public CheckingAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.minimumBalance = 500.0;
    }

    /**
     * Constructs a new checking account with specified minimum balance.
     *
     * @param firstName the account holder's first name
     * @param lastName the account holder's last name
     * @param minimumBalance the minimum balance requirement for this account
     */
    public CheckingAccount(String firstName, String lastName, double minimumBalance) {
        super(firstName, lastName);
        this.minimumBalance = minimumBalance;
    }

    /**
     * Gets the minimum balance requirement for this account.
     *
     * @return The minimum balance that must be maintained
     */
    public double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * Overrides withdraw to ensure that minimum balance requirements are met.
     *
     * @param amount the amount to withdraw
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException, AccountClosedException, TransactionLimitException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("❌ Cannot withdraw to a closed account.");
        }
        double currentBalance = inquireBalance();
        if (amount > currentBalance) {
            throw new InsufficientFundsException("❌ Insufficient balance. Transaction terminated");
        }
        if ((currentBalance - amount) < minimumBalance) {
            throw new TransactionLimitException("❌ Withdrawal denied: Balance cannot go below ₱" + minimumBalance);
        }
        super.withdraw(amount);
    }

    /**
     * Processes a check encashment request while ensuring minimum balance requirements are met.
     *
     * @param amount The amount to be encashed from the check
     */
    public void encashCheck(double amount) throws InsufficientFundsException, AccountClosedException, TransactionLimitException {
        // encashCheck follows same rules as withdraw but with its own messaging
        double currentBalance = inquireBalance();
        if (amount > currentBalance) {
            throw new InsufficientFundsException("❌ Insufficient funds to encash ₱" + amount);
        }
        if ((currentBalance - amount) >= minimumBalance) {
            System.out.println("Encashing ₱" + amount + " successful");
            super.withdraw(amount);
        } else {
            throw new TransactionLimitException("❌ Encashment denied: Balance cannot go below ₱" + minimumBalance);
        }
    }

    /**
     * Returns the type of account as a string.
     *
     * @return A string indicating this is a checking account
     */
    @Override
    public String displayAccountType() {
        return "Checking Account";
    }
}