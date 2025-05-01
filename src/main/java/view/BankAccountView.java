package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class BankAccountView extends JPanel {
    private final JLabel titleLabel = new JLabel();
    private final JLabel lblId      = new JLabel();
    private final JLabel lblBalance = new JLabel();
    private final JLabel lblStatus  = new JLabel();

    public BankAccountView(BankAccount account) {
        setLayout(new BorderLayout(10,10));

        // — Header: title + basic info —
        JPanel headerPanel = new JPanel(new BorderLayout(5,0));
        titleLabel.setText(account.displayAccountType());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel detailPanel = new JPanel(new GridLayout(0,2,5,5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        detailPanel.add(new JLabel("Account Number:")); detailPanel.add(lblId);
        detailPanel.add(new JLabel("Balance:"));        detailPanel.add(lblBalance);
        detailPanel.add(new JLabel("Status:"));         detailPanel.add(lblStatus);
        headerPanel.add(detailPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // — Action buttons —
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actions.setBorder(BorderFactory.createTitledBorder("Transactions"));

        if (account instanceof CheckingAccount) {
            actions.add(new JButton("Deposit"));
            actions.add(new JButton("Encash Check"));
            actions.add(new JButton("Transfer Money"));
        }
        else if (account instanceof InvestmentAccount) {
            actions.add(new JButton("Deposit"));
            actions.add(new JButton("Compute Monthly Interest"));
        }
        else if (account instanceof CreditCardAccount) {
            actions.add(new JButton("Charge to Card"));
            actions.add(new JButton("Pay Card"));
            actions.add(new JButton("Get Cash Advance"));
        } else {
            actions.add(new JButton("Deposit"));
            actions.add(new JButton("Withdraw"));
            actions.add(new JButton("Transfer Money"));
        }

        actions.add(new JButton("Close Bank Account"));

        add(actions, BorderLayout.CENTER);

        // populate fields
        lblId.setText(String.valueOf(account.getAccountNo()));
        lblBalance.setText(String.format("$%.2f", account.inquireBalance()));
        lblStatus.setText(account.getStatus());
    }
}
