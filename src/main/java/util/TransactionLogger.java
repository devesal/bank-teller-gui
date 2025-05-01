package util;

import model.Transaction;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles logging of transactions to a file and optionally displaying them in a table model.
 */
public class TransactionLogger {
    private String fileLocation;
    private DefaultTableModel tableModel;

    /**
     * Default constructor initializes with empty file path and null table model.
     */
    public TransactionLogger() {
        this.fileLocation = "";
        this.tableModel = null;
    }

    /**
     * Constructs a TransactionLogger with a specified file location.
     *
     * @param fileLocation the path to the transaction log file
     */
    public TransactionLogger(String fileLocation) {
        this.fileLocation = fileLocation;
        this.tableModel = null;
    }

    /**
     * Logs a transaction to the specified file and adds it to the table model, if applicable.
     *
     * @param transaction the transaction to log
     * @param balance the resulting balance after the transaction
     * @throws IOException if writing to the file fails
     */
    public void logTransaction(Transaction transaction, double balance) throws IOException {
        try (BufferedWriter transacWriter = new BufferedWriter(new FileWriter(fileLocation, true))) {
            transacWriter.write(transaction.toString());
            transacWriter.newLine();
        } catch (IOException e) {
            System.err.println("Failed to log transaction: " + e.getMessage());
        }

        addTransactionToTable(transaction, balance);
    }

    /**
     * Loads all transactions from the log file.
     *
     * @return a list of transaction data arrays (parsed by comma)
     */
    public List<String[]> loadTransactions() {
        List<String[]> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    transactions.add(parts);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Adds a transaction to the table model, if one is set.
     *
     * @param transaction the transaction to add
     * @param balance the current balance to display
     */
    public void addTransactionToTable(Transaction transaction, double balance) {
        if (tableModel == null) return;

        String date = transaction.getTimestamp().toLocalDate().toString();
        String transactionType = transaction.getType().toString();
        boolean isCredit = switch (transaction.getType()) {
            case DEPOSIT, TRANSFER, ADD_INVESTMENT, CHARGE_TO_CARD -> true;
            default -> false;
        };
        String amount = (isCredit ? "+" : "-") + "₱" + String.format("%.2f", transaction.getAmount());
        tableModel.addRow(new Object[]{date, transactionType, amount, String.format("₱%.2f", balance)});
    }

    /**
     * Sets the table model used to display transactions.
     *
     * @param tableModel the table model to attach
     */
    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }
}
