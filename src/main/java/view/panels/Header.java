package view.panels;

import view.MainView;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    private JButton btnBack;
    private JButton btnAdd;
    private JButton btnSearch;
    private JTextField searchField;
    private JPanel rightPanel;

    public Header(MainView view) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel logo = new JLabel("\uD83C\uDFE6 CO-PALS");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        leftPanel.add(logo);

        btnBack = new JButton("←");
        btnBack.setFocusable(false);
        leftPanel.add(btnBack);

        add(leftPanel, BorderLayout.WEST);

        // Title placeholder
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.CENTER);

        // Search & Add Customer
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(15);
        btnSearch = new JButton("Search");
        btnAdd = new JButton("ADD CUSTOMER");

        rightPanel.add(searchField);
        rightPanel.add(btnSearch);
        rightPanel.add(btnAdd);
        add(rightPanel, BorderLayout.EAST);

        // Store titleLabel for later updates
        putClientProperty("titleLabel", titleLabel);
    }

    /** Show or hide the search/add controls. */
    public void showControls(boolean visible) {
        rightPanel.setVisible(visible);
        revalidate();
        repaint();
    }

    /** Update the header’s central title. */
    public void updateHeaderTitle(String title) {
        JLabel titleLabel = (JLabel) this.getClientProperty("titleLabel");
        titleLabel.setText(title);
    }

    // Expose buttons/fields to the controller
    public JButton getBackButton()        { return btnBack;      }
    public JButton getAddButton()         { return btnAdd;       }
    public JTextField getSearchField()    { return searchField;  }
    public JButton getBtnSearch()         { return btnSearch;    }
}