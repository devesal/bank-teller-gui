package edu.slu.cs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import edu.slu.cs.util.FXMLLoaderUtil;
import java.io.IOException;

public class HomeController {

    @FXML
    Button addCustomerButton;

    @FXML
    private void handleAddCustomer(ActionEvent event) {
        try {
            Parent root = FXMLLoaderUtil.load("/view/customerform/Step1.fxml");
            Stage stage = (Stage) addCustomerButton.getScene().getWindow();
            Scene scene = new Scene(root, 1240, 700);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to load Step1.fxml");
        } catch (NullPointerException e) {
            System.err.println("Could not find Step1.fxml. Check the path.");
        }
    }
}