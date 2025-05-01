package model;


import util.Exceptions.*;
import java.io.Serializable;


public class CheckingAccount extends BankAccount implements Serializable {


    private final double minimumBalance;


    public CheckingAccount(String firstName, String lastName) {
        super(firstName, lastName);
        this.minimumBalance = 500.0;
    }


    public CheckingAccount(String firstName, String lastName, double minimumBalance) {
        super(firstName, lastName);
        this.minimumBalance = minimumBalance;
    }


    @Override
    public double inquireBalance() {
        return super.inquireBalance() + minimumBalance;
    }


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


    @Override
    public String displayAccountType() {
        return "Checking Account";
    }
}