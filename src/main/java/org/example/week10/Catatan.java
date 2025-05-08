package org.example.week10;

import java.time.*;

public class Catatan {
    private int id;
    private String judul;
    private String konten;
    private LocalDate deadline;

    public Catatan(int id, String judul, String konten, LocalDate deadline) {
        this.id = id;
        this.judul = judul;
        this.konten = konten;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getKonten() {
        return konten;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getCountdown() {
        if (deadline == null) return "-";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = deadline.atTime(23, 59, 59);
        Duration duration = Duration.between(now, target);

        if (duration.isNegative()) {
            duration = duration.abs();
            return "Terlambat " + formatDuration(duration);
        } else if (duration.isZero()) {
            return "Hari ini";
        } else {
            return formatDuration(duration);
        }
    }

    private String formatDuration(Duration duration) {
        long totalSeconds = duration.getSeconds();
        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d hari, %d jam, %d menit, %d detik", days, hours, minutes, seconds);
    }
}
