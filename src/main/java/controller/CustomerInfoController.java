package controller;
import javax.swing.text.DateFormatter;
import view.CustomerInfoView;
import view.MainView;
import model.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import util.FileIO;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;
    private final List<Customer> customers;
    private final List<BankAccount> allAccounts = FileIO.loadAllAccounts();

    private Customer currentCustomer;

    public CustomerInfoController(MainView mainView) {
        this.mainView = mainView;
        view = mainView.getCustomerInfoView();
        customers = FileIO.loadAllCustomers();
        currentCustomer = view.getCurrentCustomer();
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
            int row = view.getAccountsTable().getSelectedRow();

            int choice = JOptionPane.showConfirmDialog(
                    mainView.getFrame(),                    // <— the JFrame
                    "Are you sure you want to close this account?",
                    "Confirm Close Account",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
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

        // Pop up dialogue for editing account
        view.getEditButton().addActionListener(e -> {
            System.out.println("test");
            JFormattedTextField dob = createDateField();

            JTextField first = new JTextField(20);
            JTextField last  = new JTextField(20);

            Object[] msg = {
                    "First Name:", first,
                    "Last Name:",  last,
                    "Date of Birth:", currentCustomer.getBirthDate()
            };

            int opt = JOptionPane.showConfirmDialog(
                    view, msg, "Edit Customer", JOptionPane.OK_CANCEL_OPTION
            );
            if (opt == JOptionPane.OK_OPTION) {
                String f = first.getText().trim();
                String l = last .getText().trim();
                String d = dob  .getText().trim();
                // validate & apply...
                currentCustomer.setFirstName(f);
                currentCustomer.setLastName(l);
//                currentCustomer.getBirthDate();
//                view.setCustomerName(f + " " + l);
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
            BankAccount newAcc = switch (choice) {
                case "Checking Account (₱500 min)" -> new CheckingAccount(
                        currentCustomer.getFirstName(),
                        currentCustomer.getLastName(),
                        500.0
                );
                case "Investment Account (₱5,000, 35% rate)" -> new InvestmentAccount(
                        currentCustomer.getFirstName(),
                        currentCustomer.getLastName(),
                        5000.0,
                        0.35
                );
                case "Credit Card Account (₱25,000 limit)" -> new CreditCardAccount(
                        currentCustomer.getFirstName(),
                        currentCustomer.getLastName(),
                        25000.0
                );
                default ->  // "Savings Account"
                        new BankAccount(
                                currentCustomer.getFirstName(),
                                currentCustomer.getLastName()
                        );
            };

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

    private JFormattedTextField createDateField() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        DateFormatter df = new DateFormatter(format);
        df.setAllowsInvalid(false);
        df.setOverwriteMode(true);

        JFormattedTextField f = new JFormattedTextField(df);
        f.setValue(new Date());
        f.setColumns(10);
        f.setToolTipText("Enter date as YYYY-MM-DD");
        return f;
    }

    public void setCustomerIndex(int index) {
        view.setCustomerId(String.valueOf(index + 1));
        view.setCustomerName(currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
        view.setCustomerDob(currentCustomer.getBirthDate());
        populateAccountsTable();
    }

    /**
     * Populate view with most recently added customer.
     */
    public void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
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
        ArrayList<BankAccount> all = customers.stream()
                .flatMap(c -> c.getAccounts().stream())
                .collect(Collectors.toCollection(ArrayList::new));
        FileIO.saveAllAccounts(all);
    }
}
