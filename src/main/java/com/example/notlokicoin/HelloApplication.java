package com.example.notlokicoin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
<<<<<<< HEAD
=======
import javafx.scene.control.Label;
>>>>>>> Jasch
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePageNotLokiCoin.fxml"));
        Scene scene = new Scene(root);
<<<<<<< HEAD
        scene.getStylesheets().add("style.css");
        stage.setTitle("LokiCoin");
        stage.setScene(scene);
        stage.show();
    }

=======
        //scene.getStylesheets().add("style.css");
        stage.setTitle("LokiCoin");
        stage.setScene(scene);
        stage.show();

    }



>>>>>>> Jasch
    public static void main(String[] args) {
        launch();
    }
}