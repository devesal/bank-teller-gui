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
 * @version 2.1
 */

import util.Exceptions.*;
import java.io.Serializable;

/**
 * A specialized type of bank account with check-related features and minimum balance enforcement.
 */
public class CheckingAccount extends BankAccount implements Serializable {

    /** The minimum balance that must be maintained in the account */
    private final double minimumBalance;

    /**
     * Constructs a new checking account with a default minimum balance of 500.0.
     * Uses the superclass constructor to set the account holder's name and account number.
     *
     * @param firstName the account holder's first name
     * @param lastName  the account holder's last name
     */
    public CheckingAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.minimumBalance = 500.0;
    }

    /**
     * Constructs a new checking account with a specified minimum balance.
     *
     * @param firstName      the account holder's first name
     * @param lastName       the account holder's last name
     * @param minimumBalance the minimum balance requirement for this account
     */
    public CheckingAccount(String firstName, String lastName, double minimumBalance) {
        super(firstName, lastName);
        this.minimumBalance = minimumBalance;
    }

    /**
     * Returns the account's effective balance, which includes the minimum balance requirement.
     *
     * @return the total effective balance including the minimum balance
     */
    @Override
    public double inquireBalance() {
        return super.inquireBalance() + minimumBalance;
    }

    /**
     * Withdraws a specified amount from the account, ensuring the balance does not drop below the minimum.
     *
     * @param amount the amount to withdraw
     * @throws InsufficientFundsException    if funds are not sufficient
     * @throws AccountClosedException        if the account is closed
     * @throws TransactionLimitException     if withdrawal would drop balance below minimum
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException, AccountClosedException, TransactionLimitException {
        if (!"Active".equals(getStatus())) {
            throw new AccountClosedException("Cannot withdraw to a closed account.");
        }
        double currentBalance = inquireBalance();
        if (amount > currentBalance) {
            throw new InsufficientFundsException("Insufficient balance. Transaction terminated");
        }
        if ((currentBalance - amount) < minimumBalance) {
            throw new TransactionLimitException("Withdrawal denied: Balance cannot go below \u20b1" + minimumBalance);
        }
        super.withdraw(amount);
    }

    /**
     * Processes a check encashment request, enforcing minimum balance requirements.
     *
     * @param amount the amount to encash
     * @throws InsufficientFundsException    if the funds are insufficient
     * @throws AccountClosedException        if the account is closed
     * @throws TransactionLimitException     if encashment would drop balance below minimum
     */
    public void encashCheck(double amount) throws InsufficientFundsException, AccountClosedException, TransactionLimitException {
        double currentBalance = inquireBalance();
        if (amount > currentBalance) {
            throw new InsufficientFundsException("Insufficient funds to encash \u20b1" + amount);
        }
        if ((currentBalance - amount) >= minimumBalance) {
            System.out.println("Encashing \u20b1" + amount + " successful");
            super.withdraw(amount);
        } else {
            throw new TransactionLimitException("Encashment denied: Balance cannot go below \u20b1" + minimumBalance);
        }
    }

    /**
     * Displays the type of this account.
     *
     * @return a string identifying this as a checking account
     */
    @Override
    public String displayAccountType() {
        return "Checking Account";
    }
}