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
import server_client.Server;

import javax.swing.*;
import java.io.IOException;

public class SignInController {
    public static String loggedInName="";
    public static String loggedInUserName="";
    @FXML
    private PasswordField passwordTF;

    @FXML
    private AnchorPane signInPage;

    @FXML
    private TextField usernameTF;

    @FXML
    void onLoginClick(ActionEvent event) throws IOException {


        String username = usernameTF.getText();
        String password = passwordTF.getText();
        if (DBHelper.search(username,password)){
            loggedInName = DBHelper.getLoggedInName(username);
            loggedInUserName = username;
            System.out.println(loggedInName);
            signInPage.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("/frontend/home.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Home!");
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            stage.show();
//            showAlert("Logged in successfully!");
        } else if (username.isBlank() || username.isEmpty() || password.isBlank() || password.isEmpty()) {
            showAlert("All fields are required!");
        } else {
            showAlert("There is no such user!\n Try again or sign up");
        }
    }

    @FXML
    void onRegisterClick(MouseEvent event) throws IOException {
        signInPage.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("/frontend/signup-layout.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sign Up");
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
