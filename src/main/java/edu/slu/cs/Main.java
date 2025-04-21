package edu.slu.cs;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.slu.cs.util.FXMLLoaderUtil;

public class Main extends Application  {

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoaderUtil.load("/view/HomeView.fxml");
        Scene scene = new Scene(root, 1240, 700);

        stage.setTitle("CO-PALS Bank Teller System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
