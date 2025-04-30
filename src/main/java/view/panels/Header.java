package view.panels;

import view.MainView;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    private final JButton btnAddCustomer;

    public Header(MainView view) {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Logo
        JLabel logo = new JLabel("\uD83C\uDFE6 CO-PALS");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(logo, BorderLayout.WEST);

        // Title placeholder
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.CENTER);

        // Search & Add Customer
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField searchField = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        btnAddCustomer = new JButton("ADD CUSTOMER");

        rightPanel.add(searchField);
        rightPanel.add(btnSearch);
        rightPanel.add(btnAddCustomer);
        add(rightPanel, BorderLayout.EAST);

        // Store titleLabel for later updates
        putClientProperty("titleLabel", titleLabel);

        btnAddCustomer.addActionListener(e -> view.showPage(MainView.ADD_CUSTOMERS_VIEW));
    }

    public void updateHeaderTitle(String title) {
        JLabel titleLabel = (JLabel) this.getClientProperty("titleLabel");
        titleLabel.setText(title);
    }

    public JButton getBtnAddCustomer() {
        return btnAddCustomer;
    }
}
