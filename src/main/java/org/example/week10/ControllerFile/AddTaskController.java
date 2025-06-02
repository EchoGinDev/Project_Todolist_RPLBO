package org.example.week10.ControllerFile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.week10.Catatan;
import org.example.week10.manager.DBManager;
import java.time.LocalDate;

public class AddTaskController {

    // Komponen GUI dari file FXML yang dihubungkan lewat anotasi @FXML
    @FXML private TextField txtFldJudul;                 // Input untuk judul catatan
    @FXML private TextArea txtKonten;                    // Input untuk isi/konten catatan
    @FXML private DatePicker dpDeadline;                 // Input untuk memilih tanggal deadline
    @FXML private ComboBox<String> combooxKategori;      // Dropdown untuk memilih kategori catatan

    // Referensi ke controller utama (CatatanController) untuk sinkronisasi data
    private org.example.week10.ControllerFile.CatatanController catatanController;

    /**
     * Setter untuk menyimpan referensi ke controller utama,
     * agar catatan baru bisa langsung ditambahkan ke daftar utama.
     */
    public void setCatatanController(org.example.week10.ControllerFile.CatatanController controller) {
        this.catatanController = controller;
    }

    /**
     * Event handler untuk tombol "Simpan".
     * Validasi input, buat objek Catatan, simpan ke database, dan update tampilan utama.
     */
    @FXML
    private void onBtnSimpanClick(ActionEvent event) {
        // Ambil data dari inputan
        String judul = txtFldJudul.getText().trim();
        String konten = txtKonten.getText().trim();
        String kategori = combooxKategori.getValue();
        LocalDate deadlineDate = dpDeadline.getValue();
        String deadline = (deadlineDate != null) ? deadlineDate.toString() : "";

        // Validasi input kosong
        if (judul.isEmpty() || konten.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Judul dan isi tidak boleh kosong.");
            return;
        }

        // Validasi tanggal deadline
        if (deadlineDate == null) {
            showAlert(Alert.AlertType.WARNING, "Mohon pilih tanggal deadline.");
            return;
        }

        // Validasi kategori
        if (kategori == null || kategori.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Mohon pilih kategori.");
            return;
        }

        // Buat objek Catatan baru
        Catatan catatanBaru = new Catatan(0, judul, konten, deadline, kategori);

        // Simpan ke database menggunakan DBManager (singleton)
        DBManager.getInstance().tambahCatatan(catatanBaru);

        // Update tampilan utama jika controller tersedia
        if (catatanController != null) {
            catatanController.addCatatanBaru(catatanBaru);
        }

        // Tampilkan notifikasi berhasil
        showAlert(Alert.AlertType.INFORMATION, "Catatan berhasil ditambahkan.");

        // Tutup jendela saat ini (form tambah catatan)
        ((Stage) txtFldJudul.getScene().getWindow()).close();
    }

    /**
     * Event handler untuk tombol "Hapus".
     * Membersihkan seluruh field input agar user bisa mengisi ulang dari awal.
     */
    @FXML
    private void onBtnHapus(ActionEvent event) {
        txtFldJudul.clear();
        txtKonten.clear();
        combooxKategori.setValue(null); // Reset pilihan kategori
        dpDeadline.setValue(null);      // Reset tanggal
    }

    /**
     * Method utilitas untuk menampilkan popup alert kepada pengguna.
     * @param type Jenis alert (INFORMATION, WARNING, dll)
     * @param message Pesan yang akan ditampilkan di dalam alert
     */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null); // Header dikosongkan
        alert.setContentText(message); // Isi pesan
        alert.showAndWait(); // Tampilkan alert dan tunggu aksi user
    }
}
