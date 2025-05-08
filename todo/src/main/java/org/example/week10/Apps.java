package org.example.week10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class Apps extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // pastikan login-view.fxml berada di folder resources/org/example/week10/
            FXMLLoader loader = new FXMLLoader(Apps.class.getResource("login-view.fxml"));
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