package org.example.week10.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.week10.Catatan;

import java.time.LocalDate;

public class AddTaskController {

    @FXML private TextField txtFldJudul;
    @FXML private TextArea txtAreaKonten;
    @FXML private DatePicker datePickerDeadline;

    private CatatanController catatanController;

    public void setCatatanController(CatatanController controller) {
        this.catatanController = controller;
    }

    @FXML
    private void onBtnSimpanClick(ActionEvent event) {
        String judul = txtFldJudul.getText().trim();
        String konten = txtAreaKonten.getText().trim();
        LocalDate deadlineDate = datePickerDeadline.getValue();
        String deadline = (deadlineDate != null) ? deadlineDate.toString() : "";

        if (judul.isEmpty() || konten.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Judul dan isi tidak boleh kosong.");
            return;
        }

        if (deadlineDate == null) {
            showAlert(Alert.AlertType.WARNING, "Mohon pilih tanggal deadline.");
            return;
        }

        Catatan catatanBaru = new Catatan(0, judul, konten, deadline);
        if (catatanController != null) {
            catatanController.addCatatanBaru(catatanBaru);
            showAlert(Alert.AlertType.INFORMATION, "Catatan berhasil ditambahkan.");
        }

        ((Stage) txtFldJudul.getScene().getWindow()).close();
    }

    @FXML
    private void onBtnHapus(ActionEvent event) {
        txtFldJudul.clear();
        txtAreaKonten.clear();
        datePickerDeadline.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
