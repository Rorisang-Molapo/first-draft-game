module com.example.fire {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.fire to javafx.fxml;
    exports com.example.fire;
}