package org.example.week10.ControllerFile;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.week10.Catatan;
import org.example.week10.manager.DBManager;

import java.time.format.DateTimeFormatter;

/**
 * Controller untuk form pengeditan catatan.
 * Mengisi data dari catatan terpilih, dan menyimpan perubahan ke database saat diklik Update.
 */
public class EditTaskController {

    // Komponen UI dari file FXML
    @FXML private TextField txtFldJudul;
    @FXML private TextArea txtAreaKonten;
    @FXML private DatePicker datePickerDeadline;
    @FXML private ComboBox<String> comboBoxKategori;

    // Referensi ke controller utama dan catatan yang sedang diedit
    private org.example.week10.ControllerFile.CatatanController catatanController;
    private Catatan selectedCatatan;

    /**
     * Inisialisasi awal saat FXML dimuat.
     * Menambahkan opsi kategori ke ComboBox.
     */
    public void initialize() {
        comboBoxKategori.getItems().addAll("Pekerjaan", "Pribadi", "Akademik");
    }

    /**
     * Menerima data dari controller utama dan catatan yang akan diedit.
     * Mengisi form dengan data catatan yang sudah ada.
     */
    public void setData(org.example.week10.ControllerFile.CatatanController catatanController, Catatan selectedCatatan) {
        this.catatanController = catatanController;
        this.selectedCatatan = selectedCatatan;

        // Isi field dengan data dari catatan terpilih
        txtFldJudul.setText(selectedCatatan.getJudul());
        txtAreaKonten.setText(selectedCatatan.getKonten());

        // Set tanggal ke DatePicker jika ada deadline
        if (selectedCatatan.getDeadline() != null && !selectedCatatan.getDeadline().isEmpty()) {
            // Ambil 10 karakter pertama (format "yyyy-MM-dd")
            datePickerDeadline.setValue(java.time.LocalDate.parse(selectedCatatan.getDeadline().substring(0, 10)));
        }

        // Set kategori ke ComboBox
        comboBoxKategori.setValue(selectedCatatan.getKategori());
    }

    /**
     * Dipanggil saat tombol Update diklik.
     * Menyimpan perubahan ke objek dan database, lalu menutup jendela.
     */
    @FXML
    private void onBtnUpdateClick() {
        try {
            // Ambil data dari input pengguna
            selectedCatatan.setJudul(txtFldJudul.getText());
            selectedCatatan.setKonten(txtAreaKonten.getText());
            selectedCatatan.setDeadline(datePickerDeadline.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            selectedCatatan.setKategori(comboBoxKategori.getValue());

            // Update ke database
            DBManager.getInstance().updateCatatan(selectedCatatan);

            // Refresh data di tampilan utama
            catatanController.refreshData();

            // Tutup jendela saat selesai
            Stage stage = (Stage) txtFldJudul.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal mengupdate catatan.");
        }
    }

    /**
     * Dipanggil saat tombol Batal diklik.
     * Menutup jendela tanpa menyimpan perubahan.
     */
    @FXML
    private void onBtnBatal() {
        Stage stage = (Stage) txtFldJudul.getScene().getWindow();
        stage.close();
    }

    /**
     * Menampilkan alert pop-up.
     * @param type Jenis alert (INFORMATION, ERROR, dll)
     * @param message Pesan yang akan ditampilkan
     */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
