module com.example.lokicoin5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.lokicoin5 to javafx.fxml;
    exports com.example.lokicoin5;
}