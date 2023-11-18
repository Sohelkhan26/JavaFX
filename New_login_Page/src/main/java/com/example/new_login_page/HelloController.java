package com.example.new_login_page;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    Button signUpButton;
    @FXML
    Label messageLabel;
    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordPasswordField;
    Connection connection;
    @FXML
    TextField passwordPasswordFieldSignUp;
    @FXML
    TextField usernameTextFieldSignUp;
    @FXML
    Label messageLabelSignUp;
    Alert alert;
    public void loginButtonClicked() throws IOException, SQLException {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        if(usernameTextField.getText().isEmpty() || passwordPasswordField.getText().isEmpty())
        {
            messageLabel.setText("Username and Password shouldn't be blank");
            return;
        }

        if(printDataFromDB(username , password)) {
            String command = "INSERT INTO customer_count (date , count) VALUES (? , ?)";
            DBconnection dBconnection = new DBconnection();

            Connection connection1 = dBconnection.getDBConnection();
            PreparedStatement statement = connection1.prepareStatement(command);
            statement.setString(1 , String.valueOf(LocalDate.now()));
            statement.setString(2 , "1");
            statement.executeUpdate();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            StackPane stackPane = loader.load();

            dashboardController db = loader.getController();

            Scene scene = new Scene(stackPane, 1100, 800);
            Stage stage1 = (Stage) messageLabel.getScene().getWindow();
            stage1.hide();
            Stage stage = new Stage();
            stage.setScene(scene);
            db.setName(usernameTextField.getText());

            stage.show();
        }
    }
    public void signUpButtonClicked() throws SQLException {

        DBconnection dBconnection = new DBconnection();
        connection = dBconnection.getDBConnection();

        String command = "SELECT * FROM username_password WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(command);
        statement.setString(1 , usernameTextFieldSignUp.getText());

        ResultSet resultSet = statement.executeQuery();

        if(usernameTextFieldSignUp.getText().isBlank() || passwordPasswordFieldSignUp.getText().isBlank())
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in username and password");
            alert.showAndWait();
        }
        else if(resultSet.next())
        {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("This username exists! Please enter another username");
            alert.showAndWait();
        }
        else {

            String addCommand = "INSERT INTO username_password (username , password) VALUES (? , ?)";
            statement = connection.prepareStatement(addCommand);
            statement.setString(1 , usernameTextFieldSignUp.getText());
            statement.setString(2 , passwordPasswordFieldSignUp.getText());
            statement.executeUpdate();

            String newCommand = "INSERT INTO customer_count (date , count) VALUES (? , ?)";
            PreparedStatement statement1 = connection.prepareStatement(newCommand);
            statement1.setString(1 , String.valueOf(LocalDate.now()));

            statement1.setString(2 , "1");
            int row = statement1.executeUpdate();

            if(row > 0)
                System.out.println("customer_count");

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Successfully signed up!");
            alert.showAndWait();
        }
    }
    public void setCancelButtonClicked()
    {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }
    public boolean printDataFromDB(String username , String password) throws SQLException
    {
        DBconnection dBconnection = new DBconnection();
        connection = dBconnection.getDBConnection();
        String getSpecificRow = "SELECT * FROM username_password WHERE username = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(getSpecificRow);
        statement.setString(1 , username);
        statement.setString(2 , password);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }
    public void signUpClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signUpPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage)usernameTextField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}