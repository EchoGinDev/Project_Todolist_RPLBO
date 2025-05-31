module org.example.week10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.week10 to javafx.fxml;
    exports org.example.week10;
    exports org.example.week10.ControllerFile;
    opens org.example.week10.ControllerFile to javafx.fxml;
    exports org.example.week10.manager;
    opens org.example.week10.manager to javafx.fxml;
}

