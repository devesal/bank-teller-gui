package controller;

import model.Customer;
import util.FileIO;
import view.MainView;
import view.CustomerInfoView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.*;

public class MainController {
    private final MainView view;
    private final List<Customer> customerList;
    private final CustomerInfoController customerInfoController;
    private final CustomerFormController customerFormController;


    public MainController() {
        view = new MainView();
        customerFormController = new CustomerFormController(view);
        customerInfoController = new CustomerInfoController(view);
        customerList = FileIO.loadAllCustomers();       // load customers from database
        initController();
        customerInfoController.setOnAccountAddedCallback(this::refreshCustomerTable);
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
        for (Customer c : list) {
            int id = c.getId();                          // database customer ID
            String fullName = c.getFirstName() + " " + c.getLastName();
            int numAccounts = c.getAccounts() != null ? c.getAccounts().size() : 0;
            model.addRow(new Object[]{ id, fullName, numAccounts, c });
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
        // 1) Reload the master list
        customerList.clear();
        customerList.addAll(FileIO.loadAllCustomers());

        // 2) Repopulate the table
        DefaultTableModel model = (DefaultTableModel) view
                .getCustomersView()
                .getTable()
                .getModel();
        model.setRowCount(0);

        for (Customer cust : customerList) {
            model.addRow(new Object[]{
                    cust.getId(),
                    cust.getFirstName() + " " + cust.getLastName(),
                    cust.getAccounts().size()
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
