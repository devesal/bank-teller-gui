package model;

import java.util.UUID;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a customer in the Co-Pals Bank System.
 * A customer can own multiple bank accounts and is identified by a unique ID.
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
public class Customer implements Serializable {

    /** Customer's first name */
    private String firstName;

    /** Customer's last name */
    private String lastName;

    /** Customer's birth date */
    private String BirthDate;

    /** List of bank accounts owned by the customer */
    private List<BankAccount> accounts;

    /** Unique identifier for the customer */
    private final String id = UUID.randomUUID().toString();

    /**
     * Constructs a new Customer with the given personal information.
     *
     * @param firstName the customer's first name
     * @param lastName the customer's last name
     * @param BirthDate the customer's birth date
     */
    public Customer(String firstName, String lastName, String BirthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.BirthDate = BirthDate;
        this.accounts = new ArrayList<>();
    }

    /**
     * Gets the customer's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the customer's first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        if (!firstName.isEmpty()) {
            this.firstName = firstName;
        }
    }

    /**
     * Gets the customer's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the customer's last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        if (!lastName.isEmpty()) {
            this.lastName = lastName;
        }
    }

    /**
     * Gets the customer's birth date.
     *
     * @return the birth date
     */
    public String getBirthDate() {
        return BirthDate;
    }

    /**
     * Sets the customer's birth date.
     *
     * @param BirthDate the new birth date
     */
    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }

    /**
     * Adds a new bank account to the customer's list of accounts.
     *
     * @param account the account to be added
     */
    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    /**
     * Removes a bank account based on account number.
     *
     * @param accountNo the account number to be removed
     */
    public void removeAccount(int accountNo) {
        accounts.removeIf(a -> a.getAccountNo() == accountNo);
    }

    /**
     * Retrieves a specific account by account number.
     *
     * @param accountNo the account number to search for
     * @return the matching BankAccount, or null if not found
     */
    public BankAccount getAccount(int accountNo) {
        return accounts.stream()
                .filter(a -> a.getAccountNo() == accountNo)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a list of all accounts owned by the customer.
     *
     * @return a list of BankAccount objects
     */
    public List<BankAccount> getAccounts() {
        return new ArrayList<>(accounts);
    }

    /**
     * Returns a list of accounts filtered by account type.
     *
     * @param type the type of account (e.g., "Credit Card Account")
     * @return a list of accounts matching the specified type
     */
    public List<BankAccount> getAccountsByType(String type) {
        return accounts.stream()
                .filter(a -> a.displayAccountType().equals(type))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total balance across all accounts.
     *
     * @return the sum of balances from all accounts
     */
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(BankAccount::inquireBalance)
                .sum();
    }

    /**
     * Returns a list of all active accounts.
     *
     * @return a list of accounts with status "Active"
     */
    public List<BankAccount> getActiveAccounts() {
        return accounts.stream()
                .filter(a -> "Active".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all closed or inactive accounts.
     *
     * @return a list of accounts with status other than "Active"
     */
    public List<BankAccount> getClosedAccounts() {
        return accounts.stream()
                .filter(a -> !"Active".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a string representation of the customer's information.
     *
     * @return a formatted string with the customer's name and birth date
     */
    @Override
    public String toString() {
        return String.format("%s %s (Date of Birth: %s)", firstName, lastName, BirthDate);
    }

    /**
     * Returns the customer's unique ID.
     *
     * @return the customer ID
     */
    public String getId() {
        return id;
    }
}
