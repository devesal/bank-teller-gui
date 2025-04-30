package view;

import model.BankAccount;

import javax.swing.*;
import java.awt.*;

public class BankAccountView extends Panel{
    private JLabel lblId      = new JLabel();
    private JLabel lblBalance = new JLabel();
    private JLabel lblStatus  = new JLabel();
    public BankAccountView() {
        setLayout(new BorderLayout(10, 10));

        JPanel detailPanel = new JPanel(new GridLayout(0,2,5,5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        detailPanel.add(new JLabel("Account Number:"));      detailPanel.add(lblId);
        detailPanel.add(new JLabel("Balance:"));             detailPanel.add(lblBalance);
        detailPanel.add(new JLabel("Status:"));              detailPanel.add(lblStatus);
        add(detailPanel, BorderLayout.NORTH);
    }
}
