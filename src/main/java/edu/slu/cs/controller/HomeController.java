package edu.slu.cs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import edu.slu.cs.util.FXMLLoaderUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.slu.cs.implementation.BankServices;

public class HomeController {
    BankServices customer = new BankServices();
    @FXML
    Button addCustomerButton;
    @FXML
    TextField searchField;


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

    @FXML
    public void searchButton(ActionEvent actionEvent) {
        searchField.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.ENTER){
                String searchText = searchField.getText().trim();


                if(searchText.matches("\\d{10}")){
                    int accountNum = Integer.parseInt(searchText);
                    customer.searchAccountByNumber(accountNum);
                }

                if(searchText.matches("[a-zA-Z\\s]+")){
                    customer.searchAccountsByName(searchText);
                }
            }
        });
    }
}