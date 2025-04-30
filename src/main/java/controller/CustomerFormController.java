package controller;

import view.CustomerFormView;
import view.MainView;

public class CustomerFormController {
    private final CustomerFormView formView;
    private final MainView mainView;

    public CustomerFormController(MainView mainView) {
        this.formView = mainView.getCustomerFormView();
        this.mainView = mainView;
        initController();
    }

    private void initController() {
        formView.getNextButtonStep1().addActionListener(e ->
                formView.showStep(CustomerFormView.STEP_ACCOUNTS)
        );
        formView.getBackButtonStep1().addActionListener(e ->
                mainView.showPage(MainView.CUSTOMERS_VIEW)
        );
        formView.getBackButtonStep2().addActionListener(e ->
                formView.showStep(CustomerFormView.STEP_PERSONAL)
        );
        formView.getCreateButton().addActionListener(
                e -> {
                    mainView.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
                    mainView.getHeader().showControls(false);
                    formView.showStep(CustomerFormView.STEP_SUCCESS);
                }
        );
        formView.getViewButton().addActionListener(e ->
                mainView.showPage(MainView.CUSTOMER_INFO_VIEW));

    }
}