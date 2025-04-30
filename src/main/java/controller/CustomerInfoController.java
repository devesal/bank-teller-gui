package controller;

import jdk.swing.interop.SwingInterOpUtils;
import view.CustomerFormView;
import view.CustomerInfoView;
import view.MainView;

import java.awt.event.*;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView mainView;

    public CustomerInfoController(MainView mainView) {
        this.view = mainView.getCustomerInfoView();
        this.mainView = mainView;
        initController();
    }

    private void initController() {
        // 1) “Transaction History” button -> history card
        view.getHistoryButton().addActionListener(e -> {
            view.showRightCard(CustomerInfoView.TRANSACTION_HISTORY);
            mainView.getHeader().showControls(false);
        });

        // 2) Double‐click on a row -> bank‐account card
        view.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view.showRightCard(CustomerInfoView.BANK_ACCOUNT);
                    mainView.getHeader().updateHeaderTitle("ACCOUNT DETAILS");
                    mainView.getHeader().showControls(false);
                }
            }
        });

        // 3) “Add Bank Account” -> flip back to accounts list (or launch wizard)
        view.getAddAccountButton().addActionListener(e -> {
            view.showRightCard(CustomerInfoView.BANK_ADD);
            mainView.getHeader().updateHeaderTitle("BANK ACCOUNTS");
            System.out.println(mainView.getCurrentPage());
        });
    }
}
