package view;

import javax.swing.*;
import java.awt.*;

public class ReportsView extends JPanel {

    public ReportsView() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Reports Generation Interface (to be implemented)");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(lbl, BorderLayout.CENTER);
    }
}
