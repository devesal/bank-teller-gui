    package controller;

    import model.*;
    import view.CustomerFormView;
    import view.MainView;


    import javax.swing.*;
    import java.awt.event.*;
    import java.util.*;
    import java.util.stream.Collectors;

    public class MainController {
        private final MainView view;
        private final List<Customer> customerList;

        public MainController() {
            view = new MainView();
            customerList = new ArrayList<>();
            initController();
        }

        private void initController() {
            setNavigationActions();

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
                        view.showPage(MainView.CUSTOMER_INFO);
                    }
                }
            });

            view.getHeader().getSearchField().addActionListener(
                    e -> searchCustomer()
            );
        }

        private void setNavigationActions() {
            setBackButtonAction();

            view.getSidebar().getBtnCustomers().addActionListener(
                    e -> {
                        view.getHeader().updateHeaderTitle("CUSTOMERS");
                        view.getHeader().showControls(true);
                        view.showPage(MainView.CUSTOMERS);
                    }
            );

            // Show Reports card
            view.getSidebar().getBtnReports().addActionListener(
                    e -> {
                        view.getHeader().updateHeaderTitle("CUSTOMERS");
                        view.showPage(MainView.REPORTS);
                    }
            );

            view.getHeader().getAddButton().addActionListener(
                    e -> {
                        new CustomerFormController(view);
                        view.setCurrentPage(MainView.ADD_CUSTOMERS);
                        view.showPage(MainView.ADD_CUSTOMERS);
                        view.getHeader().updateHeaderTitle("CUSTOMER CREATION");
                        view.getHeader().showControls(false);
                        view.getCustomerFormView().reset();
                        view.setCurrentPage(MainView.STEP_PERSONAL);
                    }
            );

            view.getHeader().getBtnSearch().addActionListener(
                    e -> searchCustomer()
            );
        }

        public void searchCustomer() {
            String query = view.getHeader().getSearchField().getText().trim();
            if (!query.isEmpty()) {
                String key = query.toLowerCase();

                if (query.matches("[a-zA-Z\\s]+")) {

                    List<BankAccount> matchingAccounts = customerList.stream()
                            .flatMap(c -> c.getAccounts().stream())
                            .filter(a -> a.getFirstName().toLowerCase().contains(key)
                                    || a.getLastName().toLowerCase().contains(key) ||
                                    a.getStatus().toLowerCase().contains(key))
                            .collect(Collectors.toList());


                    view.getCustomersView().updateTableWithSearchResults(matchingAccounts);
                } else if (query.matches("\\d+")) {
                    int accountNo = Integer.parseInt(query);
                    BankAccount matchingAccount = customerList.stream()
                            .flatMap(c -> c.getAccounts().stream())
                            .filter(a -> a.getAccountNo() == accountNo)
                            .findFirst()
                            .orElse(null);


                    if (matchingAccount != null) {
                        view.getCustomersView().updateTableWithSearchResults(
                                List.of(matchingAccount));
                    } else {
                        view.getCustomersView().updateTableWithSearchResults(Collections.emptyList());
                    }
                }
            }
        }

        private void setBackButtonAction() {
            view.getHeader().getBackButton().addActionListener(e -> {
                switch (view.getCurrentPage()) {
                    case MainView.REPORTS,
                         MainView.CUSTOMER_INFO,
                         MainView.ADD_CUSTOMERS,
                         MainView.STEP_PERSONAL,
                         MainView.STEP_SUCCESS -> {
                            view.showPage(MainView.CUSTOMERS);
                            view.setCurrentPage(MainView.CUSTOMERS);
                            view.getHeader().updateHeaderTitle("CUSTOMERS");
                            view.getHeader().showControls(true);
                    }
                    case MainView.STEP_ACCOUNTS -> {
                        // back from accounts step → personal step
                        view.getCustomerFormView().showStep(MainView.STEP_PERSONAL);
                        view.setCurrentPage(MainView.STEP_PERSONAL);
                        view.getHeader().updateHeaderTitle("CUSTOMER CREATION");
                    }
                    case MainView.TRANSACTION_HISTORY -> {
                        // sub‐page under CustomerInfo
                        view.getCustomerInfoView().showRightCard(MainView.CUSTOMER_INFO);
                        view.setCurrentPage(MainView.CUSTOMER_INFO);
                        view.getHeader().showControls(false);
                        view.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
                    }
                    case MainView.BANK_ACCOUNT -> {
                        view.getCustomerInfoView().showRightCard(MainView.CUSTOMER_INFO);
                        view.setCurrentPage(MainView.CUSTOMER_INFO);
                        view.getHeader().showControls(false);
                        view.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
                    }
                }
            });
        }

        public void start() {
            SwingUtilities.invokeLater(() -> {
                view.getFrame().setVisible(true);
                // show customers by default
                view.showPage(MainView.CUSTOMERS);
            });
        }
    }
