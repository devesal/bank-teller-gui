package model;

import java.time.LocalDateTime;


public class Transaction {

    public enum Type {
        DEPOSIT,         // Money added to an account
        WITHDRAWAL,      // Money withdrawn from an account
        TRANSFER,        // Funds transferred between accounts
        ENCASH,          // Cheque encashment
        CASH_ADVANCE,    // Cash taken against a credit line
        CHARGE_TO_CARD,  // Charge made to a credit card
        PAY_CARD,        // Payment made towards a card
        ADD_INVESTMENT   // Funds added to an investment account
    }

    private final int fromAccount;
    private int toAccount;
    private final double amount;
    private final Type type;
    private final LocalDateTime timestamp;


    public Transaction(int fromAccount, double amount, Type type) {
        this.fromAccount = fromAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }


    public Transaction(int fromAccount, int toAccount, double amount, Type type) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }


    public int getFromAccount() {
        return fromAccount;
    }


    public int getToAccount() {
        return toAccount;
    }


    public double getAmount() {
        return amount;
    }


    public Type getType() {
        return type;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return String.format("[%s] %s: from #%d to #%d amount=₱%.2f",
                timestamp, type, fromAccount, toAccount, amount);
    }
}
