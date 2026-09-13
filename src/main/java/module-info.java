module com.example.rep2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rep2 to javafx.fxml;
    exports com.example.rep2;
}