package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerInfoView extends JPanel {
    // card names for the right‐hand panel
    public static final String CARD_ACCOUNTS              = "CARD_ACCOUNTS";
    public static final String CARD_TRANSACTION_HISTORY   = "CARD_TRANSACTION_HISTORY";
    public static final String CARD_BANK_ACCOUNT          = "CARD_BANK_ACCOUNT";

    private final JLabel lblId      = new JLabel();
    private final JLabel lblName    = new JLabel();
    private final JLabel lblDob     = new JLabel();
    private final JLabel lblContact = new JLabel();

    private final JButton btnEdit         = new JButton("Edit Customer Info");
    private final JButton btnHistory      = new JButton("Transaction History");
    private final JButton btnStatement    = new JButton("Account Statement");
    private final JButton btnCloseAccount = new JButton("Close Account");
    private final JButton btnAddAccount   = new JButton("Add Bank Account");
    private final JTable accountsTable;

    // the CardLayout and its panel
    private final CardLayout rightCardLayout = new CardLayout();
    private final JPanel     rightPanel      = new JPanel(rightCardLayout);

    public CustomerInfoView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        // === LEFT PANEL ===
        JPanel leftPanel = new JPanel(new BorderLayout(10,10));
        leftPanel.setPreferredSize(new Dimension(160, 0));

        // Customer details at top
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        detailPanel.add(new JLabel("ID:"), gbc);

    // ID Value
        gbc.gridx = 1;
        gbc.weightx = 1;
        detailPanel.add(lblId, gbc);

    // Name Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        detailPanel.add(new JLabel("Name:"), gbc);

    // Name Value (wrapped in HTML)
        gbc.gridx = 1;
        gbc.weightx = 1;
        lblName.setVerticalAlignment(SwingConstants.TOP);
        lblName.setText("<html><div style='width:120px;'>---</div></html>"); // this will be overwritten in setCustomerName()
        detailPanel.add(lblName, gbc);

    // DOB Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        detailPanel.add(new JLabel("DOB:"), gbc);

    // DOB Value
        gbc.gridx = 1;
        gbc.weightx = 1;
        detailPanel.add(lblDob, gbc);

    // Add to left panel
        leftPanel.add(detailPanel, BorderLayout.NORTH);

        // Action buttons down the center
        Dimension btnSize = new Dimension(120, 40);
        for (JButton b : new JButton[]{ btnEdit, btnHistory, btnStatement, btnCloseAccount }) {
            b.setPreferredSize(btnSize);
            b.setMaximumSize(btnSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        actionPanel.add(btnEdit);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnHistory);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnStatement);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnCloseAccount);
        leftPanel.add(actionPanel, BorderLayout.CENTER);

        // === RIGHT PANEL SETUP ===

        // 1) Build the “accounts” card
        JPanel accountsPanel = new JPanel(new BorderLayout(10,10));
        // header with “Add Account”
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnAddAccount.setPreferredSize(new Dimension(120, 40));
        headerPanel.add(btnAddAccount);
        accountsPanel.add(headerPanel, BorderLayout.NORTH);
        // the table itself
        String[] cols = {"Account No", "Type", "Status", "Balance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        accountsTable = new JTable(model);
        accountsPanel.add(new JScrollPane(accountsTable), BorderLayout.CENTER);

        // 2) Add all cards to rightPanel
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        rightPanel.add(accountsPanel,            CARD_ACCOUNTS);
        rightPanel.add(new TransactionHistoryView(), CARD_TRANSACTION_HISTORY);
        rightPanel.add(new BankAccountView(),        CARD_BANK_ACCOUNT);

        // assemble the two halves
        add(leftPanel,  BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    /** Controller calls this to flip the right‐hand card */
    public void showRightCard(String cardName) {
        rightCardLayout.show(rightPanel, cardName);
    }

    // getters for controller wiring
    public JTable getAccountsTable()          { return accountsTable;   }
    public JButton getBtnHistory()            { return btnHistory;      }
    public JButton getBtnAddAccount()         { return btnAddAccount;   }
    public JButton getBtnCloseAccount()       { return btnCloseAccount; }
    public JButton getBtnStatement()          { return btnStatement;    }
    public JButton getBtnEdit()               { return btnEdit;         }

    // setters for labels
    public void setCustomerId(String id)         { lblId.setText(id);      }
    public void setCustomerName(String name)     { lblName.setText(name);  }
    public void setCustomerDob(String dob)       { lblDob.setText(dob);    }

}
