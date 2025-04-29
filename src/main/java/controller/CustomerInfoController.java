package controller;

import view.CustomerInfoView;
import view.MainView;

import java.awt.event.*;

public class CustomerInfoController {
    private final MainView mainView;
    private final CustomerInfoView view;

    public CustomerInfoController(MainView mainView) {
        this.mainView = mainView;
        this.view = mainView.getCustomerInfoView();
        initController();
    }

    private void initController() {
        // double-click on an account row → switch the main card to ACCOUNT_VIEW
        view.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mainView.showCard(MainView.CUSTOMER_INFO_VIEW);
                }
            }
        });

        view.getBtnEdit().addActionListener(e -> {
            // e.g. open an edit-dialog
        });
        view.getBtnHistory().addActionListener(e -> {
            // e.g. load and show transaction history
        });
        view.getBtnAddAccount().addActionListener(e -> {
            // e.g. open add-account wizard
        });
    }
}
