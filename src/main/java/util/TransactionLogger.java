package util;

import model.Transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TransactionLogger {
    private final Path filePath;


    public TransactionLogger(String fileLocation) {
        this.filePath = Paths.get(fileLocation);
        createFileIfMissing();
    }

    private void createFileIfMissing() {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to create transactions file", e);
        }
    }


    public void logTransaction(Transaction tx, double v) throws IOException {
        String line = formatCsvLine(tx);
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath, StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        }
    }


    public List<Transaction> loadAllTransactions() throws IOException {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(parseCsvLine(line));
            }
        }
        return list;
    }


    public Object[] toRow(Transaction tx, double balanceAfter) {
        String date = tx.getTimestamp().toLocalDate().toString();
        String type = tx.getType().name();
        boolean isCredit = switch (tx.getType()) {
            case DEPOSIT, TRANSFER, ADD_INVESTMENT, CHARGE_TO_CARD -> true;
            default -> false;
        };
        String amount = (isCredit ? "+" : "-") + String.format("₱%.2f", tx.getAmount());
        String balance = String.format("₱%.2f", balanceAfter);
        return new Object[]{ date, type, amount, balance };
    }

    // --- CSV formatting/parsing ---
    private String formatCsvLine(Transaction tx) {
        // timestamp,type,fromAccount,toAccount,amount
        return String.join(",",
                tx.getTimestamp().toString(),
                tx.getType().name(),
                String.valueOf(tx.getFromAccount()),
                String.valueOf(tx.getToAccount()),
                String.format("%.2f", tx.getAmount())
        );
    }

    private Transaction parseCsvLine(String line) {
        // timestamp,type,fromAccount,toAccount,amount
        String[] parts = line.split(",");
        LocalDateTime ts = LocalDateTime.parse(parts[0]);
        Transaction.Type type = Transaction.Type.valueOf(parts[1]);
        int from = Integer.parseInt(parts[2]);
        int to   = Integer.parseInt(parts[3]);
        double amt = Double.parseDouble(parts[4]);
        // use constructor with fromAccount, toAccount, amount, type
        Transaction tx = new Transaction(from, to, amt, type);
        // override timestamp if needed (assumes Transaction.timestamp is final set at now; cannot override)
        return tx;
    }
}