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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label TestJ;
    @FXML
    private Label miner1dets;

    @FXML
    private Label miner2dets;
    @FXML
    private Label miner3dets;
    @FXML
    private Label miner4dets;
    @FXML
    private Label miner5dets;
    @FXML
    private Label miner6dets;
    @FXML
    private Label blockDets;
    @FXML
    private Label blockDetsHash;
    @FXML
    private Label blockDetsPrevHash;
    @FXML
    private Text transDets;
    @FXML
    private Text finaldis;
    @FXML
    private ScrollPane theScrollThingy;
    @FXML
    private ScrollPane ScrollBalances;
    @FXML
    private Label TransactionsLabel;
    @FXML
    private Label BalancesLabel;

    @FXML
    private Circle miner1green;
    @FXML
    private Circle miner2green;
    @FXML
    private Circle miner3green;
    @FXML
    private Circle miner4green;
    @FXML
    private Circle miner5green;
    @FXML
    private Circle miner6green;

    @FXML
    private Circle miner1red;
    @FXML
    private Circle miner2red;
    @FXML
    private Circle miner3red;
    @FXML
    private Circle miner4red;
    @FXML
    private Circle miner5red;
    @FXML
    private Circle miner6red;


    private Simulation simcity;
    int jayz = 0;



    public void GoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePageNotLokiCoin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void GoSim(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SimulationNotLokiCoinJasch.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();

    }

    public void GoGames(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("GamesNotLokiCoin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void StartClicked() throws IOException, InterruptedException {
        simcity = new Simulation();

        //miner1dets.setText("gg");
        // change color thread
        Thread Changeclr = new Thread (() -> {
            StartClickChangeColor();
        });
        Changeclr.start();

        // start sim thread
        Thread startsim = new Thread (() -> {
            simcity.run();
        });
        startsim.start();

        // change stuff on screen thread
        Thread updateLabelThread = new Thread(() -> {
            while (Miner.minersActive == true) {

                Block notChain = simcity.miner1.localBlockchain.getLastBlock();
                String blockDetsNumber = "Block Number: " + notChain.getBlockNumber();
                Platform.runLater(() -> blockDets.setText(blockDetsNumber));
                String bDH = "Hash: " + notChain.getHash();
                Platform.runLater(() -> blockDetsHash.setText(bDH));
                String bDPH = "PrevHash: " + notChain.getPreviousHash();
                Platform.runLater(() -> blockDetsPrevHash.setText(bDPH));
                Platform.runLater(()->finaldis.setText(simcity.miner1.getAccounts()));
                //Block notChain1 = simcity.miner1.localBlockchain.getLastBlock();
                //String blockDetails1 = notChain1.returnBlockPrintable();
                if (notChain.getBlockNumber()%3 != 0){
                    miner1green.setVisible(true);
                    miner1red.setVisible(false);
                }
                else{
                    miner1green.setVisible(false);
                    miner1red.setVisible(true);
                }
                //String blockDetails1 = "Block Validation: " + simcity.miner1.flag + System.lineSeparator();// + "fork i think:" + simcity.miner1.validFork;
                //Platform.runLater(() -> miner1dets.setText(blockDetails1));

                // Miner 2 doing the same shit
                //Block notChain2 = simcity.miner2.localBlockchain.getLastBlock();
                if (notChain.getBlockNumber()%6 != 0){
                    miner2green.setVisible(true);
                    miner2red.setVisible(false);
                }
                else{
                    miner2green.setVisible(false);
                    miner2red.setVisible(true);
                }
                //String blockDetails2 = "Block Validation: " + simcity.miner2.flag + System.lineSeparator();
                //Platform.runLater(() -> miner2dets.setText(blockDetails2));

                // miner3
                //Block notChain3 = simcity.miner3.localBlockchain.getLastBlock();
                if (simcity.miner3.flag){
                    miner3green.setVisible(true);
                    miner3red.setVisible(false);
                }
                else{
                    miner3green.setVisible(false);
                    miner3red.setVisible(true);
                }
                //String blockDetails3 = "Block Validation: " + simcity.miner3.flag + System.lineSeparator();
                //Platform.runLater(() -> miner3dets.setText(blockDetails3));

                //miner4
                //Block notChain4 = simcity.miner4.localBlockchain.getLastBlock();
                if (notChain.getBlockNumber()%3 != 0){
                    miner4green.setVisible(true);
                    miner4red.setVisible(false);
                }
                else{
                    miner4green.setVisible(false);
                    miner4red.setVisible(true);
                }
                //String blockDetails4 = "Block Validation: " + simcity.miner4.flag + System.lineSeparator();
                //Platform.runLater(() -> miner4dets.setText(blockDetails4));

                // Miner 5 doing the same shit
                //Block notChain5 = simcity.miner5.localBlockchain.getLastBlock();
                if (simcity.miner5.flag){
                    miner5green.setVisible(true);
                    miner5red.setVisible(false);
                }
                else{
                    miner5green.setVisible(false);
                    miner5red.setVisible(true);
                }
                //String blockDetails5 = "Block Validation: " + simcity.miner5.flag + System.lineSeparator();
                //Platform.runLater(() -> miner5dets.setText(blockDetails5));

                //last one i promise
                //Block notChain6 = simcity.miner6.localBlockchain.getLastBlock();
                if (notChain.getBlockNumber()%2 != 0){
                    miner6green.setVisible(true);
                    miner6red.setVisible(false);
                }
                else{
                    miner6green.setVisible(false);
                    miner6red.setVisible(true);
                }
                //String blockDetails6 = "Block Validation: " + simcity.miner6.flag + System.lineSeparator();
                //Platform.runLater(() -> miner6dets.setText(blockDetails6));

                for (Account uwu : simcity.miner3.localBlockchain.getLastBlock().getAccounts().values()){
                    int xValue = uwu.getBalance();
                    Platform.runLater(() -> TestJ.setText(String.valueOf(xValue)));
                };


                try {
                    Thread.sleep(1); // Update every second (adjust as needed)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateLabelThread.start();


    }

    @FXML
    public void StartClickChangeColor(){
        startButton.setDisable(true);
        startButton.setVisible(false);
        stopButton.setDisable(false);
        stopButton.setVisible(true);

    }

    @FXML
    public void StopClicked() {
        Miner.minersActive = false;
        Thread changethings = new Thread(()->{
            stopButton.setDisable(true);
            stopButton.setVisible(false);
            theScrollThingy.setVisible(true);
            TransactionsLabel.setVisible(true);
            //BalancesLabel.setVisible(true);
            //ScrollBalances.setVisible(true);
        });
        changethings.start();

        //Trans
        String PrintStringy = "";
        List<Transaction> jjj = Network.getPotentialTransactions(0, Network.getNumberOfPotentialTransactions());
        for (int i = 0; i < Network.getNumberOfPotentialTransactions(); i++) {
            LokiTransaction xxtx = new LokiTransaction(jjj.get(i));
            PrintStringy = PrintStringy + "Sender: " + (xxtx.getSender().hashCode()) + " Fee: " + xxtx.getFee()
                    + "   Nonce: " + xxtx.getNonce() + " Recipient: " + (xxtx.getRecipient().hashCode())
                    + " Amount: " + xxtx.getAmount() + "\n";
        }
        transDets.setText(PrintStringy);
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
        File Chode = new File("/Users/jasch/Desktop/NotLokiCoin/MyCoin/src/main/java/com/example/notlokicoin/Simulation.java");
        Desktop.getDesktop().open(Chode);
    }
}