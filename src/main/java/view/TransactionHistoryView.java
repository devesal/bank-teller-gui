package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionHistoryView extends JPanel {
    // components
    private final JTable historyTable;
    private final JButton  btnBack = new JButton("Back to Accounts");

    public TransactionHistoryView() {
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
        DefaultTableModel model = new DefaultTableModel(cols, 0)  {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // all cells non-editable
            }
        };

        historyTable = new JTable(model);
        add(new JScrollPane(historyTable), BorderLayout.CENTER);
    }

    // getters for controller
    public JTable getHistoryTable()    { return historyTable; }
    public JButton getBtnBack()        { return btnBack;      }
}
