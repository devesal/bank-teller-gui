package util;

import model.Transaction;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionLogger {
    private String fileLocation;
    private DefaultTableModel tableModel;

    public TransactionLogger (){
        fileLocation ="";
        tableModel = null;
    }

    public TransactionLogger (String fileLocation){
        this.fileLocation = fileLocation;
        this.tableModel = tableModel;
    }

    public void logTransaction (Transaction transaction, double balance) throws IOException {
        try (BufferedWriter transacWriter = new BufferedWriter(new FileWriter(fileLocation, true))){
            transacWriter.write(transaction.toString());
            transacWriter.newLine();
        } catch (IOException e) {
            System.err.println("Failed to log transaction");
        }
        addTransactionToTable(transaction, balance);
    }

    public List<String[]> loadTransactions() {
        List<String[]> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(""))) {
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


    public void addTransactionToTable (Transaction transaction, double balance) {
        String date = transaction.getTimestamp()
                .toLocalDate()
                .toString();
        String transactionType = String.valueOf(transaction.getType());
        String amount = (transactionType.equals("Deposit")||transactionType.equals("Transfer")
                || transactionType.equals("Add Investment") || transactionType.equals("Charge to Card") ? "+" : "-") + "₱" +
                String.format("%.2f", transaction.getAmount());
        String maintainingBalance = String.valueOf(balance);
        tableModel.addRow(new Object[]{date, transactionType, amount, balance});
    }
}
