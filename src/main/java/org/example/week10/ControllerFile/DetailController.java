package org.example.week10.ControllerFile;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.week10.Catatan;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DetailController {

    @FXML private Label labelJudul;
    @FXML private TextArea labelKonten;
    @FXML private Label labelDeadline;
    @FXML private Label labelCountdown;
    @FXML private Label labelStatus;
    @FXML private Label labelKategori;


    public void setDetail(Catatan catatan) {
        labelJudul.setText(catatan.getJudul());
        labelKonten.setText(catatan.getKonten());
        labelDeadline.setText(catatan.getDeadline());
        labelKategori.setText(catatan.getKategori());

        try {
            String deadlineStr = catatan.getDeadline().trim();
            LocalDateTime deadlineDateTime = deadlineStr.length() == 10
                    ? LocalDateTime.parse(deadlineStr + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), deadlineDateTime);

            String countdownText = seconds >= 0
                    ? String.format("%d hari %d jam %d menit",
                    seconds / (3600 * 24),
                    (seconds % (3600 * 24)) / 3600,
                    (seconds % 3600) / 60)
                    : "Terlambat";

            labelCountdown.setText(countdownText);
        } catch (Exception e) {
            labelCountdown.setText("Format salah");
        }

        labelStatus.setText(catatan.isSelesai() ? "Selesai" : "Belum Selesai");
    }

}
