package org.example.week10;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
    @FXML private TextField searchBox;
    @FXML private Button btnLogout;

    private final ObservableList<Catatan> catatanList = FXCollections.observableArrayList(DBManager.getInstance().getAllCatatan());
    private final Map<Catatan, SimpleStringProperty> countdownMap = new HashMap<>();
    private Thread countdownThread;

    @FXML
    private void initialize() {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        judul.setCellValueFactory(cellData -> cellData.getValue().judulProperty());
        deadline.setCellValueFactory(cellData -> cellData.getValue().deadlineProperty());

        countdown.setCellValueFactory(cellData -> {
            Catatan c = cellData.getValue();
            countdownMap.putIfAbsent(c, new SimpleStringProperty(""));
            return countdownMap.get(c);
        });

        searchBox.textProperty().addListener((obs, oldVal, newVal) -> onSearch());
        tableViewCatatan.setItems(catatanList);

        startCountdownThread();
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
            for (Catatan catatan : catatanList) {
                try {
                    String deadlineStr = catatan.getDeadline().trim();
                    LocalDateTime deadlineDateTime;

                    if (deadlineStr.length() == 10) {
                        deadlineDateTime = LocalDateTime.parse(deadlineStr + " 00:00:00",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } else {
                        deadlineDateTime = LocalDateTime.parse(deadlineStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    LocalDateTime now = LocalDateTime.now();
                    long totalSeconds = ChronoUnit.SECONDS.between(now, deadlineDateTime);

                    String text;
                    if (totalSeconds >= 0) {
                        long days = totalSeconds / (24 * 3600);
                        long hours = (totalSeconds % (24 * 3600)) / 3600;
                        long minutes = (totalSeconds % 3600) / 60;
                        long seconds = totalSeconds % 60;

                        text = String.format(
                                "%d %s %02d %s %02d %s %02d %s",
                                days, (days == 1 ? "hari" : "hari"),
                                hours, (hours == 1 ? "jam" : "jam"),
                                minutes, (minutes == 1 ? "menit" : "menit"),
                                seconds, (seconds == 1 ? "detik" : "detik")
                        );
                    } else {
                        text = "Terlambat";
                    }

                    countdownMap.computeIfAbsent(catatan, c -> new SimpleStringProperty()).set(text);
                } catch (Exception e) {
                    countdownMap.computeIfAbsent(catatan, c -> new SimpleStringProperty()).set("Format salah");
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
                if (c.getJudul().toLowerCase().contains(keyword)) {
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

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void addCatatanBaru(Catatan baru) {
        DBManager.getInstance().tambahCatatan(baru);
        catatanList.clear();
        catatanList.addAll(DBManager.getInstance().getAllCatatan());
    }

    @FXML
    public void onBtnAddClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/week10/add-task-view.fxml"));
            Parent root = loader.load();

            AddTaskController controller = loader.getController();
            controller.setCatatanController(this);

            Stage stage = new Stage();
            stage.setTitle("Tambah Catatan");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal membuka form tambah catatan.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Hapus session saat logout
            SessionManager.clearSession();

            // Tutup jendela saat ini
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.close();

            // Tampilkan halaman login
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
}
