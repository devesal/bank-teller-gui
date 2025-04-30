package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerInfoView extends JPanel {
    private final JLabel lblId      = new JLabel();
    private final JLabel lblName    = new JLabel();
    private final JLabel lblDob     = new JLabel();
    private final JLabel lblContact = new JLabel();

    private final JButton btnEdit       = new JButton("Edit Customer Info");
    private final JButton btnHistory    = new JButton("View Transaction History");
    private final JButton btnAddAccount = new JButton("Add New Bank Account");

    private final JTable accountsTable;

    public CustomerInfoView() {
        // 1) Main split
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // === LEFT PANEL ===
        JPanel leftPanel = new JPanel(new BorderLayout(10,10));

        //   └─ Customer details at top
        JPanel detailPanel = new JPanel(new GridLayout(0,2,5,5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        detailPanel.add(new JLabel("ID:"));      detailPanel.add(lblId);
        detailPanel.add(new JLabel("Name:"));    detailPanel.add(lblName);
        detailPanel.add(new JLabel("DOB:"));     detailPanel.add(lblDob);
        detailPanel.add(new JLabel("Contact:")); detailPanel.add(lblContact);
        leftPanel.add(detailPanel, BorderLayout.NORTH);

        //   └─ Actions vertically at bottom
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        actionPanel.add(btnEdit);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnHistory);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnAddAccount);
        leftPanel.add(actionPanel, BorderLayout.CENTER);

        // === RIGHT PANEL ===
        JPanel rightPanel = new JPanel(new BorderLayout(10,10));
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        //   └─ Header (search + add) at top
        JPanel topHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        JTextField searchField = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        topHeader.add(searchField);
        topHeader.add(btnSearch);
        rightPanel.add(topHeader, BorderLayout.NORTH);

        //   └─ Accounts table in the center
        String[] cols = {"Account No", "Type", "Status", "Balance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        accountsTable = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(accountsTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        // === assemble main view ===
        add(leftPanel,  BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
    // ==== getters for controller ====
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
