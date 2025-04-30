package view;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerInfoView extends JPanel {
    private final JLabel lblId      = new JLabel();
    private final JLabel lblName    = new JLabel();
    private final JLabel lblDob     = new JLabel();

    private final JToggleButton tglSavings    = new JToggleButton("Savings Account");
    private final JToggleButton tglChecking   = new JToggleButton("Checking Account");
    private final JToggleButton tglInvestment = new JToggleButton("Investment Account");
    private final JToggleButton tglCreditCard = new JToggleButton("Credit Card Account");
    private final JButton btnCreate           = new JButton("Create");

    private final JButton btnEdit         = new JButton("Edit Customer Info");
    private final JButton btnHistory      = new JButton("Transaction History");
    private final JButton btnStatement    = new JButton("Account Statement");
    private final JButton btnCloseAccount = new JButton("Close Account");
    private final JButton btnAddAccount   = new JButton("Add Bank Account");
    private final JTable accountsTable;

    private String currentRightCard;

    // the CardLayout and its panel
    private final CardLayout rightCardLayout = new CardLayout();
    private final JPanel rightPanel = new JPanel(rightCardLayout);

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
        JPanel detailPanel = new JPanel(new GridLayout(0,2,5,5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        detailPanel.add(new JLabel("ID:"));      detailPanel.add(lblId);
        detailPanel.add(new JLabel("Name:"));    detailPanel.add(lblName);
        detailPanel.add(new JLabel("DOB:"));     detailPanel.add(lblDob);
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

        JPanel accountsPanel = new JPanel(new BorderLayout(10,10));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnAddAccount.setPreferredSize(new Dimension(120, 40));
        headerPanel.add(btnAddAccount);
        accountsPanel.add(headerPanel, BorderLayout.NORTH);

        // the table itself
        String[] cols = {"Account No", "Type", "Status", "Balance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        accountsTable = new JTable(model);
        accountsPanel.add(new JScrollPane(accountsTable), BorderLayout.CENTER);

        // Add all cards to rightPanel
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        rightPanel.add(accountsPanel, MainView.BANK_ACCOUNTS);
        rightPanel.add(new TransactionHistoryView(), MainView.TRANSACTION_HISTORY);
        rightPanel.add(new BankAccountView(), MainView.BANK_ACCOUNT);
        rightPanel.add(buildAccountsPanel(), MainView.BANK_ACCOUNTS);

        // assemble the two halves
        add(leftPanel,  BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    public JPanel buildAccountsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("CHOOSE ACCOUNT TYPES", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.PLAIN, 20));
        p.add(title, BorderLayout.NORTH);

        // --- toggle-button list panel ---
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(10, 50, 10, 50));

        // Lock each toggle to 40px height, but allow full width
        Dimension rowSize = new Dimension(Integer.MAX_VALUE, 40);

        for (JToggleButton tgl : new JToggleButton[]{
                tglSavings, tglChecking, tglInvestment, tglCreditCard
        }) {
            tgl.setAlignmentX(Component.CENTER_ALIGNMENT);
            tgl.setMaximumSize(rowSize);
            tgl.setPreferredSize(rowSize);
            tgl.setFont(tgl.getFont().deriveFont(Font.PLAIN, 16f));
            list.add(tgl);
            list.add(Box.createVerticalStrut(10));
        }

        p.add(list, BorderLayout.CENTER);

        btnCreate.setPreferredSize(new Dimension(120, 40));

        // --- button bar, centered ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnPanel.add(btnCreate);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }
    //Controller calls this to flip the right‐hand card
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
    public String getCurrentRightCard()       { return currentRightCard; }

    // setters for labels
    public void setCustomerId(String id)         { lblId.setText(id);      }
    public void setCustomerName(String name)     { lblName.setText(name);  }
    public void setCustomerDob(String dob)       { lblDob.setText(dob);    }
    public void setCurrentRightCard(String card) { currentRightCard = card; }
}
