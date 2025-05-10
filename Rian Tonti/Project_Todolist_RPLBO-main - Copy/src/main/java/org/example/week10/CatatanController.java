package org.example.week10;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.time.LocalDate;

public class CatatanController {



    public Object add;
    @FXML private TableView<Catatan> tableViewCatatan;
    @FXML private TableColumn<Catatan, Integer> id;
    @FXML private TableColumn<Catatan, String> judul;
    @FXML private TableColumn<Catatan, String> deadline;
    @FXML private TextField searchBox;

    private final ObservableList<Catatan> catatanList = FXCollections.observableArrayList(DBManager.getInstance().getAllCatatan());
    private Catatan selectedCatatan = null;


    @FXML
    private void initialize() {
        id.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        judul.setCellValueFactory(cellData -> cellData.getValue().judulProperty());
        deadline.setCellValueFactory(cellData -> cellData.getValue().deadlineProperty());
        searchBox.textProperty().addListener((obs, oldVal, newVal) -> onSearch());
        tableViewCatatan.setItems(catatanList);
    }

    private void handleTableClick(MouseEvent event) {
        Catatan selected = tableViewCatatan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedCatatan = selected;
        }
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
            clearForm();
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilih catatan yang ingin dihapus.");
        }
    }

    private void clearForm() {

        selectedCatatan = null;
        tableViewCatatan.getSelectionModel().clearSelection();
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


    public void onClearSearchClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onBtnAddClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/week10/add-task-view.fxml"));
            Parent root = loader.load();

            AddTaskController controller = loader.getController();
            controller.setCatatanController(this); // Menghubungkan controller

            Stage stage = new Stage();
            stage.setTitle("Tambah Catatan");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal membuka form tambah catatan.");
        }
    }

}
