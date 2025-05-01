package controller;

import model.*;
import util.FileIO;
import view.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {
    private final CustomerFormView formView;
    private final MainView mainView;
    private final List<Customer> customers;
    private final List<BankAccount> allAccounts;
    private final CustomerInfoView infoView;
    private Customer currentCustomer;
    private Runnable onCustomerCreated;

    public CustomerFormController(MainView mainView) {
        this.formView = mainView.getCustomerFormView();
        this.infoView = mainView.getCustomerInfoView();
        this.mainView = mainView;
        // Load persisted data
        this.customers = FileIO.loadAllCustomers();
        this.allAccounts = FileIO.loadAllAccounts();
        initController();
    }

    public void setOnCustomerCreatedCallback(Runnable cb) {
        this.onCustomerCreated = cb;
    }

    private void initController() {
        formView.getNextButtonStep1().addActionListener(
                e -> {
                    formView.showStep(MainView.STEP_ACCOUNTS);
                    mainView.setCurrentPage(MainView.STEP_ACCOUNTS);
                }
        );

        // create and persist customer + accounts
        formView.getCreateButton().addActionListener(e -> {
            Customer customer = createCustomer();
            customers.add(customer);
            currentCustomer = customer;
            // Save to file
            FileIO.saveAllCustomers(customers);
            FileIO.saveAllAccounts(new ArrayList<>(allAccounts));

            // populate success view
            formView.getSuccessNameLabel().setText(
                    customer.getFirstName() + " " + customer.getLastName());
            formView.getSuccessDobLabel().setText(customer.getBirthDate());

            mainView.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
            mainView.getHeader().showControls(false);
            formView.showStep(MainView.STEP_SUCCESS);

                    if (onCustomerCreated != null) {
                        onCustomerCreated.run();
                    }
        });

        setupUntoggleBehavior(formView.getSavingsToggleButton());
        setupUntoggleBehavior(formView.getCheckingToggleButton());
        setupUntoggleBehavior(formView.getInvestmentToggleButton());
        setupUntoggleBehavior(formView.getCreditCardToggleButton());
    }

    /**
     * Builds a new Customer using the formView inputs and adds toggled accounts.
     * @return the created Customer
     */
    private Customer createCustomer() {
        String first = formView.getFirstNameField().getText().trim();
        String last  = formView.getLastNameField().getText().trim();
        String dob   = formView.getDobField().getText().trim();

        Customer customer = new Customer(first, last, dob);

        // create accounts for customer
        List<String> created = new ArrayList<>();
        if (formView.getSavingsToggleButton().isSelected()) {
            BankAccount acc = new BankAccount(first, last);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
            FileIO.saveAllAccounts((ArrayList<BankAccount>) allAccounts);
            FileIO.saveAllCustomers(customers);
        }
        if (formView.getCheckingToggleButton().isSelected()) {
            BankAccount acc = new CheckingAccount(first, last, 500.0);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
            FileIO.saveAllAccounts((ArrayList<BankAccount>) allAccounts);
            FileIO.saveAllCustomers(customers);
        }
        if (formView.getInvestmentToggleButton().isSelected()) {
            BankAccount acc = new InvestmentAccount(first, last, 5000, 0.35);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
            FileIO.saveAllAccounts((ArrayList<BankAccount>) allAccounts);
            FileIO.saveAllCustomers(customers);
        }
        if (formView.getCreditCardToggleButton().isSelected()) {
            BankAccount acc = new CreditCardAccount(first, last, 25000);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
            FileIO.saveAllAccounts((ArrayList<BankAccount>) allAccounts);
            FileIO.saveAllCustomers(customers);
        }

        formView.getSuccessNameLabel().setText(customer.getFirstName() + " " + customer.getLastName());
        formView.getSuccessDobLabel().setText(customer.getBirthDate());
        formView.getSuccessAccountsLabel().setText(
                created.isEmpty() ? "None" : String.join("\n", created)
        );
        if (onCustomerCreated != null) {
            onCustomerCreated.run();
        }

        // Clear the “editing” flag so next time it’s a fresh form:
        System.out.println("Saved " + customers.size() + " customers and " + allAccounts.size() + " accounts.");
        return customer;
    }

    private void setupUntoggleBehavior(JToggleButton button) {
        button.addActionListener(e -> {
            if (button.isSelected()) {
                // Mark this click as a possible un-toggle candidate
                button.putClientProperty("wasSelected", true);
            } else {
                // Check if it was already selected before this click
                Object wasSelected = button.getClientProperty("wasSelected");
                if (Boolean.TRUE.equals(wasSelected)) {
                    button.setSelected(false); // Un-toggle
                    button.putClientProperty("wasSelected", false);
                }
            }
        });
    }

    public Customer getCurrentCustomer() { return currentCustomer; }
}