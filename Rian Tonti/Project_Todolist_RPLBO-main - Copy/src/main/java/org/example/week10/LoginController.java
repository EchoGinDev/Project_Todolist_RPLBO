package org.example.week10;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Hyperlink lblForgot;

    @FXML
    public void btnLoginClick() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.equals("admin") && password.equals("admin")) {
            try {
                Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                currentStage.close();

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
            showAlert(Alert.AlertType.ERROR, "Login Gagal!", "Username atau password salah.");
        }
    }

    @FXML
    public void onKeyPressEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            btnLoginClick();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}