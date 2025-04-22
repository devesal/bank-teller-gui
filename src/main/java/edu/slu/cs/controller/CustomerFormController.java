package edu.slu.cs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
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
        int pageIndex = currentPage - 1;

        Parent root = null;
        try {
            if (pageIndex < 1) {
                root = FXMLLoaderUtil.load("/view/HomeView.fxml");
            } else {
                root = loadNextPage(pageIndex);
            }
        } catch (IOException e) {
            System.err.println("Failed to load " + "Step" + currentPage + " fxml");
        } catch (NullPointerException e) {
            System.err.println("Could not find " + "Step" + currentPage + " fxml. Check the path.");
        }

        if (currentPage > 1) {currentPage--;}

        Stage stage = (Stage) backButton.getScene().getWindow();
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

    @FXML
    private void handleAccountSelection(ActionEvent event) {
        ToggleButton btn = (ToggleButton) event.getSource();
        if (btn.isSelected()) {
            btn.setStyle("-fx-background-color: #1F883D; -fx-text-fill: white");
        } else {
            btn.setStyle("-fx-background-color: #D1D9E0; -fx-text-fill: black");
        }
    }

    private Parent loadNextPage(int nextPage) throws IOException, NullPointerException {
        String fxmlPath = String.format("/view/customerform/Step%d.fxml", nextPage);
        return FXMLLoaderUtil.load(fxmlPath);
    }

    public void handleCreate(ActionEvent actionEvent) {
        // TODO
    }
}
