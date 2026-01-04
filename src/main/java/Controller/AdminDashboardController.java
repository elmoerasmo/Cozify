package Controller;

import DAO.KosDAO;
import DAO.PemilikKosDAO;
import Model.Kos;
import Model.PemilikKos;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.stage.StageStyle;

public class AdminDashboardController implements Initializable {

    @FXML private Label lblTotalOwners;
    @FXML private Label lblVerifiedCount;
    @FXML private Label lblPendingCount;
    @FXML private Label lblRejectedCount;

    @FXML private TableView<PemilikKos> tblPemilik;
    @FXML private TableColumn<PemilikKos, Integer> colPemilikId;
    @FXML private TableColumn<PemilikKos, String> colPemilikNama;
    @FXML private TableColumn<PemilikKos, String> colPemilikEmail;
    @FXML private TableColumn<PemilikKos, String> colPemilikPhone;
    @FXML private TableColumn<PemilikKos, Integer> colPemilikJumlahKos;
    @FXML private TableColumn<PemilikKos, String> colPemilikStatus;

    @FXML private TableView<Kos> tblPendingKos;
    @FXML private TableColumn<Kos, Integer> colKosId;
    @FXML private TableColumn<Kos, String> colKosNama;
    @FXML private TableColumn<Kos, String> colKosTipe;
    @FXML private TableColumn<Kos, Double> colKosHarga;
    @FXML private TableColumn<Kos, String> colKosStatus;

    @FXML private FlowPane kosCardsContainer;
    @FXML private Button profileButton;    


    private final PemilikKosDAO pemilikDAO = new PemilikKosDAO();
    private final KosDAO kosDAO = new KosDAO();
    public static Stage adminStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        profileButton.setOnAction(e -> openProfile());
        initTableColumns();
        refreshAllData();
    }

    private void initTableColumns() {
        colPemilikId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPemilikNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colPemilikEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPemilikPhone.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colPemilikJumlahKos.setCellValueFactory(new PropertyValueFactory<>("jumlahKos"));
        colPemilikStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colKosId.setCellValueFactory(new PropertyValueFactory<>("idKos"));
        colKosNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colKosTipe.setCellValueFactory(new PropertyValueFactory<>("tipeKos"));
        colKosHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colKosStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void refreshAllData() {
        loadStatistics();
        loadTables();
        loadKosCards();
    }

    private void loadStatistics() {
        lblTotalOwners.setText(String.valueOf(pemilikDAO.getTotalOwners()));
        lblVerifiedCount.setText(String.valueOf(pemilikDAO.getVerifiedCount()));
        lblPendingCount.setText(String.valueOf(pemilikDAO.getPendingCount()));
        lblRejectedCount.setText(String.valueOf(pemilikDAO.getRejectedCount()));
    }

    private void loadTables() {
        tblPemilik.setItems(FXCollections.observableArrayList(pemilikDAO.getAllPemilikKos()));
        
        List<Kos> allKos = kosDAO.getAllKos();
        List<Kos> pendingKos = allKos.stream()
                .filter(k -> "Menunggu Verifikasi".equalsIgnoreCase(k.getStatus()))
                .collect(Collectors.toList());
        tblPendingKos.setItems(FXCollections.observableArrayList(pendingKos));
    }

    private void loadKosCards() {
        kosCardsContainer.getChildren().clear();
        List<Kos> verifiedKos = kosDAO.getAllKos().stream()
                .filter(k -> "Terverifikasi".equalsIgnoreCase(k.getStatus()))
                .collect(Collectors.toList());

        for (Kos kos : verifiedKos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/KosCard.fxml"));
                Parent card = loader.load();
                KosCardController controller = loader.getController();
                controller.setData(kos);
                kosCardsContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleVerifyPemilik() {
        PemilikKos selected = tblPemilik.getSelectionModel().getSelectedItem();
        if (selected != null) {
            pemilikDAO.verifyPemilik(selected.getId());
            refreshAllData();
        } else {
            showAlert("Peringatan", "Pilih pemilik kos terlebih dahulu");
        }
    }

    @FXML
    private void handleRejectPemilik() {
        PemilikKos selected = tblPemilik.getSelectionModel().getSelectedItem();
        if (selected != null) {
            pemilikDAO.rejectPemilik(selected.getId(), "Ditolak admin");
            refreshAllData();
        } else {
            showAlert("Peringatan", "Pilih pemilik kos terlebih dahulu");
        }
    }

    @FXML
    private void handleDeletePemilik() {
        PemilikKos selected = tblPemilik.getSelectionModel().getSelectedItem();
        if (selected != null) {
            pemilikDAO.deletePemilik(selected.getId());
            refreshAllData();
        } else {
            showAlert("Peringatan", "Pilih pemilik kos terlebih dahulu");
        }
    }

    @FXML
    private void handleVerifyKos() {
        Kos selected = tblPendingKos.getSelectionModel().getSelectedItem();
        if (selected != null) {
            kosDAO.updateStatus(selected.getIdKos(), "Terverifikasi");
            refreshAllData();
            showAlert("Sukses", "Kos berhasil diverifikasi");
        } else {
            showAlert("Peringatan", "Pilih data kos terlebih dahulu");
        }
    }

    @FXML
    private void handleRejectKos() {
        Kos selected = tblPendingKos.getSelectionModel().getSelectedItem();
        if (selected != null) {
            kosDAO.updateStatus(selected.getIdKos(), "Ditolak");
            refreshAllData();
            showAlert("Sukses", "Kos telah ditolak");
        } else {
            showAlert("Peringatan", "Pilih data kos terlebih dahulu");
        }
    }

    @FXML
    private void goToDashboard(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/Dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Profile.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            ProfileController controller = loader.getController();
            controller.setSource("ADMIN_DASHBOARD");
            stage.setScene(new Scene(root));
            stage.setTitle("Profil Pengguna");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    void setStage(Stage stage){
        this.adminStage = stage;
    }
}