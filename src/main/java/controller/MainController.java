    package controller;

    import model.*;
    import view.MainView;


    import javax.swing.*;
    import java.awt.event.*;
    import java.util.*;
    import java.util.stream.Collectors;

    public class MainController {
        private final MainView view;
        private final CustomerFormController customerFormController;
        private final List<Customer> customerList;

        public MainController() {
            view = new MainView();
            customerFormController = new CustomerFormController(view);
            customerList = new ArrayList<>();
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

            view.getHeader().getBtnAdd().addActionListener(
                    e -> {
                        view.showPage(MainView.ADD_CUSTOMERS_VIEW);
                        view.getHeader().updateHeaderTitle("CUSTOMER CREATION");
                        view.getCustomerFormView().reset();
                    }
            );

            view.getHeader().getBtnSearch().addActionListener(e -> {
                searchCustomer();
            });
            view.getHeader().getSearchField().addActionListener(e ->{
           searchCustomer();
            });


        }

        public void searchCustomer() {
            String query = view.getHeader().getSearchField().getText().trim();
            if (!query.isEmpty()) {
                String key = query.toLowerCase();

                if (query.matches("[a-zA-Z\\s]+")) {

                    List<BankAccount> matchingAccounts = customerList.stream()
                            .flatMap(c -> c.getAccounts().stream())
                            .filter(a -> a.getFirstName().toLowerCase().contains(key)
                                    || a.getLastName().toLowerCase().contains(key))
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

        public void start() {
            SwingUtilities.invokeLater(() -> {
                view.getFrame().setVisible(true);
                // show customers by default
                view.showPage(MainView.CUSTOMERS_VIEW);

            });
        }
    }
