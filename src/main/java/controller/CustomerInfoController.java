package controller;

import com.sun.tools.javac.Main;
import view.CustomerInfoView;
import view.MainView;

import java.awt.event.*;

public class CustomerInfoController {
    private final CustomerInfoView view;
    private final MainView         mainView;

    public CustomerInfoController(MainView mainView) {
        this.view     = mainView.getCustomerInfoView();
        this.mainView = mainView;
        initController();
    }

    private void initController() {
        // Back button → customers card
        mainView.getHeader().getBackButton().addActionListener(
                e -> {
                    mainView.getHeader().showControls(true);
                    mainView.showPage(MainView.CUSTOMERS_VIEW);
                }
        );

        // 1) “Transaction History” button → history card
        view.getBtnHistory().addActionListener(e -> {
            view.showRightCard(CustomerInfoView.CARD_TRANSACTION_HISTORY);
            mainView.getHeader().showControls(false);
        });

        // 2) Double‐click on a row → bank‐account card
        view.getAccountsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    view.showRightCard(CustomerInfoView.CARD_BANK_ACCOUNT);
                    mainView.getHeader().updateHeaderTitle("ACCOUNT DETAILS");
                    mainView.getHeader().showControls(false);
                }
            }
        });

        // 3) “Add Bank Account” → flip back to accounts list (or launch wizard)
        view.getBtnAddAccount().addActionListener(e -> {
            view.showRightCard(CustomerInfoView.CARD_ACCOUNTS);
            mainView.getHeader().updateHeaderTitle("ACCOUNTS");
            mainView.getHeader().showControls(true);
            // or: mainView.showPage(MainView.ADD_CUSTOMERS_VIEW);
        });

    }
}
