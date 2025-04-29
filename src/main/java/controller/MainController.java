package controller;

import view.MainView;

import javax.swing.*;
import java.awt.event.*;

public class MainController {
    private final MainView view;

    public MainController() {
        view = new MainView();
        initController();
    }

    private void initController() {
        // Show Customers card when sidebar button clicked
        view.getSidebar().getBtnCustomers().addActionListener(
                e -> view.showCard(MainView.CUSTOMERS_VIEW)
        );

        // Show Reports card
        view.getSidebar().getBtnReports().addActionListener(
                e -> view.showCard(MainView.REPORTS_VIEW)
        );

        // Save-on-exit stub (could hook into persistence later)
        view.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // e.g. DataManager.saveData();  // not implemented yet
            }
        });

        // On row click -> show customer info
        view.getCustomersView().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = view.getCustomersView().getTable().getSelectedRow();
                    // load customer details by ID
                    view.showCard(MainView.CUSTOMER_INFO_VIEW);
                }
            }
        });
    }

    /** Call this from your Main.java to kick things off */
    public void start() {
        SwingUtilities.invokeLater(() -> {
            view.getFrame().setVisible(true);
            // show customers by default
            view.showCard(MainView.CUSTOMERS_VIEW);
        });
    }
}
