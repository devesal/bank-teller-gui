package controller;

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
        view.getBtnHistory().addActionListener(e -> {
            view.showRightCard(MainView.TRANSACTION_HISTORY);
            view.setCurrentRightCard(MainView.TRANSACTION_HISTORY);
            mainView.setCurrentPage(MainView.TRANSACTION_HISTORY);

            mainView.getHeader().showControls(false);
            mainView.getHeader().getBackButton().addActionListener(
                    f -> view.showRightCard(MainView.BANK_ACCOUNTS)
            );
        });

        // 2) Double‐click on a row -> bank‐account card
        view.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view.showRightCard(MainView.BANK_ACCOUNT);
                    view.setCurrentRightCard(MainView.BANK_ACCOUNT);
                    mainView.setCurrentPage(MainView.BANK_ACCOUNT);
                    mainView.getHeader().updateHeaderTitle("ACCOUNT DETAILS");
                    mainView.getHeader().showControls(false);
                }
            }
        });

        // 3) “Add Bank Account” -> flip back to accounts list (or launch wizard)
        view.getBtnAddAccount().addActionListener(e -> {
            view.showRightCard(MainView.BANK_ACCOUNTS);
            view.setCurrentRightCard(MainView.BANK_ACCOUNTS);
            mainView.getHeader().updateHeaderTitle("BANK ACCOUNTS");
            // or: mainView.showPage(MainView.ADD_CUSTOMERS);
        });
    }
}
