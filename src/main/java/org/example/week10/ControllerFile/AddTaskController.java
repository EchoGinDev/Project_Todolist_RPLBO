package org.example.week10.ControllerFile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.week10.Catatan;
import org.example.week10.manager.DBManager;

import java.time.LocalDate;

public class AddTaskController {

    @FXML private TextField txtFldJudul;
    @FXML private TextArea txtKonten;
    @FXML private DatePicker dpDeadline;
    @FXML private ComboBox<String> combooxKategori; // ✅ Ganti dari TextField ke ComboBox

    private org.example.week10.ControllerFile.CatatanController catatanController;

    public void setCatatanController(org.example.week10.ControllerFile.CatatanController controller) {
        this.catatanController = controller;
    }

    @FXML
    private void onBtnSimpanClick(ActionEvent event) {
        String judul = txtFldJudul.getText().trim();
        String konten = txtKonten.getText().trim();
        String kategori = combooxKategori.getValue(); // ✅ Ambil dari ComboBox
        LocalDate deadlineDate = dpDeadline.getValue();
        String deadline = (deadlineDate != null) ? deadlineDate.toString() : "";

        if (judul.isEmpty() || konten.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Judul dan isi tidak boleh kosong.");
            return;
        }

        if (deadlineDate == null) {
            showAlert(Alert.AlertType.WARNING, "Mohon pilih tanggal deadline.");
            return;
        }

        if (kategori == null || kategori.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Mohon pilih kategori.");
            return;
        }

        Catatan catatanBaru = new Catatan(0, judul, konten, deadline, kategori);

        // ✅ Simpan ke database
        DBManager.getInstance().tambahCatatan(catatanBaru);

        // ✅ Tambahkan ke list tampilan jika diperlukan
        if (catatanController != null) {
            catatanController.addCatatanBaru(catatanBaru);
        }

        showAlert(Alert.AlertType.INFORMATION, "Catatan berhasil ditambahkan.");
        ((Stage) txtFldJudul.getScene().getWindow()).close();
    }

    @FXML
    private void onBtnHapus(ActionEvent event) {
        txtFldJudul.clear();
        txtKonten.clear();
        combooxKategori.setValue(null); // ✅ Reset ComboBox
        dpDeadline.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
