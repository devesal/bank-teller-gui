package controller;

import model.BankAccount;
import model.Customer;
import model.Transaction;
import util.Exceptions.AccountClosedException;
import util.Exceptions.InsufficientFundsException;
import util.Exceptions.TransactionLimitException;
import util.TransactionLogger;
import view.CustomerInfoView;
import view.MainView;
import view.TransactionHistoryView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;

    public CustomerInfoController(MainView mainView) {
        this.view = mainView.getCustomerInfoView();
        this.mainView = mainView;
        initController();
    }

    private void initController() {
        // 1) “Transaction History” button -> history card
        view.getHistoryButton().addActionListener(
                e -> {
                    view.showRightCard(CustomerInfoView.TRANSACTION_HISTORY);
                    mainView.getHeader().showControls(false);
                    loadTransactionHistory();
                }
        );

        view.getStatementButton().addActionListener(
                e -> {
                    view.showRightCard(CustomerInfoView.ACCOUNT_STATEMENT);
                    mainView.getHeader().showControls(false);
                }
        );

        view.getCloseAccountButton().addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    view,                                   // parent component
                    "Are you sure you want to close this account?",
                    "Confirm Close Account",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                // TODO: perform the account‐closure logic here
                // e.g. model.closeAccount(selectedAccount);
                // refresh view
            }
        });

        // Pop up dialogue for editing account
        view.getEditButton().addActionListener(e -> {
            // create the text fields
            JTextField firstNameField = new JTextField(20);
            JTextField lastNameField  = new JTextField(20);
            JTextField dobField       = new JTextField(20);

            // build a panel to hold them
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("First Name:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Last Name:"));
            panel.add(lastNameField);
            panel.add(new JLabel("Date of Birth:"));
            panel.add(dobField);

            // show the dialog
            int result = JOptionPane.showConfirmDialog(
                    view,               // parent component
                    panel,              // contents
                    "Edit Customer",    // title
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String first = firstNameField.getText().trim();
                String last  = lastNameField.getText().trim();
                String dob   = dobField.getText().trim();
                // TODO: apply these values to model/view
                System.out.printf("New values: %s %s %s%n", first, last, dob);
            }
        });

        // 2) Double‐click on a row -> bank‐account card
        view.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view.showRightCard(CustomerInfoView.BANK_ACCOUNT);
                    mainView.getHeader().updateHeaderTitle("ACCOUNT DETAILS");
                    mainView.getHeader().showControls(false);
                }
            }
        });

        // 3) “Add Bank Account” -> flip back to accounts list (or launch wizard)
        view.getAddAccountButton().addActionListener(e -> {
            view.showRightCard(CustomerInfoView.BANK_ADD);
            mainView.getHeader().updateHeaderTitle("BANK ACCOUNTS");
        });
    }

    private void processTransaction (BankAccount account, Transaction transaction) throws AccountClosedException, TransactionLimitException, InsufficientFundsException {
        switch (transaction.getType()){
            case DEPOSIT ->     account.deposit(transaction.getAmount());
            case WITHDRAWAL ->  account.withdraw(transaction.getAmount());
            case TRANSFER -> account.withdraw(transaction.getAmount());
            default -> throw new IllegalArgumentException("Unknown transaction type0");
        }

        String logFile = "logs/" + account.getAccountNo() + "_transactions.txt";
        TransactionLogger logger = new TransactionLogger(logFile);
        try {
            logger.logTransaction(transaction, account.inquireBalance());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTransactionHistory() {
        // Load transactions for this customer's account
        String logFile = "transactions.log";
        TransactionLogger logger = new TransactionLogger(logFile);
        List<String[]> transactions = logger.loadTransactions();

        // Get the transaction history view
        TransactionHistoryView historyView = new TransactionHistoryView();
        JTable historyTable = historyView.getHistoryTable();
        DefaultTableModel model = (DefaultTableModel) historyView.getHistoryTable().getModel();

        // Clear existing data
        model.setRowCount(0);

        // Add transactions to table
        for (String[] transaction : transactions) {
            // Format: [0]timestamp, [1]type, [2]fromAccount, [3]amount, [4]toAccount, [5]balance
            String date = transaction[0].split("T")[0]; // Just get date part
            String type = transaction[1];
            String amount = formatAmount(type, transaction[3]);
            String balance = "₱" + transaction[5];

            model.addRow(new Object[]{date, type, amount, balance});
        }
    }

    private String formatAmount(String type, String amount) {
        // Add + for deposits, - for withdrawals
        if (type.equalsIgnoreCase("DEPOSIT")) {
            return "+₱" + amount;
        } else {
            return "-₱" + amount;
        }
    }
}
