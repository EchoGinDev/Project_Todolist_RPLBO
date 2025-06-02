package org.example.week10.ControllerFile;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

// ✅ Import SessionManager untuk menyimpan sesi login pengguna
import org.example.week10.manager.SessionManager;

/**
 * Controller untuk halaman login aplikasi.
 * Menangani validasi login dan perpindahan ke halaman utama jika berhasil.
 */
public class LoginController {

    // Komponen UI dari file FXML
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Hyperlink lblForgot;

    /**
     * Metode ini dipanggil saat tombol login diklik.
     * Jika username dan password benar, maka akan dialihkan ke halaman utama.
     */
    @FXML
    public void btnLoginClick() {
        // Ambil input pengguna
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        // Cek kredensial secara hardcoded (sementara)
        if (username.equals("admin") && password.equals("admin")) {
            try {
                // Simpan sesi login
                SessionManager.saveSession(username);

                // Tutup jendela login
                Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                currentStage.close();

                // Buka halaman utama (daftar-catatan-view.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/week10/daftar-catatan-view.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Daftar Catatan");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error!", "Gagal membuka halaman daftar catatan.");
            }
        } else {
            // Tampilkan error jika login gagal
            showAlert(Alert.AlertType.ERROR, "Login Gagal!", "Username atau password salah.");
        }
    }

    /**
     * Memungkinkan login dengan menekan tombol Enter pada keyboard.
     * Menyederhanakan interaksi pengguna.
     */
    @FXML
    public void onKeyPressEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            btnLoginClick();  // Login ketika user menekan Enter
        }
    }

    /**
     * Menampilkan alert pop-up ke pengguna.
     * @param type Tipe alert (ERROR, INFORMATION, dll)
     * @param title Judul dari alert
     * @param content Isi pesan alert
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
