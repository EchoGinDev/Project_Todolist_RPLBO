package org.example.week10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.week10.manager.SessionManager;

import java.io.IOException;

/**
 * Kelas utama untuk menjalankan aplikasi JavaFX To-Do List.
 * Menentukan apakah pengguna sudah login atau belum, lalu menampilkan tampilan yang sesuai.
 */
public class Apps extends Application {

    /**
     * Method yang dipanggil pertama kali saat aplikasi dijalankan.
     * Menentukan tampilan awal berdasarkan session login pengguna.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Ambil informasi session user (apakah user sudah login)
        String sessionUser = SessionManager.getSessionUser();
        FXMLLoader fxmlLoader;

        // Cek apakah ada session aktif
        if (sessionUser != null) {
            // Jika pengguna sudah login, langsung ke tampilan daftar catatan
            fxmlLoader = new FXMLLoader(Apps.class.getResource("daftar-catatan-view.fxml"));
        } else {
            // Jika belum login, tampilkan tampilan login
            fxmlLoader = new FXMLLoader(Apps.class.getResource("login-view.fxml"));
        }

        // Muat FXML file yang sesuai
        Parent root = fxmlLoader.load();

        // Buat scene dari root node
        Scene scene = new Scene(root);

        // Set judul jendela aplikasi
        stage.setTitle("To-Do List");

        // Set scene ke stage (jendela utama)
        stage.setScene(scene);

        // Ukuran jendela otomatis mengikuti ukuran komponen di FXML
        stage.sizeToScene();

        // Tampilkan jendela aplikasi
        stage.show();
    }

    /**
     * Method utama untuk meluncurkan aplikasi JavaFX.
     */
    public static void main(String[] args) {
        launch(); // Memulai lifecycle aplikasi JavaFX
    }
}
