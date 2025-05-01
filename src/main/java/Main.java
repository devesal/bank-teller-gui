import javax.swing.*;

import controller.MainController;


public class Main {
    public static void main(String[] args) {

        // Set look of UI similar to OS UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            MainController controller = new MainController();

            // == START PROGRAM == //
            controller.start();
        });
    }
}