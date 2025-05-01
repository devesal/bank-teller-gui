package model;

import util.Exceptions.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a bank account with a unique 9-digit account number,
 * account holder information, balance, and status (Active/Closed).
 * Supports deposits, withdrawals, money transfers, and account status changes.
 */
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

    /** Identifier linking this account to a customer record */
    public String customerId;

    /**
     * Constructs a new active BankAccount with a random 9-digit account number
     * and the given first and last name. Initial balance is set to zero.
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
     * @return a randomly generated 9-digit integer
     */
    private int generateAccountNumber() {
        return 100_000_000 + RANDOM.nextInt(900_000_000);
    }

    /**
     * Retrieves the account number.
     *
     * @return the 9-digit account number
     */
    public int getAccountNo() {
        return accountNo;
    }

    /**
     * Retrieves the first name of the account holder.
     *
     * @return the account holder's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Updates the first name of the account holder.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last name of the account holder.
     *
     * @return the account holder's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Updates the last name of the account holder.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the current status of the account.
     *
     * @return "Active" if open, "Closed" if closed
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the status of the account.
     *
     * @param status new status value (e.g., "Active", "Closed")
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Deposits a specified amount into the account. Only allowed if the account is active.
     *
     * @param amount the amount to deposit
     * @throws AccountClosedException if the account is closed
     */
    public void deposit(double amount) throws AccountClosedException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Cannot deposit to a closed account.");
        }
        this.balance += amount;
    }

    /**
     * Withdraws a specified amount from the account. Only allowed if the account is active,
     * sufficient funds are available, and within any transaction limits.
     *
     * @param amount the amount to withdraw
     * @throws AccountClosedException      if the account is closed
     * @throws InsufficientFundsException  if the balance is insufficient
     * @throws TransactionLimitException   if the withdrawal exceeds allowed limits
     */
    public void withdraw(double amount) throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Cannot withdraw from a closed account.");
        }
        if (balance >= amount) {
            this.balance -= amount;
        } else {
            throw new InsufficientFundsException("Insufficient balance. Transaction terminated");
        }
    }

    /**
     * Retrieves the current balance of the account.
     *
     * @return the current balance
     */
    public double inquireBalance() {
        return balance;
    }

    /**
     * Transfers a specified amount from this account to another account in the list.
     * Only allowed if both accounts are active and sufficient funds are available.
     *
     * @param AccountNo    the target account number
     * @param amount       the amount to transfer
     * @param bankAccounts list of all bank accounts to search for the recipient
     * @throws AccountClosedException if the source account is closed
     * @throws InvalidAmountException if the amount is invalid or exceeds balance
     */
    public void transferMoney(int AccountNo, double amount, ArrayList<BankAccount> bankAccounts) throws AccountClosedException, InvalidAmountException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Cannot transfer from a closed account.");
        }
        if (amount > balance) {
            throw new InvalidAmountException("Invalid amount. Transaction terminated");
        }
        for (BankAccount recipient : bankAccounts) {
            if (recipient.getAccountNo() == AccountNo) {
                this.balance -= amount;
                recipient.deposit(amount);
                return;
            }
        }
    }

    /**
     * Closes the account by withdrawing any remaining balance and setting status to Closed.
     * Only allowed if the account is currently active.
     *
     * @throws AccountClosedException      if the account is already closed
     * @throws InsufficientFundsException  if an error occurs withdrawing remaining balance
     * @throws TransactionLimitException   if withdrawal exceeds allowed limits
     */
    public void closeAccount() throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        if (!"Active".equals(status)) {
            throw new AccountClosedException("Account is already closed.");
        }
        if (balance > 0) {
            withdraw(balance);
        } else {
            System.out.println("There is no balance to withdraw");
        }
        setStatus("Closed");
    }

    /**
     * Reopens a closed account by setting its status back to Active.
     * If the account is already active, no action is taken.
     */
    public void reopenAccount() {
        if ("Active".equals(status)) {
            System.out.println("❌ Account is already active.");
            return;
        }
        setStatus("Active");
    }

    /**
     * Displays the type of the account. Currently returns "Savings Account".
     *
     * @return a string describing the account type
     */
    public String displayAccountType() {
        return "Savings Account";
    }

    /**
     * Assigns a customer identifier to this account.
     *
     * @param customerId the external customer ID to associate
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Retrieves the associated customer identifier.
     *
     * @return the customer ID linked to this account
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Returns a string representation of the account, including holder name, account number, and status.
     *
     * @return formatted account summary
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + "\n#" + accountNo + "\nStatus: " + status;
    }
}
