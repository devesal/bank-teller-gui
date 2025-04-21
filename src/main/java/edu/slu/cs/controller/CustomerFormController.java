package edu.slu.cs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import edu.slu.cs.util.FXMLLoaderUtil;

import java.io.IOException;

public class CustomerFormController {

    private static int currentPage = 1;

    @FXML
    Button backButton;

    @FXML
    Button nextButton;

    @FXML
    private void handleBack(ActionEvent event) {
        int nextPage = currentPage - 1;

        Parent root = null;

        try {
            if (nextPage < 1) {
                root = FXMLLoaderUtil.load("/view/HomeView.fxml");
            } else {
                root = loadNextPage(nextPage);
            }
        } catch (IOException e) {
            System.err.println("Failed to load " + "Step" + currentPage + " fxml");
        } catch (NullPointerException e) {
            System.err.println("Could not find " + "Step" + currentPage + " fxml. Check the path.");
        }

        currentPage--;

        Stage stage = (Stage) nextButton.getScene().getWindow();
        Scene scene = new Scene(root, 1240, 700);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleNext(ActionEvent event) {
        int nextPage = currentPage + 1;

        Parent root = null;
        try {
            root = loadNextPage(nextPage);
        } catch (IOException e) {
            System.err.println("Failed to load " + "Step" + currentPage + " fxml");
        } catch (NullPointerException e) {
            System.err.println("Could not find " + "Step" + currentPage + " fxml. Check the path.");
        }

        currentPage++;

        Stage stage = (Stage) nextButton.getScene().getWindow();
        Scene scene = new Scene(root, 1240, 700);

        stage.setScene(scene);
        stage.show();
    }

    private Parent loadNextPage(int nextPage) throws IOException, NullPointerException {
        String fxmlPath = String.format("/view/customerform/Step%d.fxml", nextPage);
        return FXMLLoaderUtil.load(fxmlPath);
    }
}
