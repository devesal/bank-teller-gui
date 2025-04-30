package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class  CustomersView extends JPanel {

    JTable table;

    public CustomersView() {
        setLayout(new BorderLayout());

        String[] cols = {"Account No", "Name", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        add(scroll, BorderLayout.CENTER);
    }

    public JTable getTable() { return table; }
}
