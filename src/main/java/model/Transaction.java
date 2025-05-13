package model;

import java.time.LocalDateTime;
/**
 * Represents a transaction between bank accounts.
 *
 * Each transaction stores details such as source and destination accounts,
 * amount, type, and timestamp.
 */
public class Transaction {

    /**
     * Enum representing the type of transaction.
     */
    public enum Type {
        /** A deposit into an account */
        DEPOSIT,
        /** A withdrawal from an account */
        WITHDRAWAL,
        /** A transfer from one account to another */
        TRANSFER,
        /** An encashment transaction */
        ENCASH,
        /** A cash advance transaction */
        CASH_ADVANCE,
        /** A charge to a card */
        CHARGE_TO_CARD,
        /** A payment made to a card */
        PAY_CARD,
        /** An investment added to an account */
        ADD_INVESTMENT
    }

    /** Account number that initiated the transaction */
    private final int fromAccount;

    /** Account number that receives the transaction (can be same as fromAccount) */
    private final int toAccount;

    /** Amount of money involved in the transaction */
    private final double amount;

    /** Type of the transaction */
    private final Type type;

    /** Timestamp when the transaction was created */
    private final LocalDateTime timestamp;

    /**
     * Constructs a new Transaction.
     *
     * @param fromAccount the account number initiating the transaction
     * @param toAccount   the account number receiving the transaction
     * @param amount      the amount involved in the transaction
     * @param type        the type of transaction
     */
    public Transaction(int fromAccount, int toAccount, double amount, Type type) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * @return the originating account number
     */
    public int getFromAccount() {
        return fromAccount;
    }

    /**
     * @return the receiving account number
     */
    public int getToAccount() {
        return toAccount;
    }

    /**
     * @return the amount of money in the transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @return the type of transaction
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the date and time the transaction occurred
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of the transaction.
     *
     * @return formatted transaction summary
     */
    @Override
    public String toString() {
        return String.format("[%s] %s: from #%d to #%d amount=₱%.2f",
                timestamp, type, fromAccount, toAccount, amount);
    }
}