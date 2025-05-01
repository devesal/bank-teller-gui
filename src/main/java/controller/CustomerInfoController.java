package controller;

import view.CustomerInfoView;
import view.MainView;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import util.FileIO;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;
    private final java.util.List<Customer> customers;
    private final List<BankAccount> allAccounts = FileIO.loadAllAccounts();
    private Customer currentCustomer;

    public CustomerInfoController(MainView mainView) {
        this.view = mainView.getCustomerInfoView();
        this.mainView = mainView;
        this.customers = FileIO.loadAllCustomers();
        initController();
    }

    private void initController() {
        // 1) “Transaction History” button -> history card
        view.getHistoryButton().addActionListener(
                e -> {
                    view.showRightCard(CustomerInfoView.TRANSACTION_HISTORY);
                    mainView.getHeader().showControls(false);
                }
        );

        view.getStatementButton().addActionListener(
                e -> {
                    view.showRightCard(CustomerInfoView.ACCOUNT_STATEMENT);
                    mainView.getHeader().showControls(false);
                }
        );

        view.getCloseAccountButton().addActionListener(e -> {
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(view,
                        "No customer selected!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    view,
                    "Are you sure you want to permanently close this customer's account and delete all their data?",
                    "Confirm Close Account",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                customers.remove(currentCustomer);
                allAccounts.removeIf(acc -> currentCustomer.getAccounts().contains(acc));

                saveAll();

                JOptionPane.showMessageDialog(view,
                        "Customer and all associated accounts have been removed.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                mainView.showPage(MainView.CUSTOMERS);
                mainView.getHeader().updateHeaderTitle("CUSTOMERS");
                mainView.getHeader().showControls(true);
            }
        });

        // Pop up dialogue for editing account
        view.getCloseAccountButton().addActionListener(e -> {
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(view,
                        "No customer selected!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    view,
                    "Are you sure you want to permanently close this customer's account and delete all their data?",
                    "Confirm Close Account",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                customers.remove(currentCustomer);
                allAccounts.removeIf(acc -> currentCustomer.getAccounts().contains(acc));
                currentCustomer = null;

                saveAll();

                JOptionPane.showMessageDialog(view,
                        "Customer and all associated accounts have been removed.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Navigate back and refresh
                mainView.showPage(MainView.CUSTOMERS);
                mainView.getHeader().updateHeaderTitle("CUSTOMERS");
                mainView.getHeader().showControls(true);

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

    }
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

    private void saveAll() {
        FileIO.saveAllCustomers(customers);
        List<BankAccount> all = customers.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        FileIO.saveAllAccounts((ArrayList<BankAccount>) all);
    }
}
