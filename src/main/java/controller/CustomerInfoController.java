package controller;
import javax.swing.text.DateFormatter;

import util.TransactionLogger;
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
    private final TransactionLogger transactionLogger;

    public CustomerInfoController(MainView mainView) {
        this.mainView = mainView;
        view = mainView.getCustomerInfoView();
        customers = FileIO.loadAllCustomers();
        allAccounts = FileIO.loadAllAccounts();
        transactionLogger = new TransactionLogger("transactions.csv");

        initController();
    }

    private void initController() {

        view.getHistoryButton().addActionListener(
                e -> {
                    view.showRightCard(CustomerInfoView.TRANSACTION_HISTORY);
                    mainView.getHeader().showControls(false);
                    view.getHistoryView().refreshHistory();
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
                // validate & apply...
                currentCustomer.setFirstName(f);
                currentCustomer.setLastName(l);
                view.setCustomerName(f + " " + l);
            }
        });


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


                    if ("closed".equalsIgnoreCase(account.getStatus())) {
                        JOptionPane.showMessageDialog(
                                view,
                                String.format(
                                        "<html><font color='red'><b>Notice:</b> Account #%d is CLOSED.<br>No further transactions are allowed.</font></html>",
                                        accNo
                                ),
                                "Account Closed",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }


                    String[] operations = buildOperations(account);
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

                    executeOperation(choice, account);
                }
            }
        });


        view.getAddAccountButton().addActionListener(e -> {
            if (currentCustomer == null) {
                JOptionPane.showMessageDialog(view,
                        "No customer selected!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


            String[] options = {
                    "Savings Account",
                    "Checking Account (₱500 min)",
                    "Investment Account (₱5,000, 35% rate)",
                    "Credit Card Account (₱25,000 limit)"
            };


            String choice = (String) JOptionPane.showInputDialog(
                    view,                                // parent
                    "Select account type to add:",      // message
                    "Add Bank Account",                 // title
                    JOptionPane.PLAIN_MESSAGE,
                    null,                                // icon
                    options,                             // choices
                    options[0]                           // default
            );


            if (choice == null) return;


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

            newAcc.setCustomerId(currentCustomer.getId());

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

        // Update global accounts list to maintain consistency
        allAccounts.clear();
        allAccounts.addAll(all);

        FileIO.saveAllAccounts((ArrayList<BankAccount>) allAccounts);
    }

    public void setOnAccountAddedCallback(Runnable cb) {
        onAccountAddedCallback = cb;
    }

    private String[] buildOperations(BankAccount account) {
        if (account instanceof CheckingAccount) {
            return new String[]{"Deposit", "Encash Check", "Close Account"};
        } else if (account instanceof InvestmentAccount) {
            return new String[]{"Deposit", "Compute Interest", "Apply Monthly Interest", "Close Account"};
        } else if (account instanceof CreditCardAccount) {
            return new String[]{"Charge Card", "Pay Card", "Cash Advance", "Inquire Available Credit", "Close Account"};
        } else {
            return new String[]{"Deposit", "Withdraw", "Transfer", "Close Account"};
        }
    }

    private void executeOperation(String choice, BankAccount acc) {
        try {
            switch (choice) {
                case "Deposit" -> dialogDeposit(acc);
                case "Withdraw" -> dialogWithdraw(acc);
                case "Transfer" -> dialogTransfer(acc);
                case "Encash Check" -> dialogEncash((CheckingAccount) acc);
                case "Compute Interest" -> dialogComputeInterest((InvestmentAccount) acc);
                case "Apply Monthly Interest" -> dialogApplyInterest((InvestmentAccount) acc);
                case "Charge Card" -> dialogCharge((CreditCardAccount) acc);
                case "Pay Card" -> dialogPay((CreditCardAccount) acc);
                case "Cash Advance" -> dialogAdvance((CreditCardAccount) acc);
                case "Inquire Available Credit" -> dialogInquireCredit((CreditCardAccount) acc);
                case "Close Account" -> dialogClose(acc);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        FileIO.saveAllCustomers(customers);
        FileIO.saveAllAccounts(new ArrayList<>(allAccounts));
        populateAccountsTable();
    }

    private void dialogDeposit(BankAccount acc) throws AccountClosedException {
        String s = JOptionPane.showInputDialog(view, "Deposit amount:");
        double amt = Double.parseDouble(s);
        acc.deposit(amt);
        recordTransaction(acc.getAccountNo(), amt, Transaction.Type.DEPOSIT);
        saveAll();
        JOptionPane.showMessageDialog(view, String.format("Deposited ₱%.2f", amt));
    }

    private void dialogWithdraw(BankAccount acc) throws Exception {
        String s = JOptionPane.showInputDialog(view, "Withdraw amount:");
        double amt = Double.parseDouble(s);
        recordTransaction(acc.getAccountNo(), amt, Transaction.Type.WITHDRAWAL);
        acc.withdraw(amt);
        saveAll();
        JOptionPane.showMessageDialog(view, String.format("Withdrew ₱%.2f", amt));
    }

    private void dialogTransfer(BankAccount acc) throws Exception {
        JTextField to = new JTextField(8);
        JTextField amt = new JTextField(8);
        JPanel p = new JPanel();

        p.add(new JLabel("To Acc #:")); p.add(to);
        p.add(new JLabel("Amt:")); p.add(amt);

        int r = JOptionPane.showConfirmDialog(view, p, "Transfer", JOptionPane.OK_CANCEL_OPTION);
        if (r!=JOptionPane.OK_OPTION) return;
        int toNo = Integer.parseInt(to.getText().trim());

        BankAccount dest = allAccounts.stream().filter(a->a.getAccountNo()==toNo).findFirst().orElse(null);

        if (dest==null) throw new InvalidAmountException("Destination not found");

        if (dest instanceof CreditCardAccount) throw new InvalidAmountException("Cannot transfer to credit card");

        double amount = Double.parseDouble(amt.getText().trim());
        acc.transferMoney(toNo, amount, new ArrayList<>(allAccounts));
        recordTransaction(acc.getAccountNo(), toNo, amount, Transaction.Type.TRANSFER);
        JOptionPane.showMessageDialog(view, String.format("Transferred ₱%.2f to #%d", amount,toNo));
    }

    private void dialogEncash(CheckingAccount acc) throws Exception {
        String s = JOptionPane.showInputDialog(view, "Encash amount:");
        double amt = Double.parseDouble(s);
        acc.encashCheck(amt);
        saveAll();
        recordTransaction(acc.getAccountNo(), amt, Transaction.Type.ENCASH);
        JOptionPane.showMessageDialog(view, String.format("Encashed ₱%.2f", amt));
    }

    private void dialogComputeInterest(InvestmentAccount acc) {
        double interest = acc.calculateEarnedInterest();
        JOptionPane.showMessageDialog(view, String.format("Interest: ₱%.2f", interest));
    }

    private void dialogApplyInterest(InvestmentAccount acc) throws AccountClosedException {
        acc.applyMonthlyInterest();
        recordTransaction(acc.getAccountNo(), acc.calculateEarnedInterest(), Transaction.Type.ADD_INVESTMENT);
        saveAll();
        JOptionPane.showMessageDialog(view, "Monthly interest applied.");
    }

    private void dialogCharge(CreditCardAccount acc) throws Exception {
        String s = JOptionPane.showInputDialog(view, "Charge amount:");
        double amt = Double.parseDouble(s);
        recordTransaction(acc.getAccountNo(), amt, Transaction.Type.CHARGE_TO_CARD);
        acc.chargeToCard(amt);
        saveAll();
        JOptionPane.showMessageDialog(view, String.format("Charged ₱%.2f", amt));
    }

    private void dialogPay(CreditCardAccount acc) throws Exception {
        String s = JOptionPane.showInputDialog(view, "Payment amount:");
        double amt = Double.parseDouble(s);
        acc.payCard(amt);
        saveAll();
        JOptionPane.showMessageDialog(view, String.format("Paid ₱%.2f", amt));
    }

    private void dialogAdvance(CreditCardAccount acc) throws Exception {
        String s = JOptionPane.showInputDialog(view, "Advance amount:");
        double amt = Double.parseDouble(s);
        acc.getCashAdvance(amt);
        recordTransaction(acc.getAccountNo(), amt, Transaction.Type.PAY_CARD);
        saveAll();
        JOptionPane.showMessageDialog(view, String.format("Advanced ₱%.2f", amt));
    }

    private void dialogInquireCredit(CreditCardAccount acc) {
        double avail = acc.inquireAvailableCredit();
        JOptionPane.showMessageDialog(view, String.format("Available credit: ₱%.2f", avail));
    }

    private void dialogClose(BankAccount acc) throws Exception {
        if (acc instanceof CreditCardAccount) {
            CreditCardAccount c = (CreditCardAccount) acc;
            if (c.getCharges() > 0) {
                throw new InvalidAmountException("Settle charges before closing");
            }
        }
        acc.closeAccount();
        JOptionPane.showMessageDialog(view, String.format("Account #%d closed", acc.getAccountNo()));
    }


    // Helper to record transactions
    private void recordTransaction(int fromAcc, double amount, Transaction.Type type) {
        try {
            Transaction tx = new Transaction(fromAcc, amount, type);
            transactionLogger.logTransaction(tx, currentCustomer.getAccounts()
                    .stream()
                    .filter(a -> a.getAccountNo() == fromAcc)
                    .findFirst().orElseThrow().inquireBalance()
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Failed to log transaction: " + e.getMessage(),
                    "Logging Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void recordTransaction(int fromAcc, int toAcc, double amount, Transaction.Type type) {
        try {
            Transaction tx = new Transaction(fromAcc, toAcc, amount, type);
            recordTransaction(fromAcc, amount, type);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Failed to log transaction: " + e.getMessage(),
                    "Logging Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
