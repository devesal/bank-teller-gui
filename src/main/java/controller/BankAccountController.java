// BankAccountController.java
package controller;

import view.BankAccountView;
import view.MainView;
import model.*;
import util.FileIO;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class BankAccountController {
    private final BankAccountView view;
    private final MainView mainView;
    private final BankAccount account;
    private final List<BankAccount> allAccounts;

    public BankAccountController(MainView mainView, BankAccount account, List<BankAccount> allAccounts) {
        this.mainView = mainView;
        this.view = mainView.getBankAccountView();
        this.account = account;
        this.allAccounts = allAccounts;
        initController();
//        view.loadAccountData(account, allAccounts);
    }

    private void initController() {
        // Delegates are wired in view.loadAccountData via action listeners
        // But ensure view methods call these controller methods
    }

    public void onDeposit() {
        String input = JOptionPane.showInputDialog(view, "Enter deposit amount:");
        try {
            double amount = Double.parseDouble(input);
            account.deposit(amount);
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, String.format("Deposited ₱%.2f", amount));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Deposit Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onWithdraw() {
        String input = JOptionPane.showInputDialog(view, "Enter withdrawal amount:");
        try {
            double amount = Double.parseDouble(input);
            account.withdraw(amount);
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, String.format("Withdrew ₱%.2f", amount));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Withdrawal Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onTransfer(List<BankAccount> allAccounts) {
        JTextField accField = new JTextField(10);
        JTextField amtField = new JTextField(10);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Recipient Account No:")); panel.add(accField);
        panel.add(new JLabel("Amount:")); panel.add(amtField);
        int res = JOptionPane.showConfirmDialog(view, panel, "Transfer Money", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            int targetAcc = Integer.parseInt(accField.getText().trim());
            double amount = Double.parseDouble(amtField.getText().trim());
            account.transferMoney(targetAcc, amount, new ArrayList<>(allAccounts));
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, String.format("Transferred ₱%.2f to account %d", amount, targetAcc));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Transfer Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onEncashCheck() {
        String input = JOptionPane.showInputDialog(view, "Enter check amount to encash:");
        try {
            double amount = Double.parseDouble(input);
            ((CheckingAccount) account).encashCheck(amount);
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, String.format("Encashed ₱%.2f", amount));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Encash Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onComputeMonthlyInterest() {
        try {
            ((InvestmentAccount) account).applyMonthlyInterest();
            saveAndRefresh();
            double earned = ((InvestmentAccount) account).calculateEarnedInterest();
            JOptionPane.showMessageDialog(view, String.format("Interest applied. Earned ₱%.2f", earned));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Interest Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onChargeToCard() {
        String input = JOptionPane.showInputDialog(view, "Enter charge amount:");
        try {
            double amount = Double.parseDouble(input);
            ((CreditCardAccount) account).chargeToCard(amount);
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, String.format("Charged ₱%.2f to card", amount));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Charge Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onPayCard() {
        String input = JOptionPane.showInputDialog(view, "Enter payment amount:");
        try {
            double amount = Double.parseDouble(input);
            ((CreditCardAccount) account).payCard(amount);
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, String.format("Paid ₱%.2f", amount));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Payment Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onCashAdvance() {
        String input = JOptionPane.showInputDialog(view, "Enter cash advance amount:");
        try {
            double amount = Double.parseDouble(input);
            ((CreditCardAccount) account).getCashAdvance(amount);
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, String.format("Advanced ₱%.2f", amount));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Advance Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onCloseAccount() {
        int choice = JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to close this account?",
                "Confirm Close Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (choice != JOptionPane.YES_OPTION) return;
        try {
            account.closeAccount();
            saveAndRefresh();
            JOptionPane.showMessageDialog(view, "Account closed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Close Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAndRefresh() {
        // persist all accounts
        FileIO.saveAllAccounts(new ArrayList<>(allAccounts));
        // reload view
//        view.loadAccountData(account, allAccounts);
    }
}
