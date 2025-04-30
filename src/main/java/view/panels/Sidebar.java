package view.panels;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {
    private final JButton btnReports;
    private final JButton btnCustomers;

    public Sidebar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(140, 0));

        btnCustomers = new JButton("CUSTOMERS");
        btnCustomers.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCustomers.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnReports = new JButton("REPORTS");
        btnReports.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReports.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        add(btnCustomers);
        add(Box.createVerticalStrut(10));
        add(btnReports);
    }

    public JButton getBtnCustomers() {
        return btnCustomers;
    }

    public JButton getBtnReports() {
        return btnReports;
    }
}
