package com.example.notlokicoin;


import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;



public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;


    public void GoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePageNotLokiCoin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void GoSim(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SimulationNotLokiCoin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void GoGames(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GamesNotLokiCoin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    private Label Counting;

    final int[] count = {0};
    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                count[0]++;
                int l = Network.getNumberOfPotentialBlocks();
                Counting.setText(String.valueOf(count[0]));
            })
    );

        //IntegerProperty x = new SimpleIntegerProperty(0);
    @FXML
    public void StartClicked() throws IOException, InterruptedException {
        StartClickChangeColor();
        Simulation Strt = new Simulation();
        Strt.run();
        timeline.setCycleCount(10);
        timeline.play();
    }
        //File Chode = new File("/Users/jasch/Desktop/NotLokiCoin/LokiCoin/src/main/java/com/example/notlokicoin/HelloApplication.java");
        //Desktop.getDesktop().open(Chode);


        //x.set(x.get()+1);
    
    @FXML
    public void StartClickChangeColor(){
        startButton.setDisable(true);
        startButton.setVisible(false);
        stopButton.setDisable(false);
        stopButton.setVisible(true);
    }

    @FXML
    public void StopClicked() {
        stopButton.setDisable(true);
        stopButton.setVisible(false);
        startButton.setDisable(false);
        startButton.setVisible(true);
        timeline.stop();
    }



    @FXML
    public void PlayXand0(ActionEvent event) throws IOException {
        //File ticTacToe = new File("/Applications/Tic-Tac-Toe.app");
        //Desktop.getDesktop().open(ticTacToe);

        Parent root = FXMLLoader.load(getClass().getResource("hello-viewTTT.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();

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
        File Chode = new File("/Users/jasch/Desktop/NotLokiCoin/LokiCoin/src/main/java/com/example/notlokicoin/HelloApplication.java");
        Desktop.getDesktop().open(Chode);
    }

    @FXML
    public void ViewCode2(ActionEvent event) throws IOException {
        File Chode = new File("/Users/jasch/Desktop/NotLokiCoin/LokiCoin/src/main/java/com/example/notlokicoin/HelloApplication.java");
        Desktop.getDesktop().open(Chode);
    }
}