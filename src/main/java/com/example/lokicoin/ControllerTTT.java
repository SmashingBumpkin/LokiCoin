package com.example.lokicoin;

import com.example.lokicoin.blockchain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerTTT implements Initializable {

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    private Button button10;

    @FXML
    private Button GoHome;

    @FXML
    private Text winnerText;

    @FXML
    private TextField myTextField1;

    @FXML
    private TextField myTextField2;

    @FXML
    private Button mybutton1;

    @FXML
    private Button mybutton2;

    @FXML
    private Label mylabel1;

    @FXML
    private Label mylabel2;

    private int playerTurn = 0;

    private Simulation simcity;
    ArrayList<Button> buttons;
    private int amount2;
    private int amount1;
    private Account player1;

    private Account player2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            GoHome.setOnAction(event -> GoBack());
            setupButton(button);
            button.setFocusTraversable(false);
            Account player1 = new Account();
            Account player2 = new Account();
        });
    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
        amount1 = 0;
        amount2 = 0;
    }

    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            checkIfGameIsOver();
            
        });
    }


    public void setPlayerSymbol(Button button){
        if(playerTurn % 2 == 0){
            button.setText("X");
            playerTurn = 1;
        } else{
            button.setText("O");
            playerTurn = 0;
        }
    }
    @FXML
    public void GetAmount(ActionEvent event) {
        try {
             amount1 = Integer.parseInt(myTextField1.getText());
             amount2 = Integer.parseInt(myTextField2.getText());
        }
        catch (NumberFormatException e) {
            mylabel1.setText("Enter a valid amount of LokiCoin");
            mylabel2.setText("Enter a valid amount of LokiCoin");
        }
    }
    
    public void checkIfGameIsOver(){
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> button1.getText() + button2.getText() + button3.getText();
                case 1 -> button4.getText() + button5.getText() + button6.getText();
                case 2 -> button7.getText() + button8.getText() + button9.getText();
                case 3 -> button1.getText() + button5.getText() + button9.getText();
                case 4 -> button3.getText() + button5.getText() + button7.getText();
                case 5 -> button1.getText() + button4.getText() + button7.getText();
                case 6 -> button2.getText() + button5.getText() + button8.getText();
                case 7 -> button3.getText() + button6.getText() + button9.getText();
                default -> null;
            };
            
            if (line.equals("XXX")) {
                winnerText.setText("Player 1 wins: " + amount2 + " LokiCoin");
                Account sender =  player2;
                Account reciever = player1;
                LokiTransaction lokiTx = player2.generateLokiTransaction(player1.getPubKey(), amount1, amount2);
                Transaction tx = lokiTx.lokiToGenericTransaction();
                Network.addPotentialTransaction(tx);
            }
            
            else if (line.equals("OOO")) {
                winnerText.setText("Player 2 wins: " + amount1 + " LokiCoin");
                Account sender =  player1;
                Account reciever = player2;
                LokiTransaction lokiTx = player1.generateLokiTransaction(player2.getPubKey(), amount1, amount2);
                Transaction tx = lokiTx.lokiToGenericTransaction();
                Network.addPotentialTransaction(tx);
            }

        }
    }
    private void GoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePageNotLokiCoin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) GoHome.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    }
