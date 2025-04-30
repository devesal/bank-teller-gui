package model;


import java.util.ArrayList;
import java.util.Random;

public class BankAccount {

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
     * Constructs a new active BankAccount with a random 9-digit account number
     * and given first and last name.
     *
     * @param firstName the account holder's first name
     * @param lastName the account holder's last name
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
     */
    private int generateAccountNumber() {
        return 100_000_000 + RANDOM.nextInt(900_000_000);
    }

    public int getAccountNo() {
        return accountNo;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void deposit(double amount) {
        if (!"Active".equals(status)) {
            //System.out.println("❌ Cannot deposit to a closed account.");
            return;
        }
        this.balance += amount;
        //System.out.println("Money has been deposited to your account");
        //System.out.println("Please check your account for safety measures");
    }

    public void withdraw(double amount) {
        if (!"Active".equals(status)) {
            //System.out.println("❌ Cannot withdraw from a closed account.");
            return;
        }
        if (balance >= amount) {
            this.balance -= amount;
            //System.out.println("Money withdrawn: ₱" + amount);
        } else {
            //System.out.println("❌ Insufficient balance. Transaction terminated");
        }
    }

    public double inquireBalance() {
        return balance;
    }

    public void transferMoney(int AccountNo, double amount, ArrayList<BankAccount> bankAccounts) {
        if (!"Active".equals(status)) {
            //System.out.println("❌ Cannot transfer from a closed account.");
            return;
        }
        if (amount > balance) {
            //System.out.println("❌ Invalid amount. Transaction terminated");
            return;
        }
        for (BankAccount recipient : bankAccounts) {
            if (recipient.getAccountNo() == AccountNo) {
                this.balance -= amount;
                recipient.deposit(amount);
                //System.out.println("Transfer successful! ₱" + amount + " has been sent to account #" + recipient.getAccountNo());
                return;
            }
        }
        System.out.println("❌ Account not found");
    }

    /**
     * Closes the account by setting its status to Closed. Balance is withdrawn if positive.
     * The account remains in the list for potential reopening.
     */
    public void closeAccount() {
        if (!"Active".equals(status)) {
            System.out.println("❌ Account is already closed.");
            return;
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
     * Reopens a closed account by setting its status back to Active.
     */
    public void reopenAccount() {
        if ("Active".equals(status)) {
            System.out.println("❌ Account is already active.");
            return;
        }
        setStatus("Active");
        System.out.println("Account #" + accountNo + " has been reopened.");
    }

    public String displayAccountType() {
        return "Savings Account";
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + "\n#" + accountNo + "\nStatus: " + status;
    }
}