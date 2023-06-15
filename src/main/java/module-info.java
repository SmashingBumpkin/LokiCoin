module com.example.notlokicoin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.lokicoin to javafx.fxml;
    exports com.example.lokicoin;
    exports com.example.lokicoin.blockchain;
    opens com.example.lokicoin.blockchain to javafx.fxml;
}