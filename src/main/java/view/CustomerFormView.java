package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerFormView extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    // --- Step 1: Personal Info ---
    private final JTextField          txtFirstName = new JTextField(20);
    private final JTextField          txtLastName  = new JTextField(20);
    private final JFormattedTextField txtDob;
    private final JButton             btnNext1     = new JButton("Next");

    // --- Step 2: Account Types ---
    private final JToggleButton tglSavings    = new JToggleButton("Savings Account");
    private final JToggleButton tglChecking   = new JToggleButton("Checking Account");
    private final JToggleButton tglInvestment = new JToggleButton("Investment Account");
    private final JToggleButton tglCreditCard = new JToggleButton("Credit Card Account");
    private final JButton btnCreate           = new JButton("Create");

    // --- Step 3: Success ---
    private final JLabel lblSuccessName     = new JLabel();
    private final JLabel lblSuccessDob      = new JLabel();
    private final JTextArea lblSuccessAccounts = new JTextArea(4, 20);
    private final JScrollPane accountsScrollPane = new JScrollPane(lblSuccessAccounts);
    private final JButton btnView           = new JButton("View");

    public CustomerFormView() {
        txtDob = createDateField();

        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // assemble the steps
        cardPanel.add(buildPersonalPanel(), MainView.STEP_PERSONAL);
        cardPanel.add(buildAccountsPanel(), MainView.STEP_ACCOUNTS);
        cardPanel.add(buildSuccessPanel(),  MainView.STEP_SUCCESS);

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    public JPanel buildPersonalPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        JLabel title = new JLabel("FILL IN ACCOUNT INFORMATION", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.PLAIN, 20));
        p.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 1, 0, 10));
        form.setBorder(new EmptyBorder(10, 50, 10, 50));

        String[] labels = {
                "First Name",
                "Last Name",
                "Date of Birth (YYYY-MM-DD)"
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

            fields[i].setFont(fieldFont);
            form.add(fields[i]);
        }

        p.add(form, BorderLayout.CENTER);

        btnNext1.setPreferredSize(new Dimension(120, 40));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnPanel.add(btnNext1);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    public JPanel buildAccountsPanel() {
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

        btnCreate.setPreferredSize(new Dimension(120, 40));

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
        lblSuccessAccounts.setLineWrap(true);
        lblSuccessAccounts.setWrapStyleWord(true);
        lblSuccessAccounts.setEditable(false);
        lblSuccessAccounts.setOpaque(false);
        lblSuccessAccounts.setBorder(null);
        accountsScrollPane.setBorder(null);
        accountsScrollPane.setPreferredSize(new Dimension(400, 80)); // set scroll area size

        details.add(new JLabel("Accounts:"));
        details.add(accountsScrollPane);
        p.add(details, BorderLayout.CENTER);

        btnView.setPreferredSize(new Dimension(100, 40));

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnView);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    /** Switch to one of the three steps */
    public void showStep(String stepName) {
        cardLayout.show(cardPanel, stepName);
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
        showStep(MainView.STEP_PERSONAL);
    }

    private JFormattedTextField createDateField() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        DateFormatter df = new DateFormatter(format);
        df.setAllowsInvalid(false);
        df.setOverwriteMode(true);

        JFormattedTextField f = new JFormattedTextField(df);
        f.setValue(new Date());
        f.setColumns(10);
        f.setToolTipText("Enter date as YYYY-MM-DD");
        return f;
    }

    // getters for controller
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
    public JTextArea     getSuccessAccountsLabel()  { return lblSuccessAccounts; }
    public JButton    getViewButton()            { return btnView;            }
}
