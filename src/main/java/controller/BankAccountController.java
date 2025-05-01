package controller;

import view.BankAccountView;
import view.MainView;
import model.*;
import util.FileIO;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountController {
//    private final BankAccountView view;
//    private final MainView mainView;
//    private BankAccount account;
//    private final List<BankAccount> allAccounts;
//
//    public BankAccountController(MainView mainView, BankAccount account) {
//        this.mainView = mainView;
//        this.view = mainView.getBankAccountView();
//        this.account = account;
//        this.allAccounts = FileIO.loadAllAccounts();
//        initController();
//        view.loadAccountData(account);
//    }
//
//    private void initController() {
//        List<JButton> actions = new ArrayList<>();
//
//        if (account instanceof CheckingAccount) {
//            actions.add(new JButton("Deposit"));
//            actions.add(new JButton("Encash Check"));
//            actions.add(new JButton("Transfer Money"));
//        } else if (account instanceof InvestmentAccount) {
//            actions.add(new JButton("Deposit"));
//            actions.add(new JButton("Compute Monthly Interest"));
//        } else if (account instanceof CreditCardAccount) {
//            actions.add(new JButton("Charge to Card"));
//            actions.add(new JButton("Pay Card"));
//            actions.add(new JButton("Get Cash Advance"));
//        } else {
//            actions.add(new JButton("Deposit"));
//            actions.add(new JButton("Withdraw"));
//            actions.add(new JButton("Transfer Money"));
//        }
//
//        actions.add(new JButton("Close Bank Account"));
//
//        for (JButton button : actions) {
//            view.addActionButton(button);
//            switch (button.getText()) {
//                case "Deposit" -> button.addActionListener(e -> onDeposit());
//                case "Withdraw" -> button.addActionListener(e -> onWithdraw());
//                case "Transfer Money" -> button.addActionListener(e -> onTransfer());
//                case "Encash Check" -> button.addActionListener(e -> onEncashCheck());
//                case "Compute Monthly Interest" -> button.addActionListener(e -> onComputeInterest());
//                case "Charge to Card" -> button.addActionListener(e -> onChargeCard());
//                case "Pay Card" -> button.addActionListener(e -> onPayCard());
//                case "Get Cash Advance" -> button.addActionListener(e -> onCashAdvance());
//                case "Close Bank Account" -> button.addActionListener(e -> onCloseAccount());
//            }
//        }
//    }
//
//
//    private void onDeposit() {
//        String input = JOptionPane.showInputDialog(view, "Enter deposit amount:");
//        try {
//            double amount = Double.parseDouble(input);
//            account.deposit(amount);
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Deposited ₱" + amount);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error during deposit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onWithdraw() {
//        String input = JOptionPane.showInputDialog(view, "Enter withdrawal amount:");
//        try {
//            double amount = Double.parseDouble(input);
//            account.withdraw(amount);
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Withdrew ₱" + amount);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error during withdrawal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onTransfer() {
//        JTextField accField = new JTextField(10);
//        JTextField amtField = new JTextField(10);
//        JPanel panel = new JPanel();
//        panel.add(new JLabel("Recipient Account No:")); panel.add(accField);
//        panel.add(new JLabel("Amount:")); panel.add(amtField);
//        int res = JOptionPane.showConfirmDialog(view, panel, "Transfer Money", JOptionPane.OK_CANCEL_OPTION);
//        if (res != JOptionPane.OK_OPTION) return;
//        try {
//            int targetAcc = Integer.parseInt(accField.getText());
//            double amount = Double.parseDouble(amtField.getText());
//            account.transferMoney(targetAcc, amount, new ArrayList<>(allAccounts));
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Transferred ₱" + amount + " to account " + targetAcc);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error during transfer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onEncashCheck() {
//        String input = JOptionPane.showInputDialog(view, "Enter check amount to encash:");
//        try {
//            double amount = Double.parseDouble(input);
//            ((CheckingAccount) account).encashCheck(amount);
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Encashed ₱" + amount);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error during encashment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onComputeInterest() {
//        try {
//            ((InvestmentAccount) account).applyMonthlyInterest();
//            saveAndRefresh();
//            double earned = ((InvestmentAccount) account).calculateEarnedInterest();
//            JOptionPane.showMessageDialog(view, String.format("Interest applied. Earned: ₱%.2f", earned));
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error computing interest: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onChargeCard() {
//        String input = JOptionPane.showInputDialog(view, "Enter charge amount:");
//        try {
//            double amount = Double.parseDouble(input);
//            ((CreditCardAccount) account).chargeToCard(amount);
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Charged ₱" + amount + " to card");
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error during charge: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onPayCard() {
//        String input = JOptionPane.showInputDialog(view, "Enter payment amount:");
//        try {
//            double amount = Double.parseDouble(input);
//            ((CreditCardAccount) account).payCard(amount);
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Paid ₱" + amount);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error during payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onCashAdvance() {
//        String input = JOptionPane.showInputDialog(view, "Enter cash advance amount:");
//        try {
//            double amount = Double.parseDouble(input);
//            ((CreditCardAccount) account).getCashAdvance(amount);
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Advanced ₱" + amount);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error during cash advance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void onCloseAccount() {
//        int choice = JOptionPane.showConfirmDialog(
//                view,
//                "Are you sure you want to close this account?",
//                "Confirm Close Account",
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.WARNING_MESSAGE
//        );
//        if (choice != JOptionPane.YES_OPTION) return;
//        try {
//            account.closeAccount();
//            saveAndRefresh();
//            JOptionPane.showMessageDialog(view, "Account closed.");
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(view, "Error closing account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void saveAndRefresh() {
//        FileIO.saveAllAccounts(new ArrayList<>(allAccounts));
//        loadAccountData();
//    }
}
