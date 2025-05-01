package view;

import util.TransactionLogger;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * View for transaction history that integrates data loading from CSV via TransactionLogger.
 */
public class TransactionHistoryView extends JPanel {
    // components
    private final JTable historyTable;

    // data loader
    private final TransactionLogger logger;
    private double runningBalance = 0.0;

    public TransactionHistoryView() {
        // instantiate logger (CSV file path as needed)
        this.logger = new TransactionLogger("transactions.csv");

        // overall layout + border
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // title
        JLabel title = new JLabel("Transaction History", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // the table
        String[] cols = {"Date", "Type", "Amount", "Balance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // all cells non-editable
            }
        };
        historyTable = new JTable(model);
        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        // initial load
        refreshHistory();
    }

    /**
     * Reloads transactions from CSV and updates the table.
     */
    public void refreshHistory() {
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        model.setRowCount(0);
        runningBalance = 0.0;
        try {
            List<Transaction> txns = logger.loadAllTransactions();
            for (Transaction tx : txns) {
                // categorize transaction type: credits increase balance, others decrease
                switch (tx.getType()) {
                    case DEPOSIT,
                         TRANSFER,
                         ADD_INVESTMENT,
                         CHARGE_TO_CARD -> runningBalance += tx.getAmount();
                    default -> runningBalance -= tx.getAmount();
                }
                // add row to table
                model.addRow(logger.toRow(tx, runningBalance));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load transactions: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // getters for wiring events in Main or other scope
    public JTable getHistoryTable() {
        return historyTable;
    }
}
