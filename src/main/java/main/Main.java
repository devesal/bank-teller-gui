package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application  {
    public void start(Stage stage) {
        Parent root = new BorderPane();
        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("CO-PALS Bank Teller System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
