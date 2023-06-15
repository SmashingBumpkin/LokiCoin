package com.example.notlokicoin;

import javafx.animation.PauseTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class CountingThread extends Thread {
    public IntegerProperty x = new SimpleIntegerProperty();

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            x.set(i);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // code to be executed after the pause
                    System.out.println(x.get());
                }
            });
            pause.play();
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> Jasch
