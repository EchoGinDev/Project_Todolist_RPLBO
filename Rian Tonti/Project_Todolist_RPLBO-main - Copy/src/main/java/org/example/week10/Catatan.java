package org.example.week10;

import javafx.beans.property.*;

public class Catatan {
    private final IntegerProperty id;
    private final StringProperty judul;
    private final StringProperty konten;
    private final StringProperty deadline;

    public Catatan(int id, String judul, String konten, String deadline) {
        this.id = new SimpleIntegerProperty(id);
        this.judul = new SimpleStringProperty(judul);
        this.konten = new SimpleStringProperty(konten);
        this.deadline = new SimpleStringProperty(deadline);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getJudul() {
        return judul.get();
    }

    public void setJudul(String judul) {
        this.judul.set(judul);
    }

    public StringProperty judulProperty() {
        return judul;
    }

    public String getKonten() {
        return konten.get();
    }

    public void setKonten(String konten) {
        this.konten.set(konten);
    }

    public StringProperty kontenProperty() {
        return konten;
    }

    public String getDeadline() {
        return deadline.get();
    }

    public void setDeadline(String deadline) {
        this.deadline.set(deadline);
    }

    public StringProperty deadlineProperty() {
        return deadline;
    }

    public String getIsi() {
        return konten.get();
    }
}
