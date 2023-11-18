package com.example.new_login_page;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpPage implements Initializable {

    @FXML
    private Button canelButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private CheckBox femaleCheckBox;

    @FXML
    private TextField fullnameTextField;

    @FXML
    private Button goBackToLoginPage;

    @FXML
    private CheckBox maleCheckBox;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameTextField;
    @FXML
    void cancelButtonClicked(ActionEvent event) {
        System.out.println("Cancel button clicked");
        Stage stage = (Stage)signUpButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage)signUpButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToProfile(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canelButton.setOnAction(e -> {
            System.exit(0);
        });
    }
}
