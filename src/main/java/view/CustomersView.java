package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Customer;
import model.BankAccount;

public class CustomersView extends JPanel {
    private JTable table;

    public CustomersView() {
        setLayout(new BorderLayout());

        String[] columns = {"Account No", "Name", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0)  {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // all cells non-editable
            }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }

    public void updateTableWithSearchResults(List<BankAccount> matchingAccounts) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows


        for (BankAccount account : matchingAccounts) {
            model.addRow(new Object[]{
                    account.getAccountNo(),                       // Account No
                    account.getFirstName() + " " + account.getLastName(), // Account Holder Name
                    account.getStatus()                          // Account Status
            });
            }
        }
    }
