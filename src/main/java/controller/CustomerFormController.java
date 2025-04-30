package controller;

import model.Customer;
import model.BankAccount;
import model.CheckingAccount;
import model.CreditCardAccount;
import model.InvestmentAccount;
import util.FileIO;
import view.CustomerFormView;
import view.MainView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {
    private final CustomerFormView formView;
    private final MainView mainView;
    private final List<Customer> customers;
    private final List<BankAccount> allAccounts;

    /**
     * @param mainView the root view (should expose getCustomerFormView())
     */
    public CustomerFormController(MainView mainView) {
        this.formView = mainView.getCustomerFormView();
        this.mainView = mainView;
        // Load persisted data
        this.customers = FileIO.loadAllCustomers();
        this.allAccounts = FileIO.loadAllAccounts();
        initController();
    }

    private void initController() {
        // navigation buttons
        formView.getNextButtonStep1().addActionListener(e ->
                formView.showStep(CustomerFormView.STEP_ACCOUNTS)
        );
        formView.getBackButtonStep1().addActionListener(e ->
                mainView.showPage(MainView.CUSTOMERS_VIEW)
        );
        formView.getBackButtonStep2().addActionListener(e ->
                formView.showStep(CustomerFormView.STEP_PERSONAL)
        );

        // create and persist customer + accounts
        formView.getCreateButton().addActionListener(e -> {
            try {
                Customer customer = accountCreation();
                // Save to file
                FileIO.saveAllCustomers(customers);
                FileIO.saveAllAccounts(new ArrayList<>(allAccounts));

                // populate success view
                formView.getSuccessNameLabel().setText(
                        customer.getFirstName() + " " + customer.getLastName());
                formView.getSuccessDobLabel().setText(customer.getBirthDate());

                mainView.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
                mainView.getHeader().showControls(false);
                formView.showStep(CustomerFormView.STEP_SUCCESS);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(formView, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // navigate to info view
        formView.getViewButton().addActionListener(e ->
                mainView.showPage(MainView.CUSTOMER_INFO_VIEW)
        );
        setupUntoggleBehavior(formView.getSavingsToggleButton());
        setupUntoggleBehavior(formView.getCheckingToggleButton());
        setupUntoggleBehavior(formView.getInvestmentToggleButton());
        setupUntoggleBehavior(formView.getCreditCardToggleButton());
    }

    /**
     * Builds a new Customer using the view inputs and adds toggled accounts.
     * @return the created Customer
     */
    private Customer accountCreation() {
        String first = formView.getFirstNameField().getText().trim();
        String last  = formView.getLastNameField().getText().trim();
        String dob   = formView.getDobField().getText().trim();

        // create and store customer
        Customer customer = new Customer(first, last, dob);
        customers.add(customer);

        // create accounts for customer
        List<String> created = new ArrayList<>();
        if (formView.getSavingsToggleButton().isSelected()) {
            BankAccount acc = new BankAccount(first, last);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }
        if (formView.getCheckingToggleButton().isSelected()) {
            BankAccount acc = new CheckingAccount(first, last, 500.0);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }
        if (formView.getInvestmentToggleButton().isSelected()) {
            BankAccount acc = new InvestmentAccount(first, last, 5000, 0.35);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }
        if (formView.getCreditCardToggleButton().isSelected()) {
            BankAccount acc = new CreditCardAccount(first, last, 25000);
            customer.addAccount(acc);
            allAccounts.add(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }

        formView.getSuccessAccountsLabel().setText(
                created.isEmpty() ? "None" : String.join("\n", created)
        );

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

}
