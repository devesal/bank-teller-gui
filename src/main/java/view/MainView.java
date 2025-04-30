package view;

import view.panels.Header;
import view.panels.Sidebar;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private JFrame           frame;
    private Sidebar          sidebar;
    private Header           header;
    private JPanel           contentPanel;
    private CardLayout       contentCardLayout;
    private ReportsView      reportsView;
    private CustomersView    customersView;
    private CustomerInfoView customerInfoView;
    private CustomerFormView customerFormView;

    // Card identifiers
    public static final String CUSTOMERS_VIEW     = "CustomersView";
    public static final String REPORTS_VIEW       = "ReportsView";
    public static final String CUSTOMER_INFO_VIEW = "CustomerInfoView";
    public static final String ADD_CUSTOMERS_VIEW = "AddCustomersView";
    public MainView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("CO-PALS Bank Teller System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        header = new Header(this);
        sidebar = new Sidebar();
        contentPanel = createContentPanel();

        frame.add(header, BorderLayout.NORTH);
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        // default view
        contentCardLayout.show(contentPanel, CUSTOMERS_VIEW);
        header.updateHeaderTitle("CUSTOMERS");

        frame.setTitle("CO-PALS Bank Information System");
        frame.setVisible(true);
    }

    private JPanel createContentPanel() {
        contentCardLayout = new CardLayout();
        JPanel panel = new JPanel(contentCardLayout);

        customersView    = new CustomersView();
        reportsView      = new ReportsView();
        customerInfoView = new CustomerInfoView();
        customerFormView = new CustomerFormView();

        panel.add(customersView, CUSTOMERS_VIEW);
        panel.add(reportsView, REPORTS_VIEW);
        panel.add(customerInfoView, CUSTOMER_INFO_VIEW);
        panel.add(customerFormView, ADD_CUSTOMERS_VIEW);

        return panel;
    }

    public void showPage(String cardName) {
        contentCardLayout.show(contentPanel, cardName);
    }

    // ==== getters for controller ====
    public Frame            getFrame()            { return frame;                      }
    public Header            getHeader()          { return header;                     }
    public Sidebar          getSidebar()          { return sidebar;                    }
    public ReportsView      getReportsView()      { return reportsView;                }
    public CustomersView    getCustomersView()    { return customersView;              }
    public CustomerInfoView getCustomerInfoView() { return customerInfoView;           }
    public CustomerFormView getCustomerFormView() { return customerFormView;           }
}