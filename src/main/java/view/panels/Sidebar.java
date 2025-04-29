package view.panels;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {
    private JButton btnReports;
    private JButton btnCustomers;

    public Sidebar() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnCustomers = new JButton("CUSTOMERS");
        btnCustomers.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCustomers.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnReports = new JButton("REPORTS");
        btnReports.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReports.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        this.add(btnCustomers);
        this.add(Box.createVerticalStrut(10));
        this.add(btnReports);
    }

    public JButton getBtnCustomers() {
        return btnCustomers;
    }

    public JButton getBtnReports() {
        return btnReports;
    }
}
