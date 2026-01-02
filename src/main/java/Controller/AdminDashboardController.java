package Controller;

import DAO.PemilikKosDAO;
import Model.PemilikKos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    // ===== Label Statistik =====
    @FXML private Label lblTotalOwners;
    @FXML private Label lblVerifiedCount;
    @FXML private Label lblPendingCount;
    @FXML private Label lblRejectedCount;

    // ===== Table Pemilik =====
    @FXML private TableView<PemilikKos> tblPemilik;
    @FXML private TableColumn<PemilikKos, Integer> colPemilikId;
    @FXML private TableColumn<PemilikKos, String> colPemilikNama;
    @FXML private TableColumn<PemilikKos, String> colPemilikEmail;
    @FXML private TableColumn<PemilikKos, String> colPemilikPhone;
    @FXML private TableColumn<PemilikKos, Integer> colPemilikJumlahKos;
    @FXML private TableColumn<PemilikKos, String> colPemilikStatus;

    // ===== Table Pending =====
    @FXML private TableView<PemilikKos> tblPendingPemilik;
    @FXML private TableColumn<PemilikKos, Integer> colPendingPemilikId;
    @FXML private TableColumn<PemilikKos, String> colPendingPemilikNama;
    @FXML private TableColumn<PemilikKos, String> colPendingPemilikEmail;
    @FXML private TableColumn<PemilikKos, String> colPendingPemilikPhone;
    @FXML private TableColumn<PemilikKos, String> colPendingPemilikTanggalDaftar;

    private final PemilikKosDAO pemilikDAO = new PemilikKosDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        loadStatistics();
        loadPemilik();
        loadPendingPemilik();
    }

    private void initTable() {
        colPemilikId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPemilikNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colPemilikEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPemilikPhone.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colPemilikJumlahKos.setCellValueFactory(new PropertyValueFactory<>("jumlahKos"));
        colPemilikStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colPendingPemilikId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPendingPemilikNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colPendingPemilikEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPendingPemilikPhone.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colPendingPemilikTanggalDaftar.setCellValueFactory(new PropertyValueFactory<>("tanggalDaftar"));
    }

    private void loadStatistics() {
        lblTotalOwners.setText(String.valueOf(pemilikDAO.getTotalOwners()));
        lblVerifiedCount.setText(String.valueOf(pemilikDAO.getVerifiedCount()));
        lblPendingCount.setText(String.valueOf(pemilikDAO.getPendingCount()));
        lblRejectedCount.setText(String.valueOf(pemilikDAO.getRejectedCount()));
    }

    private void loadPemilik() {
        ObservableList<PemilikKos> data =
                FXCollections.observableArrayList(pemilikDAO.getAllPemilikKos());
        tblPemilik.setItems(data);
    }

    private void loadPendingPemilik() {
        ObservableList<PemilikKos> data =
                FXCollections.observableArrayList(pemilikDAO.getPendingPemilikKos());
        tblPendingPemilik.setItems(data);
    }

    // ===== BUTTON ACTION =====
    @FXML
    private void handleVerifyPemilik() {
        PemilikKos p = tblPemilik.getSelectionModel().getSelectedItem();
        if (p != null) {
            pemilikDAO.verifyPemilik(p.getId());
            refresh();
        }
    }

    @FXML
    private void handleRejectPemilik() {
        PemilikKos p = tblPemilik.getSelectionModel().getSelectedItem();
        if (p != null) {
            pemilikDAO.rejectPemilik(p.getId(), "Ditolak oleh admin");
            refresh();
        }
    }

    @FXML
    private void handleDeletePemilik() {
        PemilikKos p = tblPemilik.getSelectionModel().getSelectedItem();
        if (p != null) {
            pemilikDAO.deletePemilik(p.getId());
            refresh();
        }
    }

    @FXML
    private void handleVerifyPendingPemilik() {
        PemilikKos p = tblPendingPemilik.getSelectionModel().getSelectedItem();
        if (p != null) {
            pemilikDAO.verifyPemilik(p.getId());
            refresh();
        }
    }

    @FXML
    private void handleRejectPendingPemilik() {
        PemilikKos p = tblPendingPemilik.getSelectionModel().getSelectedItem();
        if (p != null) {
            pemilikDAO.rejectPemilik(p.getId(), "Ditolak oleh admin");
            refresh();
        }
    }

    private void refresh() {
        loadStatistics();
        loadPemilik();
        loadPendingPemilik();
    }
}
