package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Represents a bank customer with personal details and a list of associated bank accounts.
 *
 * Provides utility methods to manage and retrieve information about the customer's accounts.
 */
public class Customer {

    /** Customer's first name */
    private String firstName;

    /** Customer's last name */
    private String lastName;

    /** Customer's birth date (formatted as a string) */
    private String BirthDate;

    /** List of bank accounts owned by the customer */
    private List<BankAccount> accounts;

    /**
     * Constructs a new Customer with the given personal information.
     *
     * @param firstName the customer's first name
     * @param lastName  the customer's last name
     * @param BirthDate the customer's date of birth
     */
    public Customer(String firstName, String lastName, String BirthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.BirthDate = BirthDate;
        this.accounts = new ArrayList<>();
    }

    // --- Personal Info Getters/Setters ---

    /**
     * @return the customer's first name
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
        this.firstName = firstName;
    }

    /**
     * @return the customer's last name
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
        this.lastName = lastName;
    }

    /**
     * @return the customer's birth date
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

    // --- Account Management ---

    /**
     * Adds a bank account to the customer's account list.
     *
     * @param account the bank account to add
     */
    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    /**
     * Removes a bank account from the customer's account list using the account number.
     *
     * @param accountNo the account number of the account to remove
     */
    public void removeAccount(int accountNo) {
        accounts.removeIf(a -> a.getAccountNo() == accountNo);
    }

    /**
     * Retrieves a specific account by account number.
     *
     * @param accountNo the account number to search for
     * @return the matching BankAccount object, or null if not found
     */
    public BankAccount getAccount(int accountNo) {
        return accounts.stream()
                .filter(a -> a.getAccountNo() == accountNo)
                .findFirst()
                .orElse(null);
    }

    /**
     * @return a list of all bank accounts owned by the customer
     */
    public List<BankAccount> getAccounts() {
        return new ArrayList<>(accounts);
    }

    /**
     * Filters the customer's accounts by account type.
     *
     * @param type the account type to filter by (e.g., "Savings Account")
     * @return a list of matching accounts
     */
    public List<BankAccount> getAccountsByType(String type) {
        return accounts.stream()
                .filter(a -> a.displayAccountType().equals(type))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total balance across all the customer's accounts.
     *
     * @return the total balance
     */
    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(BankAccount::inquireBalance)
                .sum();
    }

    /**
     * @return a list of active accounts (status = "Active")
     */
    public List<BankAccount> getActiveAccounts() {
        return accounts.stream()
                .filter(a -> "Active".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * @return a list of closed accounts (status not equal to "Active")
     */
    public List<BankAccount> getClosedAccounts() {
        return accounts.stream()
                .filter(a -> !"Active".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a string summary of the customer's identity and birth date.
     *
     * @return a formatted string of the customer's basic information
     */
    @Override
    public String toString() {
        return String.format("%s %s (Date of Birth: %s)",
                firstName, lastName, BirthDate);
    }
}
