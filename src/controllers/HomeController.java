package controllers;

import database.DBHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import server_client.Client;
import server_client.Server;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {


    Client client;
    private ArrayList<String> allUsers = new ArrayList<>();

    @FXML
    private AnchorPane homePage;

    @FXML
    private Label loggedUserName;

    @FXML
    private VBox layoutOfAllUsers;
    @FXML
    private VBox layoutOfUserContacts;
    @FXML
    private VBox layoutOfAllMessageRequests;

    @FXML
    private TextArea messageTF;

    @FXML
    void onSendBroadcastMessage(ActionEvent event) {

    }

    private void addNewUser(String name, VBox parentLayout,
                                          Image userImage, Image iconImage) {
        // create the image views
        ImageView leftImage = new ImageView(userImage);
        leftImage.setFitWidth(50);
        leftImage.setFitHeight(50);
        ImageView rightImage = new ImageView(iconImage);
        rightImage.setFitHeight(40);
        rightImage.setFitWidth(40);

        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font(24));
        nameLabel.setPrefWidth(500);


        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-background-color: #B8E7E1; -fx-background-insets: 3; -fx-background-radius: 10;");
        hbox.setPadding(new Insets(8));
        hbox.setPrefWidth(600);
        hbox.setPrefHeight(77);
        hbox.getChildren().addAll(leftImage, nameLabel, rightImage);
        hbox.setSpacing(20);

        parentLayout.setPadding(new Insets(8, 8, 8, 8));
        parentLayout.getChildren().add(hbox);


        rightImage.setCursor(Cursor.HAND);
        rightImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                client.message = SignInController.loggedInUserName+"#Add#"+nameLabel.getText();
                System.out.println(client.message);
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        client = new Client();
        client.run();


        loggedUserName.setText(SignInController.loggedInName);

        allUsers = DBHelper.getAllUsers(SignInController.loggedInName);

        if (!allUsers.isEmpty() && !allUsers.contains("No users yet")) {
            for (String user : allUsers) {
                addNewUser(user,layoutOfAllUsers,
                        new Image("/imgs/userPic.png"),new Image("/imgs/addUser.png"));
            }
        } else {
            System.out.println("HHHHHHHH");
        }


//        ArrayList<String>names = new ArrayList<>();
//        names.add("7egz");
//        names.add("m");
//        Server.usersRequests.put("ferr",names);

//        ArrayList<String> currentUserRequests = Server.usersRequests.get(SignInController.loggedInUserName);
//
//        System.out.println(currentUserRequests);
//
//        if (currentUserRequests != null){
//            for (String request: currentUserRequests) {
//                System.out.println(request);
//                addNewUser(request,layoutOfAllMessageRequests,
//                        new Image("/imgs/userPic.png"),new Image("/imgs/accept.png"));
//            }
//        }
//        System.out.println(currentUserRequests);
    }
}
