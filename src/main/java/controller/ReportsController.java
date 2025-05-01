
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
/**
 * Controller class responsible for generating and managing transaction reports.
 * It interacts with the view and loads data using FileIO and TransactionLogger.
 * @author Aquino, Theo James Coroneza
 * @author Arellano, Clendrick Joshua Mangonon
 * @author Mangonon, John Cedrick Garcia
 * @author Ong, Ron Miguel Cau
 * @author Ramos, Ricky Marc Salazar
 * @author Rosana, Jeaven Vincent Yojan Operia
 * @version 2.1
 */
public class ReportsController {
    private final ReportsView view; // GUI component for the report
    private final TransactionLogger transactionLogger; // Utility to read transactions
    private final List<Customer> customers; // Loaded customer data
    private final List<BankAccount> allAccounts; // Loaded bank account data
    private TableRowSorter<DefaultTableModel> sorter; // Sorter for table rows
    private JButton btnExport; // Export to CSV button
    private JButton btnPrevPage; // Button for previous page
    private JButton btnNextPage; // Button for next page
    private JLabel lblPageInfo; // Page number display
    private static final int PAGE_SIZE = 50; // Transactions per page
    private int currentPage = 0; // Current page index
    private List<Transaction> currentTransactions; // Currently filtered transactions

    /**
     * Constructor that initializes controller with view and loads required data.
     * @param view the ReportsView associated with this controller
     */
    public ReportsController(ReportsView view) {
        this.view = view;
        this.transactionLogger = new TransactionLogger("transactions.csv");
        this.customers = FileIO.loadAllCustomers();
        this.allAccounts = FileIO.loadAllAccounts();
        initController();
    }

    /**
     * Initializes listeners, sorters, and view components.
     */
    private void initController() {
        // Add action listener to the generate button
        view.getBtnGenerate().addActionListener(e -> generateReport());

        // Initialize table sorter for dynamic sorting
        DefaultTableModel model = (DefaultTableModel) view.getResultsTable().getModel();
        sorter = new TableRowSorter<>(model);
        view.getResultsTable().setRowSorter(sorter);

        // Add column sorting functionality to the table
        view.getResultsTable().getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = view.getResultsTable().columnAtPoint(evt.getPoint());
                if (column >= 0) {
                    sortTable(column);
                }
            }
        });

        // Initialize export button
        btnExport = new JButton("Export to CSV");
        btnExport.setEnabled(false);
        btnExport.addActionListener(e -> exportToCSV());

        // Initialize pagination controls
        btnPrevPage = new JButton("Previous");
        btnNextPage = new JButton("Next");
        lblPageInfo = new JLabel("Page 0 of 0");

        btnPrevPage.setEnabled(false);
        btnNextPage.setEnabled(false);

        btnPrevPage.addActionListener(e -> showPreviousPage());
        btnNextPage.addActionListener(e -> showNextPage());

        // Add controls to panel and to view
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnPrevPage);
        buttonPanel.add(lblPageInfo);
        buttonPanel.add(btnNextPage);
        buttonPanel.add(view.getBtnGenerate());
        buttonPanel.add(btnExport);
        view.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Displays the previous page of transactions if available.
     */
    private void showPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateTableForCurrentPage();
        }
    }

    /**
     * Displays the next page of transactions if available.
     */
    private void showNextPage() {
        int totalPages = (int) Math.ceil((double) currentTransactions.size() / PAGE_SIZE);
        if (currentPage < totalPages - 1) {
            currentPage++;
            updateTableForCurrentPage();
        }
    }

    /**
     * Updates the table to show the current page of transactions.
     */
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

    /**
     * Sorts the table based on the clicked column.
     * @param column the column index to sort by
     */
    private void sortTable(int column) {
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(column, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }

    /**
     * Generates the report and applies filters.
     */
    private void generateReport() {
        try {
            currentTransactions = transactionLogger.loadAllTransactions();
            currentPage = 0;

            // Apply filters based on user input
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

    /**
     * Exports the current report view to a CSV file.
     */
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

    /**
     * Applies various filters to the list of transactions based on the user's input.
     * @param transactions the list of transactions to filter
     * @return the filtered list of transactions
     */
    private List<Transaction> filterTransactions(List<Transaction> transactions) {
        // Filter logic for account number, name, type, and date range
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

    /**
     * Displays all transactions in the current page with running balance.
     * @param transactions the transactions to display
     */
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

    /**
     * Displays a summary of the total transaction amount by transaction type.
     * @param transactions the list of transactions to summarize
     */
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
                    ""
            });
        }
    }

    /**
     * Displays account statements grouped by account.
     * @param transactions the list of transactions to display
     */
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

    /**
     * Retrieves the full name of the account holder based on account number.
     * @param accountNo the account number to search for
     * @return the full name of the account holder or "Unknown Account"
     */
    private String getAccountHolderName(int accountNo) {
        return allAccounts.stream()
                .filter(acc -> acc.getAccountNo() == accountNo)
                .findFirst()
                .map(acc -> acc.getFirstName() + " " + acc.getLastName())
                .orElse("Unknown Account");
    }
}