package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Customer {
    private String firstName;
    private String lastName;
    private String BirthDate;
    private List<BankAccount> accounts;
    private String status = "Active";

    public static final String ACTIVE = "Active";
    public static final String CLOSED = "Closed";

    public Customer(String firstName, String lastName, String BirthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.BirthDate = BirthDate;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
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

    // --- Personal Info Getters/Setters ---
    public String getLastName()  { return lastName;  }
    public String getFirstName() {return firstName;  }
    public String getStatus()    { return status;    }
    public String getBirthDate() { return BirthDate; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName)   { this.lastName = lastName;   }
    public void setStatus(String status)       { this.status = status;       }
    public void setBirthDate(String BirthDate) { this.BirthDate = BirthDate; }
}
