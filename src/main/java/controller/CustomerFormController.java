package controller;

import view.CustomerFormView;
import view.MainView;

public class CustomerFormController {
    private final CustomerFormView formView;

    public CustomerFormController(MainView mainView) {
        this.formView = mainView.getCustomerFormView();
        initController();
    }

    private void initController() {
        formView.getNextButtonStep1().addActionListener(
                e ->
                    formView.showStep(CustomerFormView.STEP_ACCOUNTS)
        );
        formView.getBackButtonStep2().addActionListener(e ->
                formView.showStep(CustomerFormView.STEP_PERSONAL)
        );
        formView.getCreateButton().addActionListener(e ->
                formView.showStep(CustomerFormView.STEP_SUCCESS)
        );
    }
}