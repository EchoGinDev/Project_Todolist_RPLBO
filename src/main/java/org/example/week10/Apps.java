package org.example.week10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Apps extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Load login-view.fxml dari folder resources/org/example/week10/
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("/org/example/week10/login-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
