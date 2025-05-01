/**
 * Represents a bank account with basic operations such as deposit, withdrawal,
 * balance inquiry, money transfer, closing, and reopening.
 *
 * Each account has a unique 9-digit account number, associated customer name,
 * balance, and status (Active or Closed).
 *
 */
package model;

import util.Exceptions.AccountClosedException;
import util.Exceptions.InsufficientFundsException;
import util.Exceptions.InvalidAmountException;
import util.Exceptions.TransactionLimitException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class BankAccount implements Serializable {

    /** Random generator for account numbers */
    private static final Random RANDOM = new Random();

    /** Unique 9-digit account identifier */
    private final int accountNo;

    /** First name of the account holder */
    private String firstName;

    /** Last name of the account holder */
    private String lastName;

    /** Current balance in the account */
    private double balance;

    /** Current status of the account (Active/Closed) */
    private String status;

    /**
     * Default constructor for creating an empty BankAccount object with a generated account number.
     */
    public BankAccount() {
        this.accountNo = generateAccountNumber();
        this.firstName = "";
        this.lastName = "";
    }

    /**
     * Constructs a new active BankAccount with a random 9-digit account number
     * and given first and last name.
     *
     * @param firstName the account holder's first name
     * @param lastName  the account holder's last name
     */
    public BankAccount(String firstName, String lastName) {
        this.accountNo = generateAccountNumber();
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = 0.0;
        this.status = "Active";
    }

    /**
     * Generates a random 9-digit account number between 100,000,000 and 999,999,999.
     *
     * @return a randomly generated 9-digit account number
     */
    private int generateAccountNumber() {
        return 100_000_000 + RANDOM.nextInt(900_000_000);
    }

    /** @return the account number */
    public int getAccountNo() {
        return accountNo;
    }

    /** @return the first name of the account holder */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the account holder.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** @return the last name of the account holder */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the account holder.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** @return the current account status */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the account (e.g., Active, Closed).
     *
     * @param status the new account status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to deposit
     * @throws AccountClosedException if the account is closed
     */
    public void deposit(double amount) throws AccountClosedException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("❌ Cannot deposit to a closed account.");
        }
        this.balance += amount;
    }

    /**
     * Withdraws the specified amount from the account.
     *
     * @param amount the amount to withdraw
     * @throws AccountClosedException       if the account is closed
     * @throws InsufficientFundsException   if the balance is insufficient
     * @throws TransactionLimitException    reserved for future transaction restrictions
     */
    public void withdraw(double amount) throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("❌ Cannot withdraw to a closed account.");
        }
        if (balance >= amount) {
            this.balance -= amount;
        } else {
            throw new InsufficientFundsException("❌ Insufficient balance. Transaction terminated");
        }
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the current account balance
     */
    public double inquireBalance() {
        return balance;
    }

    /**
     * Transfers money from this account to another account with the given account number.
     *
     * @param AccountNo     the recipient's account number
     * @param amount        the amount to transfer
     * @param bankAccounts  the list of all bank accounts for lookup
     * @throws AccountClosedException if the sender account is closed
     * @throws InvalidAmountException if the amount is greater than the balance
     */
    public void transferMoney(int AccountNo, double amount, ArrayList<BankAccount> bankAccounts) throws AccountClosedException, InvalidAmountException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("❌ Cannot transfer to a closed account.");
        }
        if (amount > balance) {
            throw new InvalidAmountException("❌ Invalid amount. Transaction terminated");
        }
        for (BankAccount recipient : bankAccounts) {
            if (recipient.getAccountNo() == AccountNo) {
                this.balance -= amount;
                recipient.deposit(amount);
                return;
            }
        }
        System.out.println("❌ Account not found");
    }

    /**
     * Closes the account. Withdraws all remaining balance and sets the status to Closed.
     *
     * @throws AccountClosedException     if the account is already closed
     * @throws InsufficientFundsException if withdrawal fails due to insufficient funds
     * @throws TransactionLimitException  reserved for future constraints
     */
    public void closeAccount() throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("❌ Account is already closed.");
        }
        if (balance > 0) {
            withdraw(balance);
        } else {
            System.out.println("❌ There is no balance to withdraw");
        }
        setStatus("Closed");
        System.out.println("This account has been closed. You can reopen it using the original account number.");
    }

    /**
     * Reopens a previously closed account by setting the status to Active.
     */
    public void reopenAccount() {
        if ("Active".equals(status)) {
            System.out.println("❌ Account is already active.");
            return;
        }
        setStatus("Active");
        System.out.println("Account #" + accountNo + " has been reopened.");
    }

    /**
     * Returns the account type as a string.
     *
     * @return the account type (default is "Savings Account")
     */
    public String displayAccountType() {
        return "Savings Account";
    }

    /**
     * Returns a string representation of the account, including name, account number, and status.
     *
     * @return a string representing the account
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + "\n#" + accountNo + "\nStatus: " + status;
    }
}