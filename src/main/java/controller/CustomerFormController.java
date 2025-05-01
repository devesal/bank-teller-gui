package controller;

import model.*;
import util.FileIO;
import view.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the customer creation form, handling multi-step
 * creation of Customer instances and associated accounts.
 * <p>
 * Captures user input for personal details and selected account types,
 * persists new customers and accounts, and provides callbacks upon
 * creation completion.
 * </p>
 *
 * @author Aquino, Theo James Coroneza
 * @author Arellano, Clendrick Joshua Mangonon
 * @author Mangonon, John Cedrick Garcia
 * @author Ong, Ron Miguel Cau
 * @author Ramos, Ricky Marc Salazar
 * @author Rosana, Jeaven Vincent Yojan Operia
 * @version 2.1
 */
public class CustomerFormController {
    private final CustomerFormView formView;
    private final MainView mainView;
    private final List<Customer> customers;
    private final List<BankAccount> allAccounts;
    private final CustomerInfoView infoView;
    private Customer currentCustomer;
    private Runnable onCustomerCreated;

    /**
     * Constructs the form controller linking form and main views,
     * loading existing customers and accounts.
     *
     * @param mainView the main application view
     */
    public CustomerFormController(MainView mainView) {
        this.formView = mainView.getCustomerFormView();
        this.infoView = mainView.getCustomerInfoView();
        this.mainView = mainView;
        this.customers = FileIO.loadAllCustomers();
        this.allAccounts = FileIO.loadAllAccounts();
        initController();
    }

    /**
     * Registers a callback to invoke after a new customer is created.
     *
     * @param cb the callback runnable
     */
    public void setOnCustomerCreatedCallback(Runnable cb) {
        this.onCustomerCreated = cb;
    }

    /**
     * Initializes button actions for navigation, creation, and toggle behavior.
     */
    private void initController() {
        formView.getNextButtonStep1().addActionListener(
                e -> {
                    formView.showStep(MainView.STEP_ACCOUNTS);
                    mainView.setCurrentPage(MainView.STEP_ACCOUNTS);
                }
        );

        formView.getCreateButton().addActionListener(e -> {
            Customer customer = createCustomer();
            customers.add(customer);
            currentCustomer = customer;
            FileIO.saveAllCustomers(customers);
            FileIO.saveAllAccounts(new ArrayList<>(allAccounts));

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
     * Builds and returns a new Customer based on form inputs,
     * creates selected account types, and updates view.
     *
     * @return the newly created Customer
     */
    private Customer createCustomer() {
        String first = formView.getFirstNameField().getText().trim();
        String last  = formView.getLastNameField().getText().trim();
        String dob   = formView.getDobField().getText().trim();

        Customer customer = new Customer(first, last, dob);
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

        System.out.println("Saved " + customers.size() + " customers and " + allAccounts.size() + " accounts.");
        return customer;
    }

    /**
     * Ensures JToggleButtons can be unselected by consecutive clicks.
     *
     * @param button the toggle button to configure
     */
    private void setupUntoggleBehavior(JToggleButton button) {
        button.addActionListener(e -> {
            if (button.isSelected()) {
                button.putClientProperty("wasSelected", true);
            } else {
                Object wasSelected = button.getClientProperty("wasSelected");
                if (Boolean.TRUE.equals(wasSelected)) {
                    button.setSelected(false);
                    button.putClientProperty("wasSelected", false);
                }
            }
        });
    }

    /**
     * Returns the most recently created Customer.
     *
     * @return the current Customer instance
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}
