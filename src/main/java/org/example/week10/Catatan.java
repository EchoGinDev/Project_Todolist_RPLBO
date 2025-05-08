package org.example.week10;

import java.time.LocalDate;

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
}