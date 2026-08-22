module com.example.fire {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fire to javafx.fxml;
    exports com.example.fire;
}