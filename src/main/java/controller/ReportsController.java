package controller;

import model.Transaction;
import util.TransactionLogger;
import view.ReportsView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsController {
    private final ReportsView view;
    private final TransactionLogger transactionLogger;

    public ReportsController(ReportsView view) {
        this.view = view;
        this.transactionLogger = new TransactionLogger("transactions.csv");
        initController();
    }

    private void initController() {
        // Add action listener to the generate button
        view.getBtnGenerate().addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try {
            List<Transaction> transactions = transactionLogger.loadAllTransactions();
            
            // Apply filters
            transactions = filterTransactions(transactions);
            
            // Generate report based on selected type
            String reportType = (String) view.getReportTypeCombo().getSelectedItem();
            switch (reportType) {
                case "All Transactions" -> showAllTransactions(transactions);
                case "Summary (By Type)" -> showSummaryByType(transactions);
                case "Account Statement" -> showAccountStatement(transactions);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error loading transactions: " + e.getMessage());
        }
    }

    private List<Transaction> filterTransactions(List<Transaction> transactions) {
        // Filter by account number if provided
        String accountFilter = view.getAccountField().getText().trim();
        if (!accountFilter.isEmpty()) {
            try {
                int accountNo = Integer.parseInt(accountFilter);
                transactions = transactions.stream()
                        .filter(tx -> tx.getFromAccount() == accountNo || tx.getToAccount() == accountNo)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid account number format");
                return Collections.emptyList();
            }
        }

        // Filter by name if provided
        String nameFilter = view.getNameField().getText().trim().toLowerCase();
        if (!nameFilter.isEmpty()) {
            // Note: This would require access to customer data to match names with accounts
            // For now, we'll skip name filtering
        }

        // Filter by transaction type
        String txnType = (String) view.getTxnTypeCombo().getSelectedItem();
        if (!"All".equals(txnType)) {
            transactions = transactions.stream()
                    .filter(tx -> tx.getType().name().equals(txnType.toUpperCase()))
                    .collect(Collectors.toList());
        }

        // Filter by date range
        Date fromDate = (Date) view.getFromDateSpinner().getValue();
        Date toDate = (Date) view.getToDateSpinner().getValue();
        LocalDate from = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        transactions = transactions.stream()
                .filter(tx -> {
                    LocalDate txDate = tx.getTimestamp().toLocalDate();
                    return !txDate.isBefore(from) && !txDate.isAfter(to);
                })
                .collect(Collectors.toList());

        return transactions;
    }

    private void showAllTransactions(List<Transaction> transactions) {
        DefaultTableModel model = (DefaultTableModel) view.getResultsTable().getModel();
        model.setRowCount(0);

        double runningBalance = 0.0;
        for (Transaction tx : transactions) {
            boolean isCredit = switch (tx.getType()) {
                case DEPOSIT, TRANSFER, ADD_INVESTMENT, CHARGE_TO_CARD -> true;
                default -> false;
            };

            runningBalance += isCredit ? tx.getAmount() : -tx.getAmount();

            model.addRow(new Object[]{
                    tx.getTimestamp().toLocalDate().toString(),
                    tx.getFromAccount(),
                    getAccountHolderName(tx.getFromAccount()),
                    tx.getType().name(),
                    String.format("₱%.2f", tx.getAmount()),
                    String.format("₱%.2f", runningBalance)
            });
        }
    }

    private void showSummaryByType(List<Transaction> transactions) {
        DefaultTableModel model = (DefaultTableModel) view.getResultsTable().getModel();
        model.setRowCount(0);

        // Group transactions by type and calculate totals
        Map<Transaction.Type, Double> summary = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getType,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        // Add summary rows
        for (Map.Entry<Transaction.Type, Double> entry : summary.entrySet()) {
            model.addRow(new Object[]{
                    "", // Empty date
                    "", // Empty account
                    "", // Empty name
                    entry.getKey().name(),
                    String.format("₱%.2f", entry.getValue()),
                    "" // Empty balance
            });
        }
    }

    private void showAccountStatement(List<Transaction> transactions) {
        DefaultTableModel model = (DefaultTableModel) view.getResultsTable().getModel();
        model.setRowCount(0);

        // Group transactions by account
        Map<Integer, List<Transaction>> byAccount = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getFromAccount));

        for (Map.Entry<Integer, List<Transaction>> entry : byAccount.entrySet()) {
            int accountNo = entry.getKey();
            List<Transaction> accountTxns = entry.getValue();
            double runningBalance = 0.0;

            // Add account header
            model.addRow(new Object[]{
                    "Account #" + accountNo,
                    "",
                    getAccountHolderName(accountNo),
                    "STATEMENT",
                    "",
                    ""
            });

            // Add transactions
            for (Transaction tx : accountTxns) {
                boolean isCredit = switch (tx.getType()) {
                    case DEPOSIT, TRANSFER, ADD_INVESTMENT, CHARGE_TO_CARD -> true;
                    default -> false;
                };

                runningBalance += isCredit ? tx.getAmount() : -tx.getAmount();

                model.addRow(new Object[]{
                        tx.getTimestamp().toLocalDate().toString(),
                        accountNo,
                        getAccountHolderName(accountNo),
                        tx.getType().name(),
                        String.format("₱%.2f", tx.getAmount()),
                        String.format("₱%.2f", runningBalance)
                });
            }

            // Add separator
            model.addRow(new Object[]{"", "", "", "", "", ""});
        }
    }

    private String getAccountHolderName(int accountNo) {
        // This would need to be implemented to fetch the account holder's name
        // For now, return a placeholder
        return "Account Holder";
    }
} 