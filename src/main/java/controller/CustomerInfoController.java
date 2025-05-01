package controller;

import model.Customer;
import view.CustomerInfoView;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;
    private Customer customer;

    public CustomerInfoController(MainView mainView, Customer customer) {
        this.view = mainView.getCustomerInfoView();
        this.mainView = mainView;
        this.customer = customer;

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
}
