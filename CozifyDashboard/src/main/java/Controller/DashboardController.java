package controller;

import DaoAdmin.KosDAO;
import DaoAdmin.PemilikKosDAO;
import DaoAdmin.PendingItemDAO;
import DaoOwner.MyKosDAO;
import DaoOwner.LaporanDAO;
import ModelAdmin.Kos;
import ModelAdmin.PemilikKos;
import ModelOwner.MyKos;
import ModelOwner.Laporan;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DashboardController {
    
    //Dashboard
    @FXML private VBox adminDashboard;
    @FXML private VBox ownerDashboard;
    
    //Header
    @FXML private Label lblCurrentRole;
    @FXML private Button btnSwitchToAdmin;
    @FXML private Button btnSwitchToOwner;
    
    //Buat Admin
    @FXML private Label lblPendingCount;
    @FXML private Label lblVerifiedCount;
    @FXML private Label lblRejectedCount;
    @FXML private Label lblTotalOwners;
    
    @FXML private TableView<PemilikKos> tblPemilik;
    @FXML private TableColumn<PemilikKos, Integer> colPemilikId;
    @FXML private TableColumn<PemilikKos, String> colPemilikNama;
    @FXML private TableColumn<PemilikKos, String> colPemilikEmail;
    @FXML private TableColumn<PemilikKos, String> colPemilikPhone;
    @FXML private TableColumn<PemilikKos, Integer> colPemilikJumlahKos;
    @FXML private TableColumn<PemilikKos, String> colPemilikStatus;
    
    @FXML private TableView<Kos> tblKos;
    @FXML private TableColumn<Kos, Integer> colKosId;
    @FXML private TableColumn<Kos, String> colKosNama;
    @FXML private TableColumn<Kos, String> colKosPemilik;
    @FXML private TableColumn<Kos, String> colKosAlamat;
    @FXML private TableColumn<Kos, String> colKosHarga;
    @FXML private TableColumn<Kos, String> colKosStatus;
    
    //Buat Owner
    @FXML private Label lblTotalKos;
    @FXML private Label lblTotalKamar;
    @FXML private Label lblKamarTerisi;
    @FXML private Label lblPendapatan;
    
    @FXML private TableView<MyKos> tblMyKos;
    @FXML private TableColumn<MyKos, Integer> colMyKosId;
    @FXML private TableColumn<MyKos, String> colMyKosNama;
    @FXML private TableColumn<MyKos, String> colMyKosAlamat;
    @FXML private TableColumn<MyKos, String> colMyKosHarga;
    @FXML private TableColumn<MyKos, Integer> colMyKosTotalKamar;
    @FXML private TableColumn<MyKos, Integer> colMyKosTerisi;
    @FXML private TableColumn<MyKos, Integer> colMyKosTersedia;
    
    @FXML private TableView<Laporan> tblLaporan;
    @FXML private TableColumn<Laporan, String> colTanggal;
    @FXML private TableColumn<Laporan, String> colKosLaporan;
    @FXML private TableColumn<Laporan, String> colKategori;
    @FXML private TableColumn<Laporan, String> colKeterangan;
    @FXML private TableColumn<Laporan, String> colPemasukan;
    @FXML private TableColumn<Laporan, String> colPengeluaran;
    
    @FXML private Label lblTotalPemasukan;
    @FXML private Label lblTotalPengeluaran;
    @FXML private Label lblKeuntungan;
    
    //DAO
    private PemilikKosDAO pemilikKosDAO;
    private KosDAO kosDAO;
    private PendingItemDAO pendingItemDAO;
    private MyKosDAO myKosDAO;
    private LaporanDAO laporanDAO;
    
    private int currentOwnerId = 1;
    private String currentRole = "ADMIN";
    
    @FXML
    public void initialize() {
        pemilikKosDAO = new PemilikKosDAO();
        kosDAO = new KosDAO();
        pendingItemDAO = new PendingItemDAO();
        myKosDAO = new MyKosDAO(currentOwnerId);
        laporanDAO = new LaporanDAO(currentOwnerId);
        
        setupAdminTables();
        setupOwnerTables();
        
        loadAdminData();
    }
    
    //Switch
    @FXML
    private void handleSwitchToAdmin() {
        currentRole = "ADMIN";
        adminDashboard.setVisible(true);
        adminDashboard.setManaged(true);
        ownerDashboard.setVisible(false);
        ownerDashboard.setManaged(false);
        
        lblCurrentRole.setText("👨‍💼 ADMIN");
        btnSwitchToAdmin.setStyle("-fx-background-color: white; -fx-text-fill: #8CC1E9; -fx-padding: 8 16; -fx-background-radius: 20;");
        btnSwitchToOwner.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 20;");
        
        loadAdminData();
    }
    
    @FXML
    private void handleSwitchToOwner() {
        currentRole = "OWNER";
        adminDashboard.setVisible(false);
        adminDashboard.setManaged(false);
        ownerDashboard.setVisible(true);
        ownerDashboard.setManaged(true);
        
        lblCurrentRole.setText("🏠 PEMILIK KOS");
        btnSwitchToOwner.setStyle("-fx-background-color: white; -fx-text-fill: #8CC1E9; -fx-padding: 8 16; -fx-background-radius: 20;");
        btnSwitchToAdmin.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 20;");
        
        loadOwnerData();
    }
    
    //setup admin
    
    private void setupAdminTables() {
        colPemilikId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPemilikNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colPemilikEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPemilikPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPemilikJumlahKos.setCellValueFactory(new PropertyValueFactory<>("jumlahKos"));
        colPemilikStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        colPemilikStatus.setCellFactory(column -> new TableCell<PemilikKos, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "PENDING":
                            setStyle("-fx-background-color: #FFF3E0; -fx-text-fill: #FF9800;");
                            break;
                        case "VERIFIED":
                            setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #4CAF50;");
                            break;
                        case "REJECTED":
                            setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #f44336;");
                            break;
                    }
                }
            }
        });
        
        colKosId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colKosNama.setCellValueFactory(new PropertyValueFactory<>("namaKos"));
        colKosPemilik.setCellValueFactory(new PropertyValueFactory<>("pemilik"));
        colKosAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colKosHarga.setCellValueFactory(new PropertyValueFactory<>("formattedHarga"));
        colKosStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        colKosStatus.setCellFactory(column -> new TableCell<Kos, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "PENDING":
                            setStyle("-fx-background-color: #FFF3E0; -fx-text-fill: #FF9800;");
                            break;
                        case "VERIFIED":
                            setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #4CAF50;");
                            break;
                        case "REJECTED":
                            setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #f44336;");
                            break;
                    }
                }
            }
        });
    }
    
    private void loadAdminData() {
        int pending = pendingItemDAO.getPendingCount();
        int verified = pemilikKosDAO.countByStatus("VERIFIED");
        int rejected = pemilikKosDAO.countByStatus("REJECTED");
        int total = pemilikKosDAO.getTotalPemilikKos();
        
        lblPendingCount.setText(String.valueOf(pending));
        lblVerifiedCount.setText(String.valueOf(verified));
        lblRejectedCount.setText(String.valueOf(rejected));
        lblTotalOwners.setText(String.valueOf(total));
        
        tblPemilik.setItems(pemilikKosDAO.getAllPemilikKos());
        tblKos.setItems(kosDAO.getAllKos());
    }
    
    //setup owner
    private void setupOwnerTables() {
        colMyKosId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMyKosNama.setCellValueFactory(new PropertyValueFactory<>("namaKos"));
        colMyKosAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colMyKosHarga.setCellValueFactory(new PropertyValueFactory<>("formattedHarga"));
        colMyKosTotalKamar.setCellValueFactory(new PropertyValueFactory<>("totalKamar"));
        colMyKosTerisi.setCellValueFactory(new PropertyValueFactory<>("terisi"));
        colMyKosTersedia.setCellValueFactory(new PropertyValueFactory<>("tersedia"));
        
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKosLaporan.setCellValueFactory(new PropertyValueFactory<>("kosNama"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colPemasukan.setCellValueFactory(new PropertyValueFactory<>("formattedPemasukan"));
        colPengeluaran.setCellValueFactory(new PropertyValueFactory<>("formattedPengeluaran"));
        
        colKategori.setCellFactory(column -> new TableCell<Laporan, String>() {
            @Override
            protected void updateItem(String kategori, boolean empty) {
                super.updateItem(kategori, empty);
                if (empty || kategori == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(kategori);
                    if ("PEMASUKAN".equals(kategori)) {
                        setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #4CAF50;");
                    } else {
                        setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #f44336;");
                    }
                }
            }
        });
    }
    
    private void loadOwnerData() {
        int totalKos = myKosDAO.getTotalKos();
        int totalKamar = myKosDAO.getTotalKamar();
        int kamarTerisi = myKosDAO.getKamarTerisi();
        double pendapatan = myKosDAO.getPendapatanBulanIni();
        
        lblTotalKos.setText(String.valueOf(totalKos));
        lblTotalKamar.setText(String.valueOf(totalKamar));
        lblKamarTerisi.setText(String.valueOf(kamarTerisi));
        lblPendapatan.setText(String.format("Rp %,.0f", pendapatan));
        
        tblMyKos.setItems(myKosDAO.getMyKosList());
    }
    
    //admin
    @FXML
    private void handleViewPemilikDetail() {
        PemilikKos selected = tblPemilik.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih pemilik kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Pemilik Kos");
        alert.setHeaderText("Informasi Lengkap");
        alert.setContentText(
            "ID: " + selected.getId() + "\n" +
            "Nama: " + selected.getNama() + "\n" +
            "Email: " + selected.getEmail() + "\n" +
            "No. Telepon: " + selected.getPhone() + "\n" +
            "Jumlah Kos: " + selected.getJumlahKos() + "\n" +
            "Status: " + selected.getStatus()
        );
        alert.showAndWait();
    }
    
    @FXML
    private void handleVerifyPemilik() {
        PemilikKos selected = tblPemilik.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih pemilik kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Verifikasi");
        confirm.setContentText("Apakah Anda yakin ingin memverifikasi " + selected.getNama() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (pemilikKosDAO.updateStatus(selected.getId(), "VERIFIED")) {
                showAlert("Sukses", "Pemilik kos berhasil diverifikasi!", Alert.AlertType.INFORMATION);
                loadAdminData();
            }
        }
    }
    
    @FXML
    private void handleRejectPemilik() {
        PemilikKos selected = tblPemilik.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih pemilik kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Penolakan");
        confirm.setContentText("Apakah Anda yakin ingin menolak " + selected.getNama() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (pemilikKosDAO.updateStatus(selected.getId(), "REJECTED")) {
                showAlert("Sukses", "Pemilik kos berhasil ditolak!", Alert.AlertType.INFORMATION);
                loadAdminData();
            }
        }
    }
    
    @FXML
    private void handleDeletePemilik() {
        PemilikKos selected = tblPemilik.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih pemilik kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setContentText("Apakah Anda yakin ingin menghapus " + selected.getNama() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (pemilikKosDAO.deletePemilik(selected.getId())) {
                showAlert("Sukses", "Pemilik kos berhasil dihapus!", Alert.AlertType.INFORMATION);
                loadAdminData();
            }
        }
    }
    
    @FXML
    private void handleViewKosDetail() {
        Kos selected = tblKos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Kos");
        alert.setHeaderText("Informasi Lengkap");
        alert.setContentText(
            "ID: " + selected.getId() + "\n" +
            "Nama Kos: " + selected.getNamaKos() + "\n" +
            "Pemilik: " + selected.getPemilik() + "\n" +
            "Alamat: " + selected.getAlamat() + "\n" +
            "Harga: " + selected.getFormattedHarga() + "\n" +
            "Status: " + selected.getStatus()
        );
        alert.showAndWait();
    }
    
    @FXML
    private void handleVerifyKos() {
        Kos selected = tblKos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Verifikasi");
        confirm.setContentText("Apakah Anda yakin ingin memverifikasi " + selected.getNamaKos() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (kosDAO.updateStatus(selected.getId(), "VERIFIED")) {
                showAlert("Sukses", "Kos berhasil diverifikasi!", Alert.AlertType.INFORMATION);
                loadAdminData();
            }
        }
    }
    
    @FXML
    private void handleRejectKos() {
        Kos selected = tblKos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Penolakan");
        confirm.setContentText("Apakah Anda yakin ingin menolak " + selected.getNamaKos() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (kosDAO.updateStatus(selected.getId(), "REJECTED")) {
                showAlert("Sukses", "Kos berhasil ditolak!", Alert.AlertType.INFORMATION);
                loadAdminData();
            }
        }
    }
    
    //owner
    @FXML
    private void handleTambahKos() {
        Dialog<MyKos> dialog = new Dialog<>();
        dialog.setTitle("Tambah Kos Baru");
        
        ButtonType addButtonType = new ButtonType("Tambah", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        TextField txtNama = new TextField();
        txtNama.setPromptText("Nama Kos");
        TextField txtAlamat = new TextField();
        txtAlamat.setPromptText("Alamat");
        TextField txtHarga = new TextField();
        txtHarga.setPromptText("Harga per Bulan");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nama Kos:"), 0, 0);
        grid.add(txtNama, 1, 0);
        grid.add(new Label("Alamat:"), 0, 1);
        grid.add(txtAlamat, 1, 1);
        grid.add(new Label("Harga/Bulan:"), 0, 2);
        grid.add(txtHarga, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    MyKos kos = new MyKos();
                    kos.setNamaKos(txtNama.getText());
                    kos.setAlamat(txtAlamat.getText());
                    kos.setHarga(Double.parseDouble(txtHarga.getText()));
                    return kos;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Harga harus berupa angka!", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(kos -> {
            if (myKosDAO.addKos(kos)) {
                showAlert("Sukses", "Kos baru berhasil ditambahkan!", Alert.AlertType.INFORMATION);
                loadOwnerData();
            }
        });
    }
    
    @FXML
    private void handleViewDetail() {
        MyKos selected = tblMyKos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Kos");
        alert.setContentText(
            "ID: " + selected.getId() + "\n" +
            "Nama Kos: " + selected.getNamaKos() + "\n" +
            "Alamat: " + selected.getAlamat() + "\n" +
            "Harga: " + selected.getFormattedHarga() + "\n" +
            "Total Kamar: " + selected.getTotalKamar() + "\n" +
            "Terisi: " + selected.getTerisi() + "\n" +
            "Tersedia: " + selected.getTersedia()
        );
        alert.showAndWait();
    }
    
    @FXML
    private void handleEditKos() {
        MyKos selected = tblMyKos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Dialog<MyKos> dialog = new Dialog<>();
        dialog.setTitle("Edit Kos");
        
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
        
        TextField txtNama = new TextField(selected.getNamaKos());
        TextField txtAlamat = new TextField(selected.getAlamat());
        TextField txtHarga = new TextField(String.valueOf(selected.getHarga()));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nama Kos:"), 0, 0);
        grid.add(txtNama, 1, 0);
        grid.add(new Label("Alamat:"), 0, 1);
        grid.add(txtAlamat, 1, 1);
        grid.add(new Label("Harga/Bulan:"), 0, 2);
        grid.add(txtHarga, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    selected.setNamaKos(txtNama.getText());
                    selected.setAlamat(txtAlamat.getText());
                    selected.setHarga(Double.parseDouble(txtHarga.getText()));
                    return selected;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Harga harus berupa angka!", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(kos -> {
            if (myKosDAO.updateKos(kos)) {
                showAlert("Sukses", "Kos berhasil diupdate!", Alert.AlertType.INFORMATION);
                loadOwnerData();
            }
        });
    }
    
    @FXML
    private void handleHapusKos() {
        MyKos selected = tblMyKos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Hapus");
        confirm.setContentText("Apakah Anda yakin ingin menghapus " + selected.getNamaKos() + "?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (myKosDAO.deleteKos(selected.getId())) {
                showAlert("Sukses", "Kos berhasil dihapus!", Alert.AlertType.INFORMATION);
                loadOwnerData();
            }
        }
    }
    
    @FXML
    private void handleCetakLaporan() {
        showAlert("Info", "Fitur cetak laporan akan segera tersedia!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void handleExportLaporan() {
        showAlert("Info", "Fitur export laporan akan segera tersedia!", Alert.AlertType.INFORMATION);
    }
    
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}