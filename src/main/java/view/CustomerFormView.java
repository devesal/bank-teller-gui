package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomerFormView extends JPanel {
    public static final String STEP_PERSONAL  = "STEP_PERSONAL";
    public static final String STEP_ACCOUNTS  = "STEP_ACCOUNTS";
    public static final String STEP_SUCCESS   = "STEP_SUCCESS";

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    // --- Step 1: Personal Info ---
    private final JTextField txtFirstName = new JTextField(20);
    private final JTextField txtLastName  = new JTextField(20);
    private final JTextField txtDob       = new JTextField(20);
    private final JButton    btnNext1     = new JButton("Next");

    // --- Step 2: Account Types ---
    private final JToggleButton tglSavings    = new JToggleButton("Savings Account");
    private final JToggleButton tglChecking   = new JToggleButton("Checking Account");
    private final JToggleButton tglInvestment = new JToggleButton("Investment Account");
    private final JToggleButton tglCreditCard = new JToggleButton("Credit Card Account");
    private final JButton btnCreate = new JButton("Create");

    // --- Step 3: Success ---
    private final JLabel lblSuccessName     = new JLabel();
    private final JLabel lblSuccessDob      = new JLabel();
    private final JLabel lblSuccessAccounts = new JLabel();
    private final JButton btnView           = new JButton("View");

    private String currentPage;

    public CustomerFormView() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // assemble the four steps
        cardPanel.add(buildPersonalPanel(), STEP_PERSONAL);
        cardPanel.add(buildAccountsPanel(), STEP_ACCOUNTS);
        cardPanel.add(buildSuccessPanel(), STEP_SUCCESS);

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    /** Switch to one of the three steps */
    public void showStep(String stepName) {
        cardLayout.show(cardPanel, stepName);
    }

    private JPanel buildPersonalPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("FILL IN ACCOUNT INFORMATION", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.PLAIN, 20));
        p.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 1, 0, 10));
        form.setBorder(new EmptyBorder(10, 50, 10, 50));

        String[] labels = {
                "First Name",
                "Last Name",
                "Date of Birth (eg. July 30, 2005)"
        };
        JTextField[] fields = {
                txtFirstName,
                txtLastName,
                txtDob
        };

        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 12);

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(labelFont);
            form.add(lbl);

            JTextField fld = fields[i];
            fld.setFont(fieldFont);
            form.add(fld);
        }

        p.add(form, BorderLayout.CENTER);

        btnNext1.setPreferredSize(new Dimension(100, 40));

        // --- button bar, centered ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnPanel.add(btnNext1);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private JPanel buildAccountsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("CHOOSE ACCOUNT TYPES", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.PLAIN, 20));
        p.add(title, BorderLayout.NORTH);

        // --- toggle-button list panel ---
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(10, 50, 10, 50));

        // Lock each toggle to 40px height, but allow full width
        Dimension rowSize = new Dimension(Integer.MAX_VALUE, 40);

        for (JToggleButton tgl : new JToggleButton[]{
                tglSavings, tglChecking, tglInvestment, tglCreditCard
        }) {
            tgl.setAlignmentX(Component.CENTER_ALIGNMENT);
            tgl.setMaximumSize(rowSize);
            tgl.setPreferredSize(rowSize);
            tgl.setFont(tgl.getFont().deriveFont(Font.PLAIN, 16f));
            list.add(tgl);
            list.add(Box.createVerticalStrut(10));
        }

        p.add(list, BorderLayout.CENTER);

        btnCreate.setPreferredSize(new Dimension(100, 40));

        // --- button bar, centered ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnPanel.add(btnCreate);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private JPanel buildSuccessPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("ACCOUNT CREATED SUCCESSFULLY", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.PLAIN, 20));
        p.add(title, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(0, 2, 10, 10));
        details.setBorder(new EmptyBorder(10, 50, 10, 50));
        details.add(new JLabel("Name:"));            details.add(lblSuccessName);
        details.add(new JLabel("Date of Birth:"));   details.add(lblSuccessDob);
        details.add(new JLabel("Accounts:"));        details.add(lblSuccessAccounts);
        p.add(details, BorderLayout.CENTER);

        btnView.setPreferredSize(new Dimension(100, 40));

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnView);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    public void reset() {
        // --- clear step 1 fields ---
        txtFirstName.setText("");
        txtLastName .setText("");
        txtDob      .setText("");

        // --- clear step 2 toggles ---
        tglSavings   .setSelected(false);
        tglChecking  .setSelected(false);
        tglInvestment.setSelected(false);
        tglCreditCard.setSelected(false);

        // --- clear step 3 labels ---
        lblSuccessName    .setText("");
        lblSuccessDob     .setText("");
        lblSuccessAccounts.setText("");

        // --- go back to the first card ---
        showStep(STEP_PERSONAL);
    }

    // getters for controller
    public String getCurrentPage() { return currentPage; }

    public JTextField getFirstNameField()    { return txtFirstName;   }
    public JTextField getLastNameField()     { return txtLastName;    }
    public JTextField getDobField()          { return txtDob;         }
    public JButton    getNextButtonStep1()   { return btnNext1;       }

    public JToggleButton  getSavingsToggleButton()   { return tglSavings;     }
    public JToggleButton  getCheckingToggleButton()  { return tglChecking;    }
    public JToggleButton  getInvestmentToggleButton(){ return tglInvestment;  }
    public JToggleButton  getCreditCardToggleButton(){ return tglCreditCard;  }
    public JButton        getCreateButton()          { return btnCreate;      }

    public JLabel     getSuccessNameLabel()      { return lblSuccessName;     }
    public JLabel     getSuccessDobLabel()       { return lblSuccessDob;      }
    public JLabel     getSuccessAccountsLabel()  { return lblSuccessAccounts; }
    public JButton    getViewButton()            { return btnView;            }

    // setter for controller
    public void setCurrentPage(String currentPage) { this.currentPage = currentPage; }
}
