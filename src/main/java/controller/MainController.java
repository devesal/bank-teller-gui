package controller;

import view.MainView;

import javax.swing.*;
import java.awt.event.*;

public class MainController {
    private final MainView view;
    private final CustomerFormController customerFormController;

    public MainController() {
        view = new MainView();
        customerFormController = new CustomerFormController(view);
        initController();
    }

    private void initController() {
        // Show Customers card when sidebar button clicked
        view.getSidebar().getBtnCustomers().addActionListener(
                e -> {
                    view.getHeader().updateHeaderTitle("CUSTOMERS");
                    view.getHeader().showControls(true);
                    view.showPage(MainView.CUSTOMERS_VIEW);
                }
        );

        // Show Reports card
        view.getSidebar().getBtnReports().addActionListener(
                e -> {
                    view.getHeader().updateHeaderTitle("CUSTOMERS");
                    view.showPage(MainView.REPORTS_VIEW);
                }
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
                    view.showPage(MainView.CUSTOMER_INFO_VIEW);
                }
            }
        });
    }

    public void start() {
        SwingUtilities.invokeLater(() -> {
            view.getFrame().setVisible(true);
            // show customers by default
            view.showPage(MainView.CUSTOMERS_VIEW);
        });
    }
}
