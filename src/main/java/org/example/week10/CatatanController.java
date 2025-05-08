package org.example.week10;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CatatanController {

    @FXML private TextField txtFldJudul;
    @FXML private TextArea txtAreaKonten;
    @FXML private DatePicker datePickerDeadline;
    @FXML private Button btnSimpan;
    @FXML private Button btnHapus;
    @FXML private TextField searchBox;
    @FXML private Button btnClearSearch;
    @FXML private TableView<Catatan> table;
    @FXML private TableColumn<Catatan, Integer> id;
    @FXML private TableColumn<Catatan, String> judul;
    @FXML private TableColumn<Catatan, LocalDate> deadline;
    @FXML private TableColumn<Catatan, String> countdown;
    @FXML private Label labelJam;

    private ObservableList<Catatan> catatanList;
    private int nextId = 1;
    private Catatan selectedCatatan = null;

    @FXML
    public void initialize() {
        catatanList = FXCollections.observableArrayList();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        judul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        countdown.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCountdown()));
        table.setItems(catatanList);

        table.setOnMouseClicked(event -> {
            Catatan selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedCatatan = selected;
                txtFldJudul.setText(selected.getJudul());
                txtAreaKonten.setText(selected.getKonten());
                datePickerDeadline.setValue(selected.getDeadline());
            }
        });

        searchBox.textProperty().addListener((obs, oldVal, newVal) -> onSearch());

        mulaiJamRealtime();
        updateCountdownSetiapDetik();
        periksaDeadlineDekat();
    }

    private void mulaiJamRealtime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    LocalTime now = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    labelJam.setText(now.format(formatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateCountdownSetiapDetik() {
        Timeline countdownUpdater = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> table.refresh())
        );
        countdownUpdater.setCycleCount(Timeline.INDEFINITE);
        countdownUpdater.play();
    }

    private void periksaDeadlineDekat() {
        Timeline deadlineChecker = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    for (Catatan catatan : catatanList) {
                        if (catatan.getDeadline() != null &&
                                ChronoUnit.DAYS.between(LocalDate.now(), catatan.getDeadline()) == 0) {
                            showAlert(Alert.AlertType.WARNING, "Deadline hari ini: " + catatan.getJudul());
                        }
                    }
                })
        );
        deadlineChecker.setCycleCount(Timeline.INDEFINITE);
        deadlineChecker.play();
    }

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

    @FXML
    private void onClearSearchClick() {
        searchBox.clear();
        table.setItems(catatanList);
        selectedCatatan = null;
        table.getSelectionModel().clearSelection();
    }

    @FXML
    private void onBtnSimpanClick() {
        String judulText = txtFldJudul.getText().trim();
        String isiText = txtAreaKonten.getText().trim();
        LocalDate deadlineValue = datePickerDeadline.getValue();

        if (!judulText.isEmpty() && !isiText.isEmpty() && deadlineValue != null) {
            if (selectedCatatan == null) {
                Catatan catatan = new Catatan(nextId++, judulText, isiText, deadlineValue);
                catatanList.add(catatan);
                showAlert(Alert.AlertType.INFORMATION, "Catatan Ditambahkan!");
            } else {
                selectedCatatan.setJudul(judulText);
                selectedCatatan.setKonten(isiText);
                selectedCatatan.setDeadline(deadlineValue);
                table.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Catatan Diubah!");
            }
            clearForm();
        } else {
            showAlert(Alert.AlertType.WARNING, "Judul, Isi, dan Deadline tidak boleh kosong.");
        }
    }

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

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.show();
    }

    private void clearForm() {
        txtFldJudul.clear();
        txtAreaKonten.clear();
        datePickerDeadline.setValue(null);
        txtFldJudul.requestFocus();
        selectedCatatan = null;
        table.getSelectionModel().clearSelection();
    }
}
