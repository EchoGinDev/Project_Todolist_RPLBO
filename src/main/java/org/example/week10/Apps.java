package org.example.week10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Apps extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String sessionUser = SessionManager.getSessionUser(); // cek session
        FXMLLoader fxmlLoader;

        if (sessionUser != null) {
            // Jika session ada, langsung ke daftar catatan
            fxmlLoader = new FXMLLoader(Apps.class.getResource("daftar-catatan-view.fxml"));
        } else {
            // Jika belum login, tampilkan login
            fxmlLoader = new FXMLLoader(Apps.class.getResource("login-view.fxml"));
        }

        Parent root = fxmlLoader.load();         // Load FXML
        Scene scene = new Scene(root);           // Buat scene tanpa ukuran tetap
        stage.setTitle("To-Do List");
        stage.setScene(scene);
        stage.sizeToScene();                     // Ukuran jendela menyesuaikan FXML
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
