package view;

import util.TransactionLogger;
import model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TransactionHistoryView extends JPanel {

    private final JTable historyTable;
    private final TransactionLogger logger;
    private double runningBalance = 0.0;

    private JComboBox<String> periodCombo;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private JButton btnGenerate;
    private JButton btnPrint;

    public TransactionHistoryView() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel north = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.WEST;

        periodCombo = new JComboBox<>(new String[]{"Monthly", "Quarterly", "Yearly", "Custom Range"});

        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner = new JSpinner(new SpinnerDateModel());

        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));

        btnGenerate = new JButton("Generate");
        btnPrint = new JButton("Print");
        btnPrint.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 1;
        north.add(new JLabel("Period:"), gbc);

        gbc.gridx = 1;
        north.add(periodCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        north.add(new JLabel("From:"), gbc);

        gbc.gridx = 1;
        north.add(fromDateSpinner, gbc);

        gbc.gridx = 2;
        north.add(new JLabel("To:"), gbc);

        gbc.gridx = 3;
        north.add(toDateSpinner, gbc);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.add(btnGenerate);
        btnBar.add(btnPrint);

        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=4;
        gbc.anchor = GridBagConstraints.EAST;
        north.add(btnBar, gbc);

        add(north, BorderLayout.NORTH);

        String[] cols = {"Date", "Type", "Amount", "Balance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(model);

        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        logger = new TransactionLogger("transactions.csv");

        refreshHistory();

        btnGenerate.addActionListener(e -> refreshHistory());
        btnPrint.addActionListener(e -> printHistory());
    }

    public void refreshHistory() {
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        model.setRowCount(0);
        runningBalance = 0.0;

        try {
            List<Transaction> txns = logger.loadAllTransactions();
            for (Transaction tx : txns) {
                boolean isCredit = switch (tx.getType()) {
                    case DEPOSIT, TRANSFER, ADD_INVESTMENT, CHARGE_TO_CARD -> true;
                    default -> false;
                };

                runningBalance += isCredit ? tx.getAmount() : -tx.getAmount();

                model.addRow(logger.toRow(tx, runningBalance));
            }
            btnPrint.setEnabled(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading transactions: " + e.getMessage());
        }
    }

    private void printHistory() {
        try {
            boolean printed = historyTable.print();
            if (!printed) {
                JOptionPane.showMessageDialog(this, "Printing cancelled.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Printing failed: " + e.getMessage());
        }
    }

    public JTable getHistoryTable() {
        return historyTable;
    }
}
