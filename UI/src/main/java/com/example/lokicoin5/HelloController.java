package com.example.lokicoin5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.awt.Desktop;

import com.example.lokicoin5.hi;

import java.io.File;
import java.io.IOException;

public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label welcomeText;

    public void GoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void GoSim(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Sim.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void GoGames(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Games.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void StartClicked() {
        welcomeText.setText("Insert Something Funny");
        hi h = new hi();
        h.run();
    }


    @FXML
    public void PlayXand0(ActionEvent event) throws IOException {
        File ticTacToe = new File("/Applications/Tic-Tac-Toe.app");
        Desktop.getDesktop().open(ticTacToe);
    }

    @FXML
    public void RickRoll(ActionEvent event) throws IOException {
        File rick = new File("/Users/jasch/Desktop/LokiCoin/images/Ricky.app");
        Desktop.getDesktop().open(rick);
    }

    @FXML
    public void PlayShatranj(ActionEvent event) throws IOException {
        File Shatranj = new File("/System/Applications/Chess.app");
        Desktop.getDesktop().open(Shatranj);
    }

    @FXML
    public void ViewCode(ActionEvent event) throws IOException {
        File Chode = new File("/Users/jasch/Desktop/LokiCoin/PleaseWork/LokiCoin5/src/main/java/com/example/lokicoin5/HelloApplication.java");
        Desktop.getDesktop().open(Chode);
    }

    @FXML
    public void ViewCode2(ActionEvent event) throws IOException {
        File Chode = new File("/Users/jasch/Desktop/LokiCoin/PleaseWork/LokiCoin5/src/main/java/com/example/lokicoin5/HelloController.java");
        Desktop.getDesktop().open(Chode);
    }
}