package org.example.week10;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CatatanController {

    @FXML
    private TextField txtFldJudul;

    @FXML
    private TextArea txtAreaKonten;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnHapus;

    @FXML
    private TableView<Catatan> table;

    @FXML
    private TableColumn<Catatan, Integer> id;

    @FXML
    private TableColumn<Catatan, String> judul;

    private ObservableList<Catatan> catatanList;
    private int nextId = 1;

    private Catatan selectedCatatan = null;


    @FXML
    public void initialize() {
        catatanList = FXCollections.observableArrayList();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        judul.setCellValueFactory(new PropertyValueFactory<>("judul"));

        table.setItems(catatanList);

        table.setOnMouseClicked(event -> {
            Catatan selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedCatatan = selected;
                txtFldJudul.setText(selected.getJudul());
                txtAreaKonten.setText(selected.getKonten());
            }
        });
    }


    @FXML
    private void onBtnSimpanClick() {
        String judulText = txtFldJudul.getText().trim();
        String isiText = txtAreaKonten.getText().trim();

        if (!judulText.isEmpty() && !isiText.isEmpty()) {
            if (selectedCatatan == null) {
                // Tambah data baru
                Catatan catatan = new Catatan(nextId++, judulText, isiText);
                catatanList.add(catatan);
                showAlert(Alert.AlertType.INFORMATION, "Catatan Ditambahkan!");
            } else {
                // Update data
                selectedCatatan.setJudul(judulText);
                selectedCatatan.setKonten(isiText);
                table.refresh();  // refresh tampilan tabel
                showAlert(Alert.AlertType.INFORMATION, "Catatan Dirubah!");
            }
            clearForm();
        } else {
            showAlert(Alert.AlertType.WARNING, "Judul dan Isi tidak boleh kosong.");
        }
    }

    @FXML
    private void onBtnHapus() {
        Catatan selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            catatanList.remove(selected);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih catatan yang ingin dihapus.");
            alert.show();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }

    private void clearForm() {
        txtFldJudul.clear();
        txtAreaKonten.clear();
        txtFldJudul.requestFocus();
        selectedCatatan = null;
    }
}