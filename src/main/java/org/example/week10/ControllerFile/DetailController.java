package org.example.week10.ControllerFile;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.week10.Catatan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Controller untuk menampilkan detail sebuah catatan.
 * Ditampilkan saat pengguna membuka detail dari catatan di TableView.
 */
public class DetailController {

    // Komponen UI dari FXML
    @FXML private Label labelJudul;
    @FXML private TextArea labelKonten;
    @FXML private Label labelDeadline;
    @FXML private Label labelCountdown;
    @FXML private Label labelStatus;
    @FXML private Label labelKategori;

    /**
     * Mengatur detail tampilan berdasarkan objek Catatan yang dipilih.
     * Digunakan untuk mengisi teks pada semua elemen UI.
     *
     * @param catatan Objek catatan yang akan ditampilkan
     */
    public void setDetail(Catatan catatan) {
        // Set judul, konten, deadline, dan kategori
        labelJudul.setText(catatan.getJudul());
        labelKonten.setText(catatan.getKonten());
        labelDeadline.setText(catatan.getDeadline());
        labelKategori.setText(catatan.getKategori());

        // Hitung countdown berdasarkan deadline
        try {
            String deadlineStr = catatan.getDeadline().trim();

            // Jika hanya tanggal, tambahkan jam default
            LocalDateTime deadlineDateTime = deadlineStr.length() == 10
                    ? LocalDateTime.parse(deadlineStr + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Hitung selisih waktu dari sekarang ke deadline
            long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), deadlineDateTime);

            // Format ke dalam hari-jam-menit, atau "Terlambat" jika sudah lewat
            String countdownText = seconds >= 0
                    ? String.format("%d hari %d jam %d menit",
                    seconds / (3600 * 24),
                    (seconds % (3600 * 24)) / 3600,
                    (seconds % 3600) / 60)
                    : "Terlambat";

            labelCountdown.setText(countdownText);

        } catch (Exception e) {
            // Jika format deadline salah
            labelCountdown.setText("Format salah");
        }

        // Set label status berdasarkan boolean selesai
        labelStatus.setText(catatan.isSelesai() ? "Selesai" : "Belum Selesai");
    }
}
