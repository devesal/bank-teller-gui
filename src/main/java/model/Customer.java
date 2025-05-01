package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Customer implements Serializable {
    private String firstName;
    private String lastName;
    private String BirthDate;
    private List<BankAccount> accounts;

    /**
     * Constructs a new Customer with the given personal information.
     *
     * @param firstName   the customer's first name
     * @param lastName    the customer's last name
     * @param BirthDate       the customer's email address
     */
    public Customer(String firstName, String lastName, String BirthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.BirthDate = BirthDate;
        this.accounts = new ArrayList<>();
    }

    // --- Personal Info Getters/Setters ---

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (!firstName.isEmpty()) {
            this.firstName = firstName;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (!lastName.isEmpty()) {
            this.lastName = lastName;
        }
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
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
        return String.format("%s %s (Date of Birth: %s)",
                firstName, lastName, BirthDate);
    }
}
