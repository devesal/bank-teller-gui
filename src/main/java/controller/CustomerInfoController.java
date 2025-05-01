package controller;

import model.Customer;
import model.BankAccount;
import util.Exceptions.*;
import util.FileIO;
import util.TransactionLogger;
import view.CustomerFormView;
import view.CustomerInfoView;
import view.MainView;
import model.*;
import view.TransactionHistoryView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;
    private final List<Customer> customers;
    private final List<BankAccount> allAccounts = FileIO.loadAllAccounts();
    private Customer currentCustomer;

    public CustomerInfoController(MainView mainView) {
        this.view = mainView.getCustomerInfoView();
        this.mainView = mainView;
        // load persisted customers
        this.customers = FileIO.loadAllCustomers();
        initController();
    }

    private void initController() {
        // Transaction History button
        view.getBtnHistory().addActionListener(e -> {
            view.showRightCard(CustomerInfoView.CARD_TRANSACTION_HISTORY);
            mainView.getHeader().showControls(false);
            loadTransactionHistory();

        });

        // Double-click account row
        view.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view.showRightCard(CustomerInfoView.CARD_BANK_ACCOUNT);
                    mainView.getHeader().updateHeaderTitle("ACCOUNT DETAILS");
                    mainView.getHeader().showControls(false);
                }
            }
        });

        // Add Bank Account
        view.getBtnAddAccount().addActionListener(e -> {
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(view,
                        "No customer selected!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1) Define the account‐type options
            String[] options = {
                    "Savings Account",
                    "Checking Account (₱500 min)",
                    "Investment Account (₱5,000, 35% rate)",
                    "Credit Card Account (₱25,000 limit)"
            };

            // 2) Show the dialog
            String choice = (String) JOptionPane.showInputDialog(
                    view,                                // parent
                    "Select account type to add:",      // message
                    "Add Bank Account",                 // title
                    JOptionPane.PLAIN_MESSAGE,
                    null,                                // icon
                    options,                             // choices
                    options[0]                           // default
            );

            // 3) If user cancels, choice is null → do nothing
            if (choice == null) return;

            // 4) Instantiate the right BankAccount subclass
            BankAccount newAcc;
            switch (choice) {
                case "Checking Account (₱500 min)":
                    newAcc = new CheckingAccount(
                            currentCustomer.getFirstName(),
                            currentCustomer.getLastName(),
                            500.0
                    );
                    break;
                case "Investment Account (₱5,000, 35% rate)":
                    newAcc = new InvestmentAccount(
                            currentCustomer.getFirstName(),
                            currentCustomer.getLastName(),
                            5000.0,
                            0.35
                    );
                    break;
                case "Credit Card Account (₱25,000 limit)":
                    newAcc = new CreditCardAccount(
                            currentCustomer.getFirstName(),
                            currentCustomer.getLastName(),
                            25000.0
                    );
                    break;
                default:  // "Savings Account"
                    newAcc = new BankAccount(
                            currentCustomer.getFirstName(),
                            currentCustomer.getLastName()
                    );
            }

            // 5) Attach to customer and global list, persist & refresh
            currentCustomer.addAccount(newAcc);
            allAccounts.add(newAcc);
            saveAll();                      // calls FileIO.saveAllCustomers(...) & saveAllAccounts(...)
            populateAccountsTable();        // refresh the JTable

            // 6) Inform user
            JOptionPane.showMessageDialog(view,
                    String.format("%s added!\nAccount No: %d",
                            newAcc.displayAccountType(),
                            newAcc.getAccountNo()),
                    "Account Created",
                    JOptionPane.INFORMATION_MESSAGE
            );
            System.out.println("Saved " + customers.size() + " customers and " + allAccounts.size() + " accounts.");
        });

        // Close Account
        view.getBtnCloseAccount().addActionListener(e -> {
            int row = view.getAccountsTable().getSelectedRow();
            if (row >= 0 && currentCustomer != null) {
                int accNo = (int) view.getAccountsTable().getValueAt(row, 0);
                for (BankAccount acc : currentCustomer.getAccounts()) {
                    if (acc.getAccountNo() == accNo) {
                        acc.setStatus("Closed");
                        break;
                    }
                }
                saveAll();
                populateAccountsTable();
            }
        });

        // Edit Customer Info
        view.getBtnEdit().addActionListener(e -> {
            JTextField firstNameField = new JTextField(currentCustomer.getFirstName(), 15);
            JTextField lastNameField = new JTextField(currentCustomer.getLastName(), 15);

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.add(new JLabel("Edit First Name:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Edit Last Name:"));
            panel.add(lastNameField);
            panel.add(new JLabel("Date of Birth:"));
            panel.add(new JLabel(currentCustomer.getBirthDate())); // display only, not editable

            int result = JOptionPane.showConfirmDialog(
                    view,
                    panel,
                    "Edit Customer Information",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String newFirstName = firstNameField.getText().trim();
                String newLastName = lastNameField.getText().trim();

                if (!newFirstName.isEmpty() && !newLastName.isEmpty()) {
                    currentCustomer.setFirstName(newFirstName);
                    currentCustomer.setLastName(newLastName);

                    view.setCustomerName(newFirstName + " " + newLastName);

                    JOptionPane.showMessageDialog(view, "Customer info updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(view, "First name and last name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Account Statement
        view.getBtnStatement().addActionListener(e -> {
            // implement statement display, perhaps another card
        });

    }

    /**
     * Populate view with customer data by index in list.
     */
    public void setCustomerIndex(int index) {
        this.currentCustomer = customers.get(index);
        view.setCustomerId(String.valueOf(index + 1));
        view.setCustomerName(currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
        view.setCustomerDob(currentCustomer.getBirthDate());
        populateAccountsTable();
    }

    /**
     * Populate view with most recently added customer.
     */
    public void setLatestCustomer() {
        if (customers.isEmpty()) return;
        setCustomerIndex(customers.size() - 1);
    }

    private void populateAccountsTable() {
        DefaultTableModel model = (DefaultTableModel) view.getAccountsTable().getModel();
        model.setRowCount(0);
        for (BankAccount acc : currentCustomer.getAccounts()) {
            model.addRow(new Object[]{
                    acc.getAccountNo(),
                    acc.displayAccountType(),
                    acc.getStatus(),

                    acc.inquireBalance()
            });
        }
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
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(view, "No customer selected");
            return;
        }

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
    private void saveAll() {
        FileIO.saveAllCustomers(customers);
        List<BankAccount> all = customers.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        FileIO.saveAllAccounts((ArrayList<BankAccount>) all);
    }
}
