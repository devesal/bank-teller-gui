package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerInfoView extends JPanel {
    // components you’ll need to update at runtime:
    private final JLabel lblId      = new JLabel();
    private final JLabel lblName    = new JLabel();
    private final JLabel lblDob     = new JLabel();
    private final JLabel lblContact = new JLabel();

    // buttons for your controller to hook
    private final JButton btnEdit       = new JButton("Edit Customer Info");
    private final JButton btnHistory    = new JButton("View Transaction History");
    private final JButton btnAddAccount = new JButton("Add New Bank Account");

    // the table whose double-click you want to handle in your controller
    private final JTable accountsTable;

    public CustomerInfoView() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setLayout(new BorderLayout(10,10));

        // === Top: Customer detail panel ===
        JPanel detailPanel = new JPanel(new GridLayout(0,2,10,10));
        detailPanel.setBorder(new TitledBorder("Customer Information"));
        detailPanel.add(new JLabel("ID:"));      detailPanel.add(lblId);
        detailPanel.add(new JLabel("Name:"));    detailPanel.add(lblName);
        detailPanel.add(new JLabel("DOB:"));     detailPanel.add(lblDob);
        detailPanel.add(new JLabel("Contact:")); detailPanel.add(lblContact);

        // === Center: action buttons ===
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.add(btnEdit);
        actions.add(btnHistory);
        actions.add(btnAddAccount);

        // === Bottom: accounts table ===
        String[] cols = {"Account No", "Type", "Status", "Balance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        accountsTable = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(accountsTable);

        // assemble
        add(detailPanel, BorderLayout.NORTH);
        add(actions,     BorderLayout.CENTER);
        add(tableScroll, BorderLayout.SOUTH);
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
