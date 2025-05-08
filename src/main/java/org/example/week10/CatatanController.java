package org.example.week10;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;


/**
 * Controller untuk aplikasi manajemen catatan.
 * Mengelola logika untuk menambahkan, mengedit, menghapus, dan mencari catatan.
 */
public class CatatanController {

    @FXML
    private DatePicker datePickerDeadline;

    @FXML
    private TableColumn<Catatan, String> deadline;


    @FXML
    private TextField txtFldJudul; // Input field untuk judul catatan

    @FXML
    private TextArea txtAreaKonten; // Input field untuk isi/konten catatan

    @FXML
    private Button btnSimpan; // Tombol simpan (tambah/edit)

    @FXML
    private Button btnHapus; // Tombol hapus catatan

    @FXML
    private TextField searchBox; // Field pencarian judul catatan

    @FXML
    private Button btnClearSearch; // Tombol untuk membersihkan pencarian

    @FXML
    private TableView<Catatan> table; // Tabel untuk menampilkan daftar catatan

    @FXML
    private TableColumn<Catatan, Integer> id; // Kolom ID catatan

    @FXML
    private TableColumn<Catatan, String> judul; // Kolom judul catatan

    private ObservableList<Catatan> catatanList; // Daftar catatan
    private int nextId = 1; // ID auto-increment untuk catatan baru
    private Catatan selectedCatatan = null; // Referensi catatan yang sedang dipilih

    /**
     * Inisialisasi awal ketika controller dijalankan.
     * - Menghubungkan kolom tabel dengan properti objek Catatan
     * - Menangani klik pada baris tabel untuk menampilkan data ke form
     */
    @FXML
    public void initialize() {
        catatanList = FXCollections.observableArrayList();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        judul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        table.setItems(catatanList);

        // Isi form jika baris tabel diklik
        table.setOnMouseClicked(event -> {
            Catatan selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedCatatan = selected;
                txtFldJudul.setText(selected.getJudul());
                txtAreaKonten.setText(selected.getKonten());
                datePickerDeadline.setValue(LocalDate.parse(selected.getDeadline()));

            }
        });

        // Event ketika teks di searchBox berubah → langsung filter
        searchBox.textProperty().addListener((obs, oldVal, newVal) -> onSearch());
    }

    /**
     * Fungsi pencarian catatan berdasarkan judul.
     * Menampilkan catatan yang mengandung kata kunci dalam daftar.
     */
    private void onSearch() {
        String keyword = searchBox.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            table.setItems(catatanList);
        } else {
            ObservableList<Catatan> filtered = FXCollections.observableArrayList();
            for (Catatan c : catatanList) {
                if (c.getJudul().toLowerCase().contains(keyword)) {
                    filtered.add(c);
                }
            }
            table.setItems(filtered);
        }
    }

    /**
     * Fungsi untuk mengosongkan field pencarian.
     * Mengembalikan tampilan tabel ke daftar semua catatan.
     */
    @FXML
    private void onClearSearchClick() {
        searchBox.clear();
        table.setItems(catatanList);
        selectedCatatan = null;
        table.getSelectionModel().clearSelection();
    }

    /**
     * Fungsi ketika tombol "Simpan" diklik.
     * Menambah catatan baru jika belum ada yang dipilih,
     * atau mengubah data catatan yang sudah ada.
     */
    @FXML
    private void onBtnSimpanClick() {
        String judulText = txtFldJudul.getText().trim();
        String isiText = txtAreaKonten.getText().trim();
        String deadlineText = datePickerDeadline.getValue() != null ? datePickerDeadline.getValue().toString() : "";

        if (!judulText.isEmpty() && !isiText.isEmpty()) {
            if (selectedCatatan == null) {
                // Tambah data baru
                Catatan catatan = new Catatan(nextId++, judulText, isiText, deadlineText);
                catatanList.add(catatan);
                showAlert(Alert.AlertType.INFORMATION, "Catatan Ditambahkan!");
            } else {
                // Edit catatan
                selectedCatatan.setJudul(judulText);
                selectedCatatan.setKonten(isiText);
                selectedCatatan.setDeadline(deadlineText);
                table.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Catatan Diubah!");
            }
            clearForm();
        } else {
            showAlert(Alert.AlertType.WARNING, "Judul dan Isi tidak boleh kosong.");
        }
    }


    /**
     * Fungsi ketika tombol "Hapus" diklik.
     * Menghapus catatan yang sedang dipilih dari daftar.
     */
    @FXML
    private void onBtnHapus() {
        Catatan selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            catatanList.remove(selected);
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Catatan Dihapus!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilih catatan yang ingin dihapus.");
        }
    }

    /**
     * Fungsi untuk menampilkan popup pesan (Alert) ke pengguna.
     * @param type Tipe alert (INFORMATION, WARNING, dll.)
     * @param message Pesan yang ditampilkan
     */
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }

    /**
     * Mengosongkan form input dan reset pilihan di tabel.
     */
    private void clearForm() {
        txtFldJudul.clear();
        txtAreaKonten.clear();
        txtFldJudul.requestFocus();
        selectedCatatan = null;
        table.getSelectionModel().clearSelection();
        datePickerDeadline.setValue(null);

    }
}
