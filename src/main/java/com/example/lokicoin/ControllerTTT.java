package com.example.lokicoin;

import com.example.lokicoin.blockchain.Block;
import com.example.lokicoin.blockchain.Simulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);

        });
    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
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

    }
    public void setPlayerSymbol(Button button){
        if(playerTurn % 2 == 0){
            button.setText("X");
            Account player1 = new account(),
            playerTurn = 1;
        } else{
            button.setText("O");
            Account player2 = new account();
            playerTurn = 0;
        }
    }
    public void getamount(ActionEvent event) {
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
                winnerText.setText("Player 1 wins:" + amount2 + "LokiCoin");
                Account player1 = account reciver();
                Account player2 = account sender();
            }
            
            else if (line.equals("OOO")) {
                winnerText.setText("Player 1 wins:" + amount1 + "LokiCoin");
                Account player1 = account sender();
                Account player2 = account reciever();
            }
            
        }
    }
    public void GoHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePageNotLokiCoin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();

    }
