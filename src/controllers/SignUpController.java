package controllers;

import com.sun.tools.javac.Main;
import database.DBHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField nameTF;
    @FXML
    private PasswordField passwordTF;
    @FXML
    private PasswordField confirmPasswordTF;
    @FXML
    private AnchorPane signUpPage;
    @FXML
    private TextField usernameTF;
    @FXML
    void onRegisterNewUserClick(ActionEvent event) {

        String name = nameTF.getText();
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        String confirmedPass = confirmPasswordTF.getText();

        if (name.isEmpty() || name.isBlank()
                || username.isEmpty() || username.isBlank() ||
                password.isEmpty() || password.isBlank() ||
                confirmedPass.isEmpty() || confirmedPass.isBlank()){
            showAlert("All fields are required!");
        }else {
            if (DBHelper.searchForUsername(username)){
                showAlert("This username already exists!");
            }else {
                if (password.equals(confirmedPass)){
                    DBHelper.insert(name,username,password);
                    signUpPage.getScene().getWindow().hide();
                    showAlert("User: "+username+" successfully registered!" );
                }else {
                    showAlert("Password does not match!");
                }
            }
        }
    }

    @FXML
    void onSignInClick(MouseEvent event) throws IOException {

        signUpPage.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(SignInController.class.getResource("/frontend/login-layout.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sign In");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.show();

    }
    @FXML
    void showAlert(String m){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Note!");
        alert.setContentText(m);
        alert.showAndWait();
    }
}
