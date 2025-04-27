package edu.slu.cs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a customer of the Co-Pals Bank System,
 * containing personal information and associated bank accounts.
 *
 * Created on: 4/27/2025
 *
 * @author Aquino, Theo
 * @version 1.0
 */
public class Customer {
    private String firstName;
    private String lastName;
    private String BirthDate;
    private String phoneNumber;
    private List<BankAccount> accounts;

    /**
     * Constructs a new Customer with the given personal information.
     *
     * @param firstName   the customer's first name
     * @param lastName    the customer's last name
     * @param BirthDate       the customer's email address
     * @param phoneNumber the customer's phone number
     */
    public Customer(String firstName, String lastName, String BirthDate, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.BirthDate = BirthDate;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }

    // --- Personal Info Getters/Setters ---

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }




    public void addAccount(BankAccount account) {
        accounts.add(account);
    }


    public void removeAccount(int accountNo) {
        accounts.removeIf(a -> a.getAccountNo() == accountNo);
    }


    public BankAccount getAccount(int accountNo) {
        return accounts.stream()
                .filter(a -> a.getAccountNo() == accountNo)
                .findFirst()
                .orElse(null);
    }


    public List<BankAccount> getAccounts() {
        return new ArrayList<>(accounts);
    }


    public List<BankAccount> getAccountsByType(String type) {
        return accounts.stream()
                .filter(a -> a.displayAccountType().equals(type))
                .collect(Collectors.toList());
    }


    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(BankAccount::inquireBalance)
                .sum();
    }


    public List<BankAccount> getActiveAccounts() {
        return accounts.stream()
                .filter(a -> "Active".equals(a.getStatus()))
                .collect(Collectors.toList());
    }


    public List<BankAccount> getClosedAccounts() {
        return accounts.stream()
                .filter(a -> !"Active".equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("%s %s (Date of Birth: %s, Phone: %s)",
                firstName, lastName, BirthDate, phoneNumber);
    }
}
