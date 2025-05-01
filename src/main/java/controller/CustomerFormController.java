package controller;

import java.util.*;

import view.*;
import model.*;
import persistence.CustomerRepository;

public class CustomerFormController {
    private final CustomerRepository customers = new CustomerRepository();
    private final CustomerFormView view;
    private final MainView mainView;
    private Customer customer;

    public CustomerFormController(MainView mainView) {
        this.view = mainView.getCustomerFormView();
        this.mainView = mainView;

        initController();
    }

    private void initController() {
        // Add listeners for navigation buttons
        setButtonActions();
    }

    private void setButtonActions() {
        view.getNextButtonStep1().addActionListener(
                e -> {
                    view.showStep(MainView.STEP_ACCOUNTS);
                    mainView.setCurrentPage(MainView.STEP_ACCOUNTS);
                }
        );

        // create and store customer + accounts
        view.getCreateButton().addActionListener(
                e -> {
                    customer = createCustomer();
                    customers.add(customer);

                    view.getSuccessNameLabel().setText(
                            customer.getFirstName() + " " + customer.getLastName()
                    );
                    view.getSuccessDobLabel().setText(customer.getBirthDate());
                    view.getSuccessAccountsLabel().setText(listSelectedAccounts(customer));

                    view.showStep(MainView.STEP_SUCCESS);
                    mainView.setCurrentPage(MainView.STEP_SUCCESS);
                    mainView.getHeader().showControls(false);
                }
        );

        view.getViewButton().addActionListener(
                e -> {
                    new CustomerInfoController(mainView, customer);
                    mainView.showPage(MainView.CUSTOMER_INFO);
                    mainView.setCurrentPage(MainView.CUSTOMER_INFO);
                    mainView.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
                }
        );
    }

    private String listSelectedAccounts(Customer customer) {
        String first = customer.getFirstName();
        String last = customer.getLastName();

        // List summary of selected accounts
        List<String> selectedAccounts = new ArrayList<>();

        if (view.getSavingsToggleButton().isSelected()) {
            BankAccount acc = new BankAccount(first, last);
            customer.addAccount(acc);
            selectedAccounts.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }

        if (view.getCheckingToggleButton().isSelected()) {
            BankAccount acc = new CheckingAccount(first, last, 500.0);
            customer.addAccount(acc);
            selectedAccounts.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }

        if (view.getInvestmentToggleButton().isSelected()) {
            BankAccount acc = new InvestmentAccount(first, last, 5000, 0.35);
            customer.addAccount(acc);
            selectedAccounts.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }

        if (view.getCreditCardToggleButton().isSelected()) {
            BankAccount acc = new CreditCardAccount(first, last, 25000);
            customer.addAccount(acc);
            selectedAccounts.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }

        return String.join(", ", selectedAccounts);
    }

    private Customer createCustomer() {
        // Get inputs
        String first = view.getFirstNameField().getText().trim();
        String last  = view.getLastNameField().getText().trim();
        String dob   = view.getDobField().getText().trim();

        // Create new customer object and pass inputs
        customer = new Customer(first, last, dob);
        return customer;
    }
}