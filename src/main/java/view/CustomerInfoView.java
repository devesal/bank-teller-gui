package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerInfoView extends JPanel {
    private final JLabel lblId      = new JLabel();
    private final JLabel lblName    = new JLabel();
    private final JLabel lblDob     = new JLabel();
    private final JLabel lblContact = new JLabel();

    private final JButton btnEdit         = new JButton("Edit Customer Info");
    private final JButton btnHistory      = new JButton("Transaction History");
    private final JButton btnStatement   = new JButton("Account Statement");
    private final JButton btnCloseAccount = new JButton("Close Account");

    private final JButton btnAddAccount = new JButton("Add Bank Account");
    private final JTable accountsTable;

    public CustomerInfoView() {
        // Main split
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // LEFT PANEL
        JPanel leftPanel = new JPanel(new BorderLayout(10,10));
        leftPanel.setPreferredSize(new Dimension(140, 0));

        // Customer details at top
        JPanel detailPanel = new JPanel(new GridLayout(0,2,5,5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        detailPanel.add(new JLabel("ID:"));      detailPanel.add(lblId);
        detailPanel.add(new JLabel("Name:"));    detailPanel.add(lblName);
        detailPanel.add(new JLabel("DOB:"));     detailPanel.add(lblDob);
        detailPanel.add(new JLabel("Contact:")); detailPanel.add(lblContact);
        leftPanel.add(detailPanel, BorderLayout.NORTH);

        // Actions vertically at center

        Dimension btnSize = new Dimension(120, 40);
        for (JButton b : new JButton[]{ btnEdit, btnHistory, btnStatement, btnCloseAccount }) {
            b.setPreferredSize(btnSize);
            b.setMaximumSize(btnSize);
            b.setMinimumSize(btnSize);
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

        // === RIGHT PANEL ===
        JPanel rightPanel = new JPanel(new BorderLayout(10,10));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        btnAddAccount.setPreferredSize(new Dimension(120, 40));
        headerPanel.add(btnAddAccount);
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        // Accounts table in right panel
        String[] cols = {"Account No", "Type", "Status", "Balance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        accountsTable = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(accountsTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        // assemble main view
        add(leftPanel,  BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
    // getters for controller
    public JButton   getBtnEdit()        { return btnEdit;       }
    public JButton   getBtnHistory()     { return btnHistory;    }
    public JButton   getBtnAddAccount()  { return btnAddAccount; }
    public JTable    getAccountsTable()  { return accountsTable; }

    // and setters so controller can fill in the data:
    public void setCustomerId(String id)         { lblId.setText(id);         }
    public void setCustomerName(String name)     { lblName.setText(name);     }
    public void setCustomerDob(String dob)       { lblDob.setText(dob);       }
    public void setCustomerContact(String phone) { lblContact.setText(phone); }
}
