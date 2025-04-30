package util;

import javax.swing.table.DefaultTableModel;
import java.io.*;

public class TransactionLogger {
    private String fileLocation;

    public TransactionLogger (){
        fileLocation ="";
    }

    public TransactionLogger (String fileLocation){
        this.fileLocation = fileLocation;
    }

    public void logTransaction (Transaction transaction) throws IOException {
        try (BufferedWriter transacWriter = new BufferedWriter(new FileWriter(fileLocation, true))){
            transacWriter.write(transaction.toString());
            transacWriter.newLine();
        } catch (IOException e) {
            System.err.println("Failed to log transaction");
        }
    }

    public void loadTransactions (DefaultTableModel tTable, String fileLocation){
        try (BufferedReader tReader = new BufferedReader(new FileReader(fileLocation))){
            String line;
            while ((line = tReader.readLine())!= null){
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String date = parts[0].substring(0, 10); // Only keep the date part from timestamp
                    String type = parts[3];
                    String amount = (type.equals("DEPOSIT") || type.equals("TRANSFER") ? "+" : "-") + "$" + parts[4];
                    String balance = ""; // Optional: If you have balance, otherwise leave blank
                    tTable.addRow(new Object[]{date, type, amount, balance});
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }
}

