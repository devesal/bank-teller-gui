package controller;

import model.Customer;
import model.BankAccount;
import util.FileIO;
import view.CustomerFormView;
import view.CustomerInfoView;
import view.MainView;

import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;
    private final List<Customer> customers;
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
            // Reset the form but pre-fill customer info
            mainView.getCustomerFormView().reset();
            mainView.getCustomerFormView().getFirstNameField().setText(currentCustomer.getFirstName());
            mainView.getCustomerFormView().getLastNameField().setText(currentCustomer.getLastName());
            mainView.getCustomerFormView().getDobField().setText(currentCustomer.getBirthDate());

            // Show only the account selection step
            mainView.getCustomerFormView().showStep(CustomerFormView.STEP_ACCOUNTS);

            // Show the form panel
            mainView.showPage(MainView.ADD_CUSTOMERS_VIEW);
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
            mainView.showPage(MainView.CUSTOMER_INFO_VIEW);
            // assume the form is set up to edit currentCustomer
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

    private void saveAll() {
        FileIO.saveAllCustomers(customers);
        List<BankAccount> all = customers.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        FileIO.saveAllAccounts((ArrayList<BankAccount>) all);
    }
}
