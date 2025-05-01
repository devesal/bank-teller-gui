package controller;

import model.Customer;
import util.FileIO;
import view.MainView;
import view.CustomerInfoView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {
    private final MainView view;
    private final CustomerInfoView customerInfoView;
    private final List<Customer> customerList;
    private final List<BankAccount> allAccounts;
    private final CustomerInfoController customerInfoController;
    private final CustomerFormController customerFormController;


    public MainController() {
        view = new MainView();
        customerInfoView = view.getCustomerInfoView();
        customerFormController = new CustomerFormController(view);
        customerInfoController = new CustomerInfoController(view);
        customerInfoController.setOnAccountAddedCallback(this::refreshCustomerTable);
        customerList = FileIO.loadAllCustomers();       // load customers from database
        allAccounts = FileIO.loadAllAccounts();
        initController();

    }

    private void initController() {
        setNavigationActions();

        // initial load of customers
        refreshCustomerTable();

        view.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // persist data if needed
            }
        });

        // double-click row -> show info
        view.getCustomersView().getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = view.getCustomersView().getTable().getSelectedRow();
                    if (row >= 0) {
                        // map view row to customerList
                        Customer customer = customerList.get(row);
                        customerInfoController.setCurrentCustomer(customer);
                        view.showPage(MainView.CUSTOMER_INFO);
                        view.getHeader().updateHeaderTitle("ACCOUNT MANAGEMENT");
                        view.getHeader().showControls(false);
                    }
                }
            }
        });

        // search action
        ActionListener doSearch = e -> {
            String q = view.getHeader().getSearchField().getText().trim().toLowerCase();
            if (q.isEmpty()) {
                loadCustomersToTable(customerList);
            } else {
                List<Customer> filtered = new ArrayList<>();
                for (Customer c : customerList) {
                    String name = (c.getFirstName() + " " + c.getLastName()).toLowerCase();
                    if (name.contains(q)) filtered.add(c);
                }
                loadCustomersToTable(filtered);
            }
        };
        view.getHeader().getSearchField().addActionListener(doSearch);
        view.getHeader().getBtnSearch().addActionListener(doSearch);
    }

    /**
     * Populate the CustomersView table from a list of Customer objects.
     */
    private void loadCustomersToTable(List<Customer> list) {
        DefaultTableModel model = (DefaultTableModel)
                view.getCustomersView().getTable().getModel();
        model.setRowCount(0);
        Map<String, List<BankAccount>> accountsByCust = customerList.stream()
                .collect(Collectors.toMap(
                        Customer::getId,
                        Customer::getAccounts
                ));

        for (Customer cust : list) {
            String id = cust.getId();// database customer ID
            String fullName = cust.getFirstName() + " " + cust.getLastName();
            int numAccounts = accountsByCust
                    .getOrDefault(id, Collections.emptyList())
                    .size();

            model.addRow(new Object[]{ id, fullName, numAccounts });
        }
    }

    private void setNavigationActions() {
        view.getSidebar().getCustomersButton().addActionListener(e -> {
            refreshCustomerTable();
            view.getHeader().updateHeaderTitle("CUSTOMERS");
            view.getHeader().showControls(true);
            loadCustomersToTable(customerList);
            view.showPage(MainView.CUSTOMERS);
        });

        view.getSidebar().getReportsButton().addActionListener(e -> {
            view.showPage(MainView.REPORTS);
            view.getHeader().updateHeaderTitle("REPORTS");
            view.getHeader().showControls(false);
        });

        view.getHeader().getAddButton().addActionListener(e -> {
            view.setCurrentPage(MainView.ADD_CUSTOMER);
            view.showPage(MainView.ADD_CUSTOMER);
            view.getHeader().updateHeaderTitle("CUSTOMER CREATION");
            view.getHeader().showControls(false);
            view.getCustomerFormView().reset();
        });

        view.getCustomerFormView().getViewButton().addActionListener(e -> {
            Customer justCreated = customerFormController.getCurrentCustomer();
            if (justCreated != null) {
                customerInfoController.setCurrentCustomer(justCreated);
                view.showPage(MainView.CUSTOMER_INFO);
                view.setCurrentPage(MainView.CUSTOMER_INFO);
            }
        });

        view.getHeader().getBackButton().addActionListener(e ->
                view.getSidebar().getCustomersButton().doClick()
        );
    }
    public void refreshCustomerTable() {
        // 1) Reload customers and accounts from DB
        customerList.clear();
        List<Customer> allCustomers = FileIO.loadAllCustomers();
        customerList.addAll(allCustomers);
        List<BankAccount> allAccounts = FileIO.loadAllAccounts();
        allAccounts.addAll(allAccounts);
        // 2) Group accounts by customerId
        Map<String, List<BankAccount>> accountsByCust = customerList.stream()
                .collect(Collectors.toMap(
                        Customer::getId,
                        Customer::getAccounts
                ));

        DefaultTableModel model = (DefaultTableModel) view
                .getCustomersView().getTable().getModel();
        model.setRowCount(0);
        for (Customer cust : customerList) {
            model.addRow(new Object[]{
                    cust.getId(),
                    cust.getFirstName() + " " + cust.getLastName(),
                    accountsByCust.getOrDefault(cust.getId(), List.of()).size()
            });
        }

    }
    public void start() {
        SwingUtilities.invokeLater(() -> {
            loadCustomersToTable(customerList);
            view.getFrame().setVisible(true);
            view.showPage(MainView.CUSTOMERS);
        });
    }
}
