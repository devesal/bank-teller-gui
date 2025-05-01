package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AccountStatementView extends JPanel {
    // filters / header
    private JLabel lblAccountNo;
    private JLabel lblHolderName;
    private JComboBox<String> periodCombo;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private JButton btnGenerate;
    private JButton btnPrint;

    // results
    private JTable txnTable;

    public AccountStatementView() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        // --- NORTH: account info + period selection ---
        JPanel north = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.WEST;

        lblAccountNo   = new JLabel("Account #: ");
        lblHolderName  = new JLabel("Holder: ");

        periodCombo = new JComboBox<>(new String[]{
                "Monthly", "Quarterly", "Yearly", "Custom Range"
        });
        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner   = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de1 = new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor de2 = new JSpinner.DateEditor(toDateSpinner,   "yyyy-MM-dd");
        fromDateSpinner.setEditor(de1);
        toDateSpinner.setEditor(de2);

        btnGenerate = new JButton("Generate");
        btnPrint    = new JButton("Print");
        btnPrint.setEnabled(false); // only enabled after generation

        layoutNorthPanel(north);

        // layout buttons
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.add(btnGenerate);
        btnBar.add(btnPrint);
        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=4;
        gbc.anchor = GridBagConstraints.EAST;
        north.add(btnBar, gbc);

        add(north, BorderLayout.NORTH);

        // --- CENTER: transaction table ---
        String[] cols = { "Date", "Type", "Description", "Amount", "Balance" };
        txnTable = new JTable(new DefaultTableModel(cols, 0));
        add(new JScrollPane(txnTable), BorderLayout.CENTER);
    }

    /** Populates the header labels with the chosen account’s info */
    public void setAccountInfo(String acctNo, String holderName) {
        lblAccountNo .setText("Account #: "    + acctNo);
        lblHolderName.setText("Holder: "        + holderName);
    }

    private void layoutNorthPanel(JPanel north) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        north.add(lblAccountNo, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        north.add(lblHolderName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        north.add(new JLabel("Period:"), gbc);

        gbc.gridx = 1; /* gbc.gridy stays 1 */
        north.add(periodCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        north.add(new JLabel("From:"), gbc);

        gbc.gridx = 1; /* gbc.gridy stays 2 */
        north.add(fromDateSpinner, gbc);

        gbc.gridx = 2; /* gbc.gridy stays 2 */
        north.add(new JLabel("To:"), gbc);

        gbc.gridx = 3; /* gbc.gridy stays 2 */
        north.add(toDateSpinner, gbc);
    }

    // getters for controller
    public JComboBox<String> getPeriodCombo()     { return periodCombo;     }
    public JSpinner         getFromDateSpinner() { return fromDateSpinner; }
    public JSpinner         getToDateSpinner()   { return toDateSpinner;   }
    public JButton          getBtnGenerate()     { return btnGenerate;     }
    public JButton          getBtnPrint()        { return btnPrint;        }
    public JTable           getTxnTable()        { return txnTable;        }
}
