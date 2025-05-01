package util;

import model.Transaction;

import javax.swing.table.DefaultTableModel;
import java.io.*;

public class TransactionLogger {
    private String fileLocation;
    private DefaultTableModel tableModel;

    public TransactionLogger (){
        fileLocation ="";
        tableModel = null;
    }

    public TransactionLogger (String fileLocation, DefaultTableModel tableModel){
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

    public void loadTransactions (DefaultTableModel tTable, String fileLocation){
        try (BufferedReader tReader = new BufferedReader(new FileReader(fileLocation))){
            String line;
            while ((line = tReader.readLine())!= null){
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String date = parts[0].substring(0, 10);
                    String type = parts[3];
                    String amount = (type.equals("DEPOSIT") || type.equals("TRANSFER") ? "+" : "-") + "$" + parts[4];
                    String balance = "";
                    tTable.addRow(new Object[]{date, type, amount, balance});
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find report file");
        } catch (IOException e) {
            System.err.println("Error writing to file");
        }

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

