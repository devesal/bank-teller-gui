package model;

import java.time.LocalDateTime;

/**
 * Represents a financial transaction between one or more accounts in the Co-Pals Bank System.
 * A transaction may be a deposit, withdrawal, transfer, or other supported types.
 *
 * Created on: 3/21/2025
 *
 * @version 1.0
 */
public class Transaction {

    /**
     * Enum representing the type of transaction.
     */
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

    /**
     * Constructs a transaction record.
     *
     * @param fromAccount the account initiating the transaction
     * @param amount      the amount of money involved in the transaction
     * @param type        the type of transaction being recorded
     */
    public Transaction(int fromAccount, double amount, Type type) {
        this.fromAccount = fromAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs a transaction record.
     *
     * @param fromAccount the account initiating the transaction
     * @param toAccount   the receiving account (can be the same for self-transactions)
     * @param amount      the amount of money involved in the transaction
     * @param type        the type of transaction being recorded
     */
    public Transaction(int fromAccount, int toAccount, double amount, Type type) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Returns the ID of the account initiating the transaction.
     *
     * @return the source account number
     */
    public int getFromAccount() {
        return fromAccount;
    }

    /**
     * Returns the ID of the account receiving the transaction.
     *
     * @return the destination account number
     */
    public int getToAccount() {
        return toAccount;
    }

    /**
     * Returns the monetary value of the transaction.
     *
     * @return the transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the type of the transaction.
     *
     * @return the transaction type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the timestamp when the transaction occurred.
     *
     * @return the timestamp of the transaction
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string summary of the transaction.
     *
     * @return formatted string with transaction details
     */
    @Override
    public String toString() {
        return String.format("[%s] %s: from #%d to #%d amount=₱%.2f",
                timestamp, type, fromAccount, toAccount, amount);
    }
}
