package org.example.week10;

public class Catatan {
    private int id;
    private String judul;
    private String konten;
    private String deadline; // Format string agar mudah ditampilkan


    public Catatan(String judul, String konten) {
        this.judul = judul;
        this.konten = konten;
    }

    public Catatan(int id, String judul, String konten, String deadline) {
        this.id = id;
        this.judul = judul;
        this.konten = konten;
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }
}
