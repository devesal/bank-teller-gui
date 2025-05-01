package controller;
import javax.swing.text.DateFormatter;
import view.CustomerInfoView;
import view.MainView;
import model.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import util.Exceptions.*;
import util.FileIO;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;
    private final List<Customer> customers;
    private final List<BankAccount> allAccounts;
    private Runnable onAccountAddedCallback;
    private Customer currentCustomer;
    public CustomerInfoController(MainView mainView) {
        this.mainView = mainView;
        view = mainView.getCustomerInfoView();
        customers = FileIO.loadAllCustomers();
        allAccounts = FileIO.loadAllAccounts();
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

            if (row < 0) {
                JOptionPane.showMessageDialog(
                        view,
                        "Please select a bank account to close.",
                        "No Account Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    view,
                    "Are you sure you want to close this account?",
                    "Confirm Close",
                    JOptionPane.YES_NO_OPTION
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
                view.setCustomerName(f + " " + l);
            }
        });

        // 2) Double‐click on a row -> bank‐account card
        view.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = view.getAccountsTable().getSelectedRow();
                    if (row < 0 || currentCustomer == null) return;
                    int accNo = (int) view.getAccountsTable().getValueAt(row, 0);
                    BankAccount account = currentCustomer.getAccounts().stream()
                            .filter(a -> a.getAccountNo() == accNo)
                            .findFirst().orElse(null);
                    if (account == null) return;

                    // Build operations list
                    String[] operations = buildOperations(account);
                    // Prompt operation selection with HTML list for clarity
                    String choice = (String) JOptionPane.showInputDialog(
                            view,
                            String.format("<html><b>Select operation for account %d</b></html>", accNo),
                            "Account Operations",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            operations,
                            operations[0]
                    );
                    if (choice == null) return;

                    // Execute and handle
                    executeOperation(choice, account);
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
            // —— TAG THE NEW ACCOUNT WITH ITS OWNER’S ID ——
            newAcc.setCustomerId(currentCustomer.getId());

            // 5) Attach to customer and global list, persist & refresh
            List<BankAccount> all = FileIO.loadAllAccounts();
            currentCustomer.addAccount(newAcc);
            all.add(newAcc);
            FileIO.saveAllCustomers(customers);
            FileIO.saveAllAccounts(new ArrayList<>(all));      // refresh the JTable
            populateAccountsTable();
            // 6) Inform user
            JOptionPane.showMessageDialog(view,
                    String.format("%s added!\nAccount No: %d",
                            newAcc.displayAccountType(),
                            newAcc.getAccountNo()),
                    "Account Created",
                    JOptionPane.INFORMATION_MESSAGE
            );
            if (onAccountAddedCallback != null) {
                onAccountAddedCallback.run();
            }
            System.out.println("Saved " + customers.size() + " customers and " + allAccounts.size() + " accounts.");
        });

    }

    public static JFormattedTextField createDateField() {
        return getjFormattedTextField();
    }

    public static JFormattedTextField getjFormattedTextField() {
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
    /**
     +     * Show a given customer’s info (ID, name, DOB) and their accounts.
     +     */
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        if (customer == null) return;

        // Use the real ID string:
        view.setCustomerId(customer.getId());
        view.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        view.setCustomerDob(customer.getBirthDate());

        populateAccountsTable();
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
    public void setOnAccountAddedCallback(Runnable cb) {
        onAccountAddedCallback = cb;
    }

    /**
     * Builds a list of operations available for the given account.
     *
     * @param account the account to build operations for
     * @return an array of operation strings
     */
    private String[] buildOperations(BankAccount account) {
        if (account instanceof CheckingAccount) {
            return new String[]{"Deposit", "Withdraw", "Transfer", "Encash Check", "Close Account"};
        } else if (account instanceof InvestmentAccount) {
            return new String[]{"Deposit", "Compute Interest", "Close Account"};
        } else if (account instanceof CreditCardAccount) {
            return new String[]{"Charge Card", "Pay Card", "Cash Advance", "Close Account"};
        } else {
            return new String[]{"Deposit", "Withdraw", "Transfer", "Close Account"};
        }
    }

    private void executeOperation(String choice, BankAccount account) {
        try {
            switch (choice) {
                case "Deposit" -> dialogDeposit(account);
                case "Withdraw" -> dialogWithdraw(account);
                case "Transfer" -> dialogTransfer(account);
                case "Encash Check" -> dialogEncash(account);
                case "Compute Interest" -> dialogInterest((InvestmentAccount) account);
                case "Charge Card" -> dialogCharge((CreditCardAccount) account);
                case "Pay Card" -> dialogPay((CreditCardAccount) account);
                case "Cash Advance" -> dialogAdvance((CreditCardAccount) account);
                case "Close Account" -> dialogClose(account);
            }
        } catch (AccountClosedException | InsufficientFundsException | TransactionLimitException | InvalidAmountException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    String.format("<html><font color='red'><b>Error:</b> %s</font></html>", ex.getMessage()),
                    "Transaction Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(
                    view,
                    "<html><font color='red'>Invalid number format. Please enter a valid amount.</font></html>",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        // Persist and refresh
        FileIO.saveAllCustomers(customers);
        FileIO.saveAllAccounts(new ArrayList<>(allAccounts));
        populateAccountsTable();
    }

    private void dialogDeposit(BankAccount acc) throws AccountClosedException {
        String amt = JOptionPane.showInputDialog(view, "Enter deposit amount:");
        double amount = Double.parseDouble(amt);
        acc.deposit(amount);
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Deposited ₱%.2f</html>", amount),
                "Deposit Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogWithdraw(BankAccount acc) throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        String amt = JOptionPane.showInputDialog(view, "Enter withdrawal amount:");
        double amount = Double.parseDouble(amt);
        acc.withdraw(amount);
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Withdrew ₱%.2f</html>", amount),
                "Withdrawal Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogTransfer(BankAccount acc) throws AccountClosedException, InvalidAmountException {
        JTextField to = new JTextField(5);
        JTextField amt = new JTextField(5);
        JPanel p = new JPanel();
        p.add(new JLabel("To Account:")); p.add(to);
        p.add(new JLabel("Amount:")); p.add(amt);
        int res = JOptionPane.showConfirmDialog(view, p, "Transfer Funds", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        int toAcc = Integer.parseInt(to.getText().trim());
        double amount = Double.parseDouble(amt.getText().trim());
        acc.transferMoney(toAcc, amount, new ArrayList<>(allAccounts));
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Transferred ₱%.2f to account %d</html>", amount, toAcc),
                "Transfer Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogEncash(BankAccount acc) throws InsufficientFundsException, AccountClosedException, TransactionLimitException {
        String amt = JOptionPane.showInputDialog(view, "Enter check encash amount:");
        double amount = Double.parseDouble(amt);
        ((CheckingAccount) acc).encashCheck(amount);
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> En cashed ₱%.2f</html>", amount),
                "Encashment Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogInterest(InvestmentAccount acc) throws AccountClosedException {
        acc.applyMonthlyInterest();
        double earned = acc.calculateEarnedInterest();
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Interest applied. Earned ₱%.2f</html>", earned),
                "Interest Applied",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogCharge(CreditCardAccount acc) throws AccountClosedException, TransactionLimitException {
        String amt = JOptionPane.showInputDialog(view, "Enter charge amount:");
        double amount = Double.parseDouble(amt);
        acc.chargeToCard(amount);
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Charged ₱%.2f</html>", amount),
                "Charge Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogPay(CreditCardAccount acc) throws AccountClosedException, TransactionLimitException {
        String amt = JOptionPane.showInputDialog(view, "Enter payment amount:");
        double amount = Double.parseDouble(amt);
        acc.payCard(amount);
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Paid ₱%.2f</html>", amount),
                "Payment Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogAdvance(CreditCardAccount acc) throws AccountClosedException, TransactionLimitException {
        String amt = JOptionPane.showInputDialog(view, "Enter cash advance amount:");
        double amount = Double.parseDouble(amt);
        acc.getCashAdvance(amount);
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Advanced ₱%.2f</html>", amount),
                "Cash Advance Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void dialogClose(BankAccount acc) throws AccountClosedException, InsufficientFundsException, TransactionLimitException {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                String.format("<html>Are you sure you want to close account %d?</html>", acc.getAccountNo()),
                "Confirm Close",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;
        acc.closeAccount();
        JOptionPane.showMessageDialog(
                view,
                String.format("<html><b>Success:</b> Account %d closed.</html>", acc.getAccountNo()),
                "Account Closed",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
