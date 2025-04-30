package controller;

import model.Customer;
import model.BankAccount;
import model.CheckingAccount;
import model.CreditCardAccount;
import model.InvestmentAccount;
import view.CustomerFormView;
import view.MainView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {
    private final CustomerFormView view;
    private final MainView mainView;
    private final List<Customer> customers;

    public CustomerFormController(MainView mainView) {
        this.view = mainView.getCustomerFormView();
        this.mainView = mainView;
        this.customers = new ArrayList<>();
        initController();
    }

    private void initController() {
        view.getNextButtonStep1().addActionListener(
                e -> {
                    view.showStep(MainView.STEP_ACCOUNTS);
                    mainView.setCurrentPage(MainView.STEP_ACCOUNTS);
                }
        );

        // create and persist customer + accounts
        view.getCreateButton().addActionListener(
                e -> {
                    try {
                        Customer customer = accountCreation();
                        // populate success view
                        view.getSuccessNameLabel().setText(
                                customer.getFirstName() + " " + customer.getLastName());
                        view.getSuccessDobLabel().setText(customer.getBirthDate());
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    mainView.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
                    mainView.getHeader().showControls(false);
                    view.showStep(MainView.STEP_SUCCESS);
                    mainView.setCurrentPage(MainView.STEP_SUCCESS);
                }
        );

        view.getViewButton().addActionListener(
                e -> {
                    new CustomerInfoController(mainView);
                    mainView.showPage(MainView.CUSTOMER_INFO);
                    mainView.setCurrentPage(MainView.CUSTOMER_INFO);
                }
        );
    }

    /**
     * Builds a new Customer using the view inputs and adds toggled accounts.
     * @return the created Customer
     */
    private Customer accountCreation() {
        String first = view.getFirstNameField().getText().trim();
        String last  = view.getLastNameField().getText().trim();
        String dob   = view.getDobField().getText().trim();

        // create and store customer
        Customer customer = new Customer(first, last, dob);
        customers.add(customer);

        // create accounts
        List<String> created = new ArrayList<>();
        if (view.getSavingsToggleButton().isSelected()) {
            BankAccount acc = new BankAccount(first, last);
            customer.addAccount(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }
        if (view.getCheckingToggleButton().isSelected()) {
            BankAccount acc = new CheckingAccount(first, last, 500.0);
            customer.addAccount(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }
        if (view.getInvestmentToggleButton().isSelected()) {
            BankAccount acc = new InvestmentAccount(first, last, 5000, 0.35);
            customer.addAccount(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }
        if (view.getCreditCardToggleButton().isSelected()) {
            BankAccount acc = new CreditCardAccount(first, last, 25000);
            customer.addAccount(acc);
            created.add(acc.displayAccountType() + " (#" + acc.getAccountNo() + ")");
        }

        view.getSuccessAccountsLabel().setText(
                created.isEmpty() ? "None" : String.join(", ", created)
        );

        return customer;
    }
}