package controller;

import model.Transaction;
import model.BankAccount;
import model.Customer;
import util.TransactionLogger;
import util.FileIO;
import view.ReportsView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.*;

public class ReportsController {
    private final ReportsView view;
    private final TransactionLogger transactionLogger;
    private final List<Customer> customers;
    private final List<BankAccount> allAccounts;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton btnExport;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private JLabel lblPageInfo;
    private static final int PAGE_SIZE = 50;
    private int currentPage = 0;
    private List<Transaction> currentTransactions;

    public ReportsController(ReportsView view) {
        this.view = view;
        this.transactionLogger = new TransactionLogger("transactions.csv");
        this.customers = FileIO.loadAllCustomers();
        this.allAccounts = FileIO.loadAllAccounts();
        initController();
    }

    private void initController() {

        view.getBtnGenerate().addActionListener(e -> generateReport());
        

        DefaultTableModel model = (DefaultTableModel) view.getResultsTable().getModel();
        sorter = new TableRowSorter<>(model);
        view.getResultsTable().setRowSorter(sorter);
        

        view.getResultsTable().getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = view.getResultsTable().columnAtPoint(evt.getPoint());
                if (column >= 0) {
                    sortTable(column);
                }
            }
        });


        btnExport = new JButton("Export to CSV");
        btnExport.setEnabled(false);
        btnExport.addActionListener(e -> exportToCSV());


        btnPrevPage = new JButton("Previous");
        btnNextPage = new JButton("Next");
        lblPageInfo = new JLabel("Page 0 of 0");
        
        btnPrevPage.setEnabled(false);
        btnNextPage.setEnabled(false);
        
        btnPrevPage.addActionListener(e -> showPreviousPage());
        btnNextPage.addActionListener(e -> showNextPage());
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnPrevPage);
        buttonPanel.add(lblPageInfo);
        buttonPanel.add(btnNextPage);
        buttonPanel.add(view.getBtnGenerate());
        buttonPanel.add(btnExport);
        view.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateTableForCurrentPage();
        }
    }

    private void showNextPage() {
        int totalPages = (int) Math.ceil((double) currentTransactions.size() / PAGE_SIZE);
        if (currentPage < totalPages - 1) {
            currentPage++;
            updateTableForCurrentPage();
        }
    }

    private void updateTableForCurrentPage() {
        if (currentTransactions == null || currentTransactions.isEmpty()) {
            return;
        }

        int startIndex = currentPage * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, currentTransactions.size());
        List<Transaction> pageTransactions = currentTransactions.subList(startIndex, endIndex);

        DefaultTableModel model = (DefaultTableModel) view.getResultsTable().getModel();
        model.setRowCount(0);

        String reportType = (String) view.getReportTypeCombo().getSelectedItem();
        switch (reportType) {
            case "All Transactions" -> showAllTransactions(pageTransactions);
            case "Summary (By Type)" -> showSummaryByType(pageTransactions);
            case "Account Statement" -> showAccountStatement(pageTransactions);
        }


        int totalPages = (int) Math.ceil((double) currentTransactions.size() / PAGE_SIZE);
        lblPageInfo.setText(String.format("Page %d of %d", currentPage + 1, totalPages));
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled(currentPage < totalPages - 1);
    }

    private void sortTable(int column) {
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(column, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }

    private void generateReport() {
        try {
            currentTransactions = transactionLogger.loadAllTransactions();
            currentPage = 0;
            

            currentTransactions = filterTransactions(currentTransactions);
            

            int totalPages = (int) Math.ceil((double) currentTransactions.size() / PAGE_SIZE);
            lblPageInfo.setText(String.format("Page %d of %d", currentPage + 1, totalPages));
            btnPrevPage.setEnabled(false);
            btnNextPage.setEnabled(totalPages > 1);
            

            updateTableForCurrentPage();
            

            btnExport.setEnabled(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error loading transactions: " + e.getMessage());
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getPath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(file)) {

                DefaultTableModel model = (DefaultTableModel) view.getResultsTable().getModel();
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.print(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
                

                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        if (value != null) {
                            writer.print(value.toString());
                        }
                        if (col < model.getColumnCount() - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                }
                
                JOptionPane.showMessageDialog(view, "Report exported successfully to: " + file.getPath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(view, "Error exporting report: " + e.getMessage());
            }
        }
    }

    private List<Transaction> filterTransactions(List<Transaction> transactions) {

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


        String nameFilter = view.getNameField().getText().trim().toLowerCase();
        if (!nameFilter.isEmpty()) {

            Set<Integer> matchingAccountNos = allAccounts.stream()
                    .filter(acc -> (acc.getFirstName() + " " + acc.getLastName()).toLowerCase().contains(nameFilter))
                    .map(BankAccount::getAccountNo)
                    .collect(Collectors.toSet());
            
            transactions = transactions.stream()
                    .filter(tx -> matchingAccountNos.contains(tx.getFromAccount()) || matchingAccountNos.contains(tx.getToAccount()))
                    .collect(Collectors.toList());
        }


        String txnType = (String) view.getTxnTypeCombo().getSelectedItem();
        if (!"All".equals(txnType)) {
            transactions = transactions.stream()
                    .filter(tx -> tx.getType().name().equals(txnType.toUpperCase()))
                    .collect(Collectors.toList());
        }


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


        Map<Transaction.Type, Double> summary = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getType,
                        Collectors.summingDouble(Transaction::getAmount)
                ));


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


        Map<Integer, List<Transaction>> byAccount = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getFromAccount));

        for (Map.Entry<Integer, List<Transaction>> entry : byAccount.entrySet()) {
            int accountNo = entry.getKey();
            List<Transaction> accountTxns = entry.getValue();
            double runningBalance = 0.0;


            model.addRow(new Object[]{
                    "Account #" + accountNo,
                    "",
                    getAccountHolderName(accountNo),
                    "STATEMENT",
                    "",
                    ""
            });


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


            model.addRow(new Object[]{"", "", "", "", "", ""});
        }
    }

    private String getAccountHolderName(int accountNo) {
        return allAccounts.stream()
                .filter(acc -> acc.getAccountNo() == accountNo)
                .findFirst()
                .map(acc -> acc.getFirstName() + " " + acc.getLastName())
                .orElse("Unknown Account");
    }
} 