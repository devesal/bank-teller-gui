package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsView extends JPanel {
    // filter controls
    private JComboBox<String> reportTypeCombo;
    private JTextField accountField;
    private JTextField nameField;
    private JComboBox<String> txnTypeCombo;
    private JSpinner     fromDateSpinner;
    private JSpinner     toDateSpinner;
    private JButton      btnGenerate;

    // results table
    private JTable table;

    public ReportsView() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // --- North: filter panel ---
        JPanel filters = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Row 0: Report Type selector
        reportTypeCombo = new JComboBox<>(new String[]{
                "All Transactions",
                "Summary (By Type)",
                "Account Statement"
        });
        gbc.gridx=0; gbc.gridy=0; filters.add(new JLabel("Report:"), gbc);
        gbc.gridx=1;             filters.add(reportTypeCombo, gbc);

        // Row 1: Account # / Name
        accountField = new JTextField(10);
        nameField    = new JTextField(10);
        gbc.gridx=0; gbc.gridy=1; filters.add(new JLabel("Account #:"), gbc);
        gbc.gridx=1;             filters.add(accountField, gbc);
        gbc.gridx=2;             filters.add(new JLabel("Name:"), gbc);
        gbc.gridx=3;             filters.add(nameField, gbc);

        // Row 2: Transaction Type
        txnTypeCombo = new JComboBox<>(new String[]{
                "All", "Deposit", "Withdrawal", "Transfer", "Fee"
        });
        gbc.gridx=0; gbc.gridy=2; filters.add(new JLabel("Txn Type:"), gbc);
        gbc.gridx=1;             filters.add(txnTypeCombo, gbc);

        // Row 3: Date range
        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner   = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de1 = new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor de2 = new JSpinner.DateEditor(toDateSpinner,   "yyyy-MM-dd");
        fromDateSpinner.setEditor(de1);
        toDateSpinner  .setEditor(de2);
        gbc.gridx=0; gbc.gridy=3; filters.add(new JLabel("From:"), gbc);
        gbc.gridx=1;             filters.add(fromDateSpinner, gbc);
        gbc.gridx=2;             filters.add(new JLabel("To:"),   gbc);
        gbc.gridx=3;             filters.add(toDateSpinner,   gbc);

        // Row 4: Generate button
        btnGenerate = new JButton("Generate");

        gbc.gridx      = 0;
        gbc.gridy      = 4;
        gbc.gridwidth  = 4;
        gbc.anchor     = GridBagConstraints.LINE_END;
        filters.add(btnGenerate, gbc);

        add(filters, BorderLayout.NORTH);

        // --- Center: results table ---
        String[] cols = { "Date", "Account #", "Name", "Type", "Amount", "Balance" };
        table = new JTable(new DefaultTableModel(cols, 0)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // all cells non-editable
            }
        };

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // getters for controller
    public JComboBox<String> getReportTypeCombo()   { return reportTypeCombo; }
    public JTextField       getAccountField()       { return accountField;    }
    public JTextField       getNameField()          { return nameField;       }
    public JComboBox<String> getTxnTypeCombo()      { return txnTypeCombo;    }
    public JSpinner         getFromDateSpinner()    { return fromDateSpinner; }
    public JSpinner         getToDateSpinner()      { return toDateSpinner;   }
    public JButton          getBtnGenerate()        { return btnGenerate;     }
    public JTable           getResultsTable()       { return table;           }
}
