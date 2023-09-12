module com.example.tpo_05 {
    requires javafx.controls;
    requires javafx.fxml;
    requires activemq.all;
    requires java.naming;


    opens com.example.tpo_05 to javafx.fxml;
    exports com.example.tpo_05;
}