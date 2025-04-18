package models;

import java.util.ArrayList;

public class CreditCardAccount extends BankAccount {
    private double creditLimit;
    private double charges;

    public CreditCardAccount() {
        super();
        creditLimit = 100000;
        charges = 0.0;
    }

    public CreditCardAccount(int accountNo, String accountName, double creditLimit, double charges) {
        super(accountNo, accountName);
        this.creditLimit = creditLimit;
        this.charges = charges;
        setAccountNo(accountNo);
        setAccountName(accountName);
    }

    public double getCreditLimit() {
        return this.creditLimit;
    }

    public double getCharges() {
        return this.charges;
    }

    public void payCard(double amount) {
          if (getStatus().equalsIgnoreCase("Closed")) {
            System.out.println("❌ Transaction declined: Account is closed.");
            return;
        }
        if (amount > charges) {
            System.out.println("❌ Payment exceeds total charges");
            return;
        } else {
            charges -= amount;
            System.out.println("Payment successful! Your remaining balance is: ₱" + charges);
        }
    }

    public void inquireAvailableCredit() {
         if (getStatus().equalsIgnoreCase("Closed")) {
            System.out.println("❌ Cannot inquire credit: Account is closed.");
            return;
        }
        double availableCredit = creditLimit - charges;
        System.out.println("Your available credit is: "+ availableCredit);
    }

    public void chargeToCard(double amount) {
        double availableCredit = creditLimit - charges;
        if (availableCredit >= amount) {
            charges += amount;
            System.out.println("Charges successful, total charges: "+ charges);
        } else {
            System.out.println("❌ Not enough credit");
        }
    }

    public void getCashAdvance(double amount) {
          if (getStatus().equalsIgnoreCase("Closed")) {
            System.out.println("❌ Transaction declined: Account is closed.");
            return;
        }

        double availableCredit = creditLimit - charges;
        availableCredit = availableCredit * 0.5;
        
        if (amount < availableCredit) {
            charges += amount;
            System.out.println("Cash advance approved! You have been charged: ₱" + amount);
        } else {
            System.out.println("❌ Transaction declined: Requested cash advance exceeds your available credit.");
        }
    }

    @Override
    public String toString() {
        return getAccountName() +
                "\n#" + getAccountNo() +
                "\nStatus: " + getStatus() +
                "\nCredit Limit: " + creditLimit;
    }

    @Override
    public void closeAccount(ArrayList<BankAccount> bankAccounts) {
        if (charges == 0) {
            System.out.println("Congratulations! You have achieved a Great Credit Score!");
        } else {
            System.out.println("❌ Please settle your remaining balance before closing your account");
            return;
        }

        bankAccounts.remove(this);
        super.setStatus("Closed");
        System.out.println("Your account has been closed.");
    }
} 
