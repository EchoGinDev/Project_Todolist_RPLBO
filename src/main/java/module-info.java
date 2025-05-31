module org.example.week10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens org.example.week10 to javafx.fxml;
    exports org.example.week10;
    exports org.example.week10.Controller;
    opens org.example.week10.Controller to javafx.fxml;
    exports org.example.week10.Manager;
    opens org.example.week10.Manager to javafx.fxml;
}

