package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomerFormView extends JPanel {
    public static final String STEP_PERSONAL  = "STEP_PERSONAL";
    public static final String STEP_ACCOUNTS  = "STEP_ACCOUNTS";
    public static final String STEP_SUCCESS   = "STEP_SUCCESS";

    private final CardLayout cardLayout;
    private final JPanel      cardPanel;

    // --- Step 1: Personal Info ---
    private final JTextField txtFirstName = new JTextField(20);
    private final JTextField txtLastName  = new JTextField(20);
    private final JTextField txtDob       = new JTextField(20);
    private final JButton    btnNext1     = new JButton("Next");

    // --- Step 2: Account Types ---
    private final JCheckBox  chkSavings    = new JCheckBox("Savings Account");
    private final JCheckBox  chkChecking   = new JCheckBox("Checking Account");
    private final JCheckBox  chkInvestment = new JCheckBox("Investment Account");
    private final JCheckBox  chkCreditCard = new JCheckBox("Credit Card Account");
    private final JButton    btnBack2      = new JButton("Back");
    private final JButton    btnCreate     = new JButton("Create");

    // --- Step 3: Success ---
    private final JLabel lblSuccessName     = new JLabel();
    private final JLabel lblSuccessDob      = new JLabel();
    private final JLabel lblSuccessPhone    = new JLabel();
    private final JLabel lblSuccessAccounts = new JLabel();
    private final JButton btnView           = new JButton("View");

    public CustomerFormView() {
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

    /** Switch to one of the four steps */
    public void showStep(String stepName) {
        cardLayout.show(cardPanel, stepName);
    }

    private JPanel buildPersonalPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("FILL IN ACCOUNT INFORMATION", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        p.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 1, 0, 10));
        form.setBorder(new EmptyBorder(10, 50, 10, 50));
        form.add(new JLabel("First Name"));
        form.add(txtFirstName);
        form.add(new JLabel("Last Name"));
        form.add(txtLastName);
        form.add(new JLabel("Date of Birth"));
        form.add(txtDob);
        p.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnNext1);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private JPanel buildAccountsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("CHOOSE ACCOUNT TYPES", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        p.add(title, BorderLayout.NORTH);

        // --- checkbox list panel ---
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(10, 50, 10, 50));

        // Desired height for each “row”
        Dimension rowSize = new Dimension(Integer.MAX_VALUE, 40);

        // Configure each checkbox
        for (JCheckBox cb : new JCheckBox[]{ chkSavings, chkChecking, chkInvestment, chkCreditCard }) {
            cb.setAlignmentX(Component.CENTER_ALIGNMENT);
            cb.setMaximumSize(rowSize);      // fills width, locks height
            cb.setPreferredSize(rowSize);    // suggests our 40px height
            cb.setFont(cb.getFont().deriveFont(Font.PLAIN, 16f));  // slightly larger text
            list.add(cb);
            list.add(Box.createVerticalStrut(10));
        }

        p.add(list, BorderLayout.CENTER);

        // --- button bar ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.add(btnBack2);
        btnPanel.add(btnCreate);
        // add a little padding above the buttons
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private JPanel buildSuccessPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("ACCOUNT CREATED SUCCESSFULLY", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        p.add(title, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(0, 2, 10, 10));
        details.setBorder(new EmptyBorder(10, 50, 10, 50));
        details.add(new JLabel("Name:"));            details.add(lblSuccessName);
        details.add(new JLabel("Date of Birth:"));   details.add(lblSuccessDob);
        details.add(new JLabel("Phone number:"));    details.add(lblSuccessPhone);
        details.add(new JLabel("Accounts:"));        details.add(lblSuccessAccounts);
        p.add(details, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnView);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    public JTextField getFirstNameField()    { return txtFirstName;   }
    public JTextField getLastNameField()     { return txtLastName;    }
    public JTextField getDobField()          { return txtDob;         }
    public JButton    getNextButtonStep1()   { return btnNext1;       }

    public JButton    getBackButtonStep2()   { return btnBack2;       }
    public JCheckBox  getSavingsCheckbox()   { return chkSavings;     }
    public JCheckBox  getCheckingCheckbox()  { return chkChecking;    }
    public JCheckBox  getInvestmentCheckbox(){ return chkInvestment;  }
    public JCheckBox  getCreditCardCheckbox(){ return chkCreditCard;  }
    public JButton    getCreateButton()      { return btnCreate;      }

    public JLabel     getSuccessNameLabel()      { return lblSuccessName;     }
    public JLabel     getSuccessDobLabel()       { return lblSuccessDob;      }
    public JLabel     getSuccessPhoneLabel()     { return lblSuccessPhone;    }
    public JLabel     getSuccessAccountsLabel()  { return lblSuccessAccounts; }
    public JButton    getViewButton()            { return btnView;            }
}
