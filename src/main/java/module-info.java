module com.example.notlokicoin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.notlokicoin to javafx.fxml;
    exports com.example.notlokicoin;
}