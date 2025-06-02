package org.example.week10.ControllerFile;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.example.week10.*;
import org.example.week10.manager.DBManager;
import org.example.week10.manager.SessionManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class CatatanController {

    @FXML private TableView<Catatan> tableViewCatatan;
    @FXML private TableColumn<Catatan, Integer> id;
    @FXML private TableColumn<Catatan, String> judul;
    @FXML private TableColumn<Catatan, String> deadline;
    @FXML private TableColumn<Catatan, String> countdown;
    @FXML private TableColumn<Catatan, Boolean> status;
    @FXML private TableColumn<Catatan, String> kategori;
    @FXML private TextField searchBox;
    @FXML private Button btnLogout;

    private final ObservableList<Catatan> catatanList = FXCollections.observableArrayList();
    private final Map<Catatan, SimpleStringProperty> countdownMap = new HashMap<>();
    private Thread countdownThread;

    @FXML
    private void initialize() {
        catatanList.setAll(DBManager.getInstance().getAllCatatan());
        tableViewCatatan.setItems(catatanList);
        tableViewCatatan.setEditable(true);

        id.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        judul.setCellValueFactory(data -> data.getValue().judulProperty());
        deadline.setCellValueFactory(data -> data.getValue().deadlineProperty());
        kategori.setCellValueFactory(data -> data.getValue().kategoriProperty());

        status.setCellValueFactory(data -> data.getValue().selesaiProperty());
        status.setCellFactory(tc -> new CheckBoxTableCell<>());

        status.setEditable(true);

        for (Catatan catatan : catatanList) {
            catatan.selesaiProperty().addListener((obs, oldVal, newVal) -> {
                DBManager.getInstance().updateCatatanStatus(catatan.getId(), newVal);
            });
        }


        countdown.setCellValueFactory(data -> {
            Catatan c = data.getValue();
            countdownMap.putIfAbsent(c, new SimpleStringProperty(""));
            return countdownMap.get(c);
        });

        searchBox.setPromptText("Cari judul atau kategori...");
        searchBox.textProperty().addListener((obs, oldVal, newVal) -> onSearch());

        startCountdownThread();

        tableViewCatatan.setRowFactory(tv -> {
            TableRow<Catatan> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
                    Catatan selectedCatatan = row.getItem();
                    showDetailView(selectedCatatan);
                }
            });
            return row;
        });
    }

    private void startCountdownThread() {
        countdownThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    updateCountdown();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        countdownThread.setDaemon(true);
        countdownThread.start();
    }

    private void updateCountdown() {
        Platform.runLater(() -> {
            for (Catatan c : catatanList) {
                try {
                    String deadlineStr = c.getDeadline().trim();
                    LocalDateTime deadlineDateTime = deadlineStr.length() == 10
                            ? LocalDateTime.parse(deadlineStr + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            : LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), deadlineDateTime);

                    String text = seconds >= 0
                            ? String.format("%d hari %d jam %d menit",
                            seconds / (3600 * 24),
                            (seconds % (3600 * 24)) / 3600,
                            (seconds % 3600) / 60)
                            : "Terlambat";

                    countdownMap.get(c).set(text);
                } catch (Exception e) {
                    countdownMap.get(c).set("Format salah");
                }
            }
        });
    }

    private void onSearch() {
        String keyword = searchBox.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            tableViewCatatan.setItems(catatanList);
        } else {
            ObservableList<Catatan> filtered = FXCollections.observableArrayList();
            for (Catatan c : catatanList) {
                if (c.getJudul().toLowerCase().contains(keyword) ||
                        c.getKategori().toLowerCase().contains(keyword)) {
                    filtered.add(c);
                }
            }
            tableViewCatatan.setItems(filtered);
        }
    }

    @FXML
    private void onBtnHapus() {
        Catatan selected = tableViewCatatan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DBManager.getInstance().hapusCatatan(selected.getId());
            catatanList.remove(selected);
            countdownMap.remove(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilih catatan yang ingin dihapus.");
        }
    }

    public void addCatatanBaru(Catatan catatanBaru) {
        int newId = catatanList.size() + 1;
        catatanBaru.setId(newId);

        catatanList.add(catatanBaru);
    }

    @FXML
    private void onBtnAddClick(javafx.event.ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/week10/add-task-view.fxml"));
            Parent root = loader.load();

            AddTaskController controller = loader.getController();
            controller.setCatatanController(this);

            Stage stage = new Stage();
            stage.setTitle("Tambah Catatan");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal membuka form tambah catatan.");
        }
    }

    private void showDetailView(Catatan catatan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/week10/detail-view.fxml"));
            Parent root = loader.load();

            DetailController controller = loader.getController();
            controller.setDetail(catatan);

            Stage stage = new Stage();
            stage.setTitle("Detail Catatan");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal membuka detail catatan.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        try {
            SessionManager.clearSession();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/week10/login-view.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal logout.");
        }
    }

    @FXML
    private void onBtnEdit() {
        Catatan selected = tableViewCatatan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/week10/edit-task-view.fxml"));
                Parent root = loader.load();

                EditTaskController controller = loader.getController();
                controller.setData(this, selected);

                Stage stage = new Stage();
                stage.setTitle("Edit Catatan");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Gagal membuka form edit catatan.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilih catatan yang ingin diedit.");
        }
    }

    public void refreshData() {
        catatanList.setAll(DBManager.getInstance().getAllCatatan());
    }
}
