package org.example.week10;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class EditTaskController {
    @FXML private TextField txtFldJudul;
    @FXML private TextArea txtAreaKonten;
    @FXML private DatePicker datePickerDeadline;

    private CatatanController catatanController;
    private Catatan selectedCatatan;

    public void setData(CatatanController catatanController, Catatan selectedCatatan) {
        this.catatanController = catatanController;
        this.selectedCatatan = selectedCatatan;
        // Isi field dengan data yang dipilih
        txtFldJudul.setText(selectedCatatan.getJudul());
        txtAreaKonten.setText(selectedCatatan.getKonten());
        if (selectedCatatan.getDeadline() != null && !selectedCatatan.getDeadline().isEmpty()) {
            datePickerDeadline.setValue(java.time.LocalDate.parse(selectedCatatan.getDeadline().substring(0,10)));
        }
    }

    @FXML
    private void onBtnUpdateClick() {
        try {
            selectedCatatan.setJudul(txtFldJudul.getText());
            selectedCatatan.setKonten(txtAreaKonten.getText());
            selectedCatatan.setDeadline(datePickerDeadline.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00");

            DBManager.getInstance().updateCatatan(selectedCatatan);
            catatanController.refreshData();

            Stage stage = (Stage) txtFldJudul.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal mengupdate catatan.");
        }
    }

    @FXML
    private void onBtnBatal() {
        Stage stage = (Stage) txtFldJudul.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
