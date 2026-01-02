package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Kos;
import Model.Laporan;
import DAO.KosDAO;
import DAO.LaporanDAO;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.BaseColor;

import java.io.*;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class OwnerDashboardController implements Initializable {
    
    // FXML Components
    @FXML private VBox notificationBox;
    @FXML private Label lblNotificationMessage;
    @FXML private Label lblTotalKos;
    @FXML private Label lblTotalKamar;
    @FXML private Label lblKamarTerisi;
    @FXML private Label lblPendapatan;
    @FXML private FlowPane myKosCardsContainer;
    @FXML private ComboBox<String> cbPeriode;
    @FXML private ComboBox<String> cbFilterKos;
    @FXML private Label lblTotalPemasukan;
    @FXML private Label lblTotalPengeluaran;
    @FXML private Label lblKeuntungan;
    @FXML private TableView<Laporan> tblLaporan;
    @FXML private TableColumn<Laporan, String> colTanggal;
    @FXML private TableColumn<Laporan, String> colKosLaporan;
    @FXML private TableColumn<Laporan, String> colKategori;
    @FXML private TableColumn<Laporan, String> colKeterangan;
    @FXML private TableColumn<Laporan, Double> colPemasukan;
    @FXML private TableColumn<Laporan, Double> colPengeluaran;
    
    // DAOs and Data
    private KosDAO kosDAO;
    private LaporanDAO laporanDAO;
    private int currentOwnerId = 1; // TODO: Get from login session
    private NumberFormat currencyFormat;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kosDAO = new KosDAO();
        laporanDAO = new LaporanDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        setupTables();
        setupComboBoxes();
        loadData();
    }
    
    private void setupTables() {
        colTanggal.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(() -> {
                if (cellData.getValue().getTanggal() != null) {
                    return cellData.getValue().getTanggal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                return "";
            })
        );
        colKosLaporan.setCellValueFactory(new PropertyValueFactory<>("namaKos"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colPemasukan.setCellValueFactory(new PropertyValueFactory<>("pemasukan"));
        colPengeluaran.setCellValueFactory(new PropertyValueFactory<>("pengeluaran"));
    }
    
    private void setupComboBoxes() {
        // Setup Periode ComboBox
        if (cbPeriode != null) {
            ObservableList<String> periodeItems = FXCollections.observableArrayList(
                "Hari Ini",
                "Minggu Ini",
                "Bulan Ini",
                "Tahun Ini"
            );
            cbPeriode.setItems(periodeItems);
            cbPeriode.setValue("Bulan Ini");
            System.out.println("✅ ComboBox Periode initialized");
        } else {
            System.out.println("⚠️ cbPeriode is NULL in FXML!");
        }
        
        // Setup Filter Kos ComboBox
        setupKosFilterComboBox();
    }
    
    private void setupKosFilterComboBox() {
        if (cbFilterKos != null) {
            ObservableList<String> items = FXCollections.observableArrayList("Semua Kos");
            items.addAll(kosDAO.getKosNamesByOwner(currentOwnerId));
            cbFilterKos.setItems(items);
            cbFilterKos.setValue("Semua Kos");
            System.out.println("✅ ComboBox Filter Kos initialized");
        } else {
            System.out.println("⚠️ cbFilterKos is NULL in FXML!");
        }
    }
    
    private void loadData() {
        // Load statistics
        lblTotalKos.setText(String.valueOf(kosDAO.getTotalKosByOwner(currentOwnerId)));
        lblTotalKamar.setText(String.valueOf(kosDAO.getTotalKamarByOwner(currentOwnerId)));
        lblKamarTerisi.setText(String.valueOf(kosDAO.getKamarTerisiByOwner(currentOwnerId)));
        
        double pendapatan = laporanDAO.getPendapatanBulanIni(currentOwnerId);
        lblPendapatan.setText(formatCurrency(pendapatan));
        
        // Check for rejected kos
        if (kosDAO.hasRejectedKosByOwner(currentOwnerId)) {
            int rejectedCount = kosDAO.getRejectedKosCountByOwner(currentOwnerId);
            notificationBox.setVisible(true);
            notificationBox.setManaged(true);
            lblNotificationMessage.setText("Anda memiliki " + rejectedCount + " kos yang ditolak. Klik pada kos untuk melihat alasan penolakan.");
        }
        
        // Load kos cards
        loadMyKosCards();
        
        System.out.println("✅ Owner Data Loaded Complete");
    }
    
    private void loadMyKosCards() {
        myKosCardsContainer.getChildren().clear();
        kosDAO.getKosByOwner(currentOwnerId).forEach(kos -> myKosCardsContainer.getChildren().add(createMyKosCard(kos)));
    }
    
    private VBox createMyKosCard(Kos kos) {
        VBox card = new VBox(0);
        card.setPrefWidth(340);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3); -fx-cursor: hand;");
        
        // Image Section
        StackPane imagePane = new StackPane();
        imagePane.setPrefHeight(200);
        imagePane.setStyle("-fx-background-radius: 15 15 0 0;");
        
        if (kos.getFoto() != null && !kos.getFoto().isEmpty()) {
            try {
                File imgFile = new File(kos.getFoto());
                if (imgFile.exists()) {
                    Image img = new Image(imgFile.toURI().toString());
                    ImageView imageView = new ImageView(img);
                    imageView.setFitWidth(340);
                    imageView.setFitHeight(200);
                    imageView.setPreserveRatio(false);
                    imageView.setSmooth(true);
                    imagePane.getChildren().add(imageView);
                } else {
                    addPlaceholderImage(imagePane);
                }
            } catch (Exception e) {
                addPlaceholderImage(imagePane);
            }
        } else {
            addPlaceholderImage(imagePane);
        }
        
        // Info Section
        VBox infoBox = new VBox(12);
        infoBox.setPadding(new Insets(20));
        
        Label locationLabel = new Label(kos.getAlamat());
        locationLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");
        locationLabel.setWrapText(true);
        
        Label nameLabel = new Label(kos.getNama());
        nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333;");
        nameLabel.setWrapText(true);
        
        HBox detailsBox = new HBox(10);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        
        Label priceLabel = new Label(formatCurrency(kos.getHarga()) + "/bulan");
        priceLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        if ("Terverifikasi".equals(kos.getStatus())) {
            HBox ratingBox = new HBox(5);
            ratingBox.setAlignment(Pos.CENTER);
            Label starLabel = new Label("★");
            starLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 14;");
            Label ratingLabel = new Label(String.format("%.1f", kos.getRating()));
            ratingLabel.setStyle("-fx-font-size: 14;");
            ratingBox.getChildren().addAll(starLabel, ratingLabel);
            detailsBox.getChildren().addAll(priceLabel, spacer, ratingBox);
        } else {
            detailsBox.getChildren().add(priceLabel);
        }
        
        Label roomsLabel = new Label("📦 Sisa " + kos.getKamarTersedia() + " dari " + kos.getTotalKamar() + " kamar");
        roomsLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #666;");
        
        Label statusBadge = createStatusBadge(kos.getStatus());
        
        infoBox.getChildren().addAll(locationLabel, nameLabel, detailsBox, roomsLabel, statusBadge);
        
        // Add rejection reason if exists
        if ("Ditolak".equals(kos.getStatus()) && kos.getAlasanPenolakan() != null) {
            VBox reasonBox = new VBox(5);
            reasonBox.setStyle("-fx-background-color: #ffebee; -fx-padding: 10; -fx-background-radius: 8;");
            reasonBox.setPadding(new Insets(10));
            
            Label reasonTitle = new Label("⚠️ Alasan Penolakan:");
            reasonTitle.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: #f44336;");
            
            Label reasonLabel = new Label(kos.getAlasanPenolakan());
            reasonLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #f44336;");
            reasonLabel.setWrapText(true);
            
            reasonBox.getChildren().addAll(reasonTitle, reasonLabel);
            infoBox.getChildren().add(reasonBox);
        }
        
        card.setOnMouseClicked(e -> showMyKosDetailDialog(kos));
        card.getChildren().addAll(imagePane, infoBox);
        
        return card;
    }
    
    private void addPlaceholderImage(StackPane imagePane) {
        imagePane.setStyle(imagePane.getStyle() + "-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        Label imageLabel = new Label("🏠");
        imageLabel.setStyle("-fx-font-size: 48; -fx-text-fill: white;");
        imagePane.getChildren().add(imageLabel);
    }
    
    private Label createStatusBadge(String status) {
        Label badge = new Label();
        badge.setPadding(new Insets(6, 12, 6, 12));
        badge.setStyle("-fx-border-radius: 20; -fx-background-radius: 20; -fx-font-size: 11; -fx-font-weight: bold;");
        
        switch (status) {
            case "Terverifikasi":
                badge.setText("✓ Terverifikasi");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #E8F5E9; -fx-text-fill: #4CAF50;");
                break;
            case "Menunggu Verifikasi":
                badge.setText("⏳ Menunggu Verifikasi");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #FFF3E0; -fx-text-fill: #FF9800;");
                break;
            case "Ditolak":
                badge.setText("✗ Ditolak");
                badge.setStyle(badge.getStyle() + "-fx-background-color: #FFEBEE; -fx-text-fill: #f44336;");
                break;
        }
        
        return badge;
    }
    
    private void showMyKosDetailDialog(Kos kos) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Detail Kos - " + kos.getNama());
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f5f5f5;");
        
        // Image Box
        VBox imageBox = new VBox();
        imageBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        
        if (kos.getFoto() != null && !kos.getFoto().isEmpty()) {
            try {
                File imgFile = new File(kos.getFoto());
                if (imgFile.exists()) {
                    Image img = new Image(imgFile.toURI().toString());
                    ImageView imageView = new ImageView(img);
                    imageView.setFitWidth(500);
                    imageView.setFitHeight(300);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageBox.getChildren().add(imageView);
                } else {
                    Label noImg = new Label("📷 Tidak ada foto");
                    noImg.setStyle("-fx-font-size: 24; -fx-text-fill: #999;");
                    imageBox.getChildren().add(noImg);
                }
            } catch (Exception e) {
                Label errorImg = new Label("⚠️ Error memuat foto");
                errorImg.setStyle("-fx-font-size: 18; -fx-text-fill: #f44336;");
                imageBox.getChildren().add(errorImg);
            }
        } else {
            Label noImg = new Label("📷 Tidak ada foto");
            noImg.setStyle("-fx-font-size: 24; -fx-text-fill: #999;");
            imageBox.getChildren().add(noImg);
        }
        imageBox.setAlignment(Pos.CENTER);
        
        // Info Box
        VBox infoBox = new VBox(12);
        infoBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        
        Label title = new Label(kos.getNama());
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        GridPane detailGrid = new GridPane();
        detailGrid.setHgap(20);
        detailGrid.setVgap(12);
        
        addDetailRow(detailGrid, 0, "Alamat", kos.getAlamat());
        addDetailRow(detailGrid, 1, "Deskripsi", kos.getDeskripsi() != null ? kos.getDeskripsi() : "-");
        addDetailRow(detailGrid, 2, "Harga", formatCurrency(kos.getHarga()) + "/bulan");
        addDetailRow(detailGrid, 3, "Tipe Kos", kos.getTipeKos());
        addDetailRow(detailGrid, 4, "Fasilitas", kos.getFasilitas() != null ? kos.getFasilitas() : "-");
        addDetailRow(detailGrid, 5, "Total Kamar", String.valueOf(kos.getTotalKamar()));
        addDetailRow(detailGrid, 6, "Kamar Tersedia", String.valueOf(kos.getKamarTersedia()));
        addDetailRow(detailGrid, 7, "Status", kos.getStatus());
        
        if ("Terverifikasi".equals(kos.getStatus())) {
            addDetailRow(detailGrid, 8, "Rating", String.format("%.1f ★", kos.getRating()));
        }
        
        if (kos.getAlasanPenolakan() != null && !kos.getAlasanPenolakan().isEmpty()) {
            Label reasonTitle = new Label("⚠️ Alasan Penolakan:");
            reasonTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #f44336; -fx-font-size: 14;");
            
            Text reasonText = new Text(kos.getAlasanPenolakan());
            reasonText.setStyle("-fx-fill: #f44336;");
            reasonText.setWrappingWidth(400);
            
            VBox reasonBox = new VBox(8, reasonTitle, reasonText);
            reasonBox.setStyle("-fx-background-color: #ffebee; -fx-padding: 15; -fx-background-radius: 8;");
            detailGrid.add(reasonBox, 0, 9, 2, 1);
        }
        
        infoBox.getChildren().addAll(title, new Separator(), detailGrid);
        
        // Button Box
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button btnEdit = new Button("✏️ Edit");
        btnEdit.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-cursor: hand;");
        btnEdit.setOnAction(e -> {
            dialog.close();
            showKosForm(kos);
        });
        
        Button btnDelete = new Button("🗑 Hapus");
        btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-cursor: hand;");
        btnDelete.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText("Hapus Kos");
            confirm.setContentText("Apakah Anda yakin ingin menghapus " + kos.getNama() + "?");
            
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (kosDAO.deleteKos(kos.getIdKos())) {
                    showAlert("Berhasil", "Kos berhasil dihapus!", Alert.AlertType.INFORMATION);
                    dialog.close();
                    loadData();
                }
            }
        });
        
        Button btnClose = new Button("Tutup");
        btnClose.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-cursor: hand;");
        btnClose.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(btnEdit, btnDelete, btnClose);
        content.getChildren().addAll(imageBox, infoBox, buttonBox);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5;");
        
        Scene scene = new Scene(scrollPane, 600, 750);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void showKosForm(Kos kos) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(kos == null ? "Tambah Kos Baru" : "Edit Kos");
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: white;");
        
        Label headerLabel = new Label(kos == null ? "Masukkan Data Kos Baru" : "Edit Data Kos");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        
        // Input Fields
        TextField txtNama = new TextField();
        txtNama.setPromptText("Nama Kos");
        txtNama.setPrefWidth(300);
        txtNama.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        TextField txtAlamat = new TextField();
        txtAlamat.setPromptText("Alamat");
        txtAlamat.setPrefWidth(300);
        txtAlamat.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        TextArea txtDeskripsi = new TextArea();
        txtDeskripsi.setPromptText("Deskripsi");
        txtDeskripsi.setPrefRowCount(3);
        txtDeskripsi.setPrefWidth(300);
        txtDeskripsi.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        TextField txtHarga = new TextField();
        txtHarga.setPromptText("Harga per bulan");
        txtHarga.setPrefWidth(300);
        txtHarga.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        ComboBox<String> cbTipe = new ComboBox<>();
        cbTipe.getItems().addAll("Putra", "Putri", "Campur");
        cbTipe.setValue("Putra");
        cbTipe.setPrefWidth(300);
        
        TextField txtFasilitas = new TextField();
        txtFasilitas.setPromptText("Fasilitas (pisahkan dengan koma)");
        txtFasilitas.setPrefWidth(300);
        txtFasilitas.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        TextField txtTotalKamar = new TextField();
        txtTotalKamar.setPromptText("Total Kamar");
        txtTotalKamar.setPrefWidth(300);
        txtTotalKamar.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        TextField txtKamarTersedia = new TextField();
        txtKamarTersedia.setPromptText("Kamar Tersedia");
        txtKamarTersedia.setPrefWidth(300);
        txtKamarTersedia.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        Label lblFoto = new Label("Belum ada foto");
        final String[] fotoPath = {null};
        Button btnUploadFoto = new Button("Upload Foto");
        btnUploadFoto.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand;");
        btnUploadFoto.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pilih Foto Kos");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File file = fileChooser.showOpenDialog(dialog);
            if (file != null) {
                fotoPath[0] = file.getAbsolutePath();
                lblFoto.setText(file.getName());
            }
        });
        
        // Populate fields if editing
        if (kos != null) {
            txtNama.setText(kos.getNama());
            txtAlamat.setText(kos.getAlamat());
            txtDeskripsi.setText(kos.getDeskripsi() != null ? kos.getDeskripsi() : "");
            txtHarga.setText(String.valueOf((int)kos.getHarga()));
            cbTipe.setValue(kos.getTipeKos());
            txtFasilitas.setText(kos.getFasilitas() != null ? kos.getFasilitas() : "");
            txtTotalKamar.setText(String.valueOf(kos.getTotalKamar()));
            txtKamarTersedia.setText(String.valueOf(kos.getKamarTersedia()));
            if (kos.getFoto() != null && !kos.getFoto().isEmpty()) {
                lblFoto.setText(kos.getFoto());
                fotoPath[0] = kos.getFoto();
            }
        }
        
        // Add to grid
        int row = 0;
        grid.add(new Label("Nama Kos:"), 0, row);
        grid.add(txtNama, 1, row++);
        grid.add(new Label("Alamat:"), 0, row);
        grid.add(txtAlamat, 1, row++);
        grid.add(new Label("Deskripsi:"), 0, row);
        grid.add(txtDeskripsi, 1, row++);
        grid.add(new Label("Harga/Bulan:"), 0, row);
        grid.add(txtHarga, 1, row++);
        grid.add(new Label("Tipe Kos:"), 0, row);
        grid.add(cbTipe, 1, row++);
        grid.add(new Label("Fasilitas:"), 0, row);
        grid.add(txtFasilitas, 1, row++);
        grid.add(new Label("Total Kamar:"), 0, row);
        grid.add(txtTotalKamar, 1, row++);
        grid.add(new Label("Kamar Tersedia:"), 0, row);
        grid.add(txtKamarTersedia, 1, row++);
        grid.add(new Label("Foto:"), 0, row);
        HBox hboxFoto = new HBox(10);
        hboxFoto.getChildren().addAll(btnUploadFoto, lblFoto);
        grid.add(hboxFoto, 1, row++);
        
        // Button Box
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button btnSave = new Button("Simpan");
        btnSave.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-cursor: hand;");
        btnSave.setOnAction(e -> {
            String nama = txtNama.getText().trim();
            String alamat = txtAlamat.getText().trim();
            String deskripsi = txtDeskripsi.getText().trim();
            String hargaStr = txtHarga.getText().trim();
            String tipeKos = cbTipe.getValue();
            String fasilitas = txtFasilitas.getText().trim();
            String totalKamarStr = txtTotalKamar.getText().trim();
            String kamarTersediaStr = txtKamarTersedia.getText().trim();
            
            if (nama.isEmpty() || alamat.isEmpty() || hargaStr.isEmpty() || totalKamarStr.isEmpty() || 
                kamarTersediaStr.isEmpty() || tipeKos == null || tipeKos.isEmpty()) {
                showAlert("Error", "Semua field wajib harus diisi!", Alert.AlertType.ERROR);
                return;
            }
            
            try {
                double harga = Double.parseDouble(hargaStr);
                int totalKamar = Integer.parseInt(totalKamarStr);
                int kamarTersedia = Integer.parseInt(kamarTersediaStr);
                
                if (harga <= 0 || totalKamar <= 0 || kamarTersedia < 0 || kamarTersedia > totalKamar || totalKamar > 1000) {
                    showAlert("Error", "Nilai tidak valid!", Alert.AlertType.ERROR);
                    return;
                }
                
                Kos newKos = kos == null ? new Kos() : kos;
                newKos.setIdPemilik(currentOwnerId);
                newKos.setNama(nama);
                newKos.setAlamat(alamat);
                newKos.setDeskripsi(!deskripsi.isEmpty() ? deskripsi : null);
                newKos.setHarga(harga);
                newKos.setTipeKos(tipeKos);
                newKos.setFasilitas(!fasilitas.isEmpty() ? fasilitas : null);
                newKos.setTotalKamar(totalKamar);
                newKos.setKamarTersedia(kamarTersedia);
                newKos.setKamarTerisi(totalKamar - kamarTersedia);
                newKos.setFoto(fotoPath[0]);
                newKos.setStatus("Menunggu Verifikasi");
                
                boolean success = kos == null ? kosDAO.insertKos(newKos) : kosDAO.updateKos(newKos);
                if (success) {
                    showAlert("Berhasil", kos == null ? "Kos berhasil ditambahkan!" : "Kos berhasil diupdate!", Alert.AlertType.INFORMATION);
                    dialog.close();
                    loadData();
                } else {
                    showAlert("Error", "Gagal menyimpan data!", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Format angka tidak valid!", Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert("Error", "Terjadi kesalahan: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });
        
        Button btnCancel = new Button("Batal");
        btnCancel.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14; -fx-cursor: hand;");
        btnCancel.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(btnSave, btnCancel);
        
        mainContainer.getChildren().addAll(headerLabel, grid, buttonBox);
        
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        
        Scene scene = new Scene(scrollPane, 500, 650);
        dialog.setScene(scene);
        dialog.show();
        
        javafx.application.Platform.runLater(() -> txtNama.requestFocus());
    }
    
    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label lblLabel = new Label(label + ":");
        lblLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #666;");
        
        Text txtValue = new Text(value);
        txtValue.setStyle("-fx-fill: #333;");
        txtValue.setWrappingWidth(350);
        
        grid.add(lblLabel, 0, row);
        grid.add(txtValue, 1, row);
    }
    
    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);
        String formatted = formatter.format(amount);
        return formatted.replace(",00", "");
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // FXML Event Handlers
    @FXML
    private void handleCloseNotification() {
        notificationBox.setVisible(false);
        notificationBox.setManaged(false);
    }
    
    @FXML
    private void handleTambahKos() {
        showKosForm(null);
    }
    
    @FXML
    private void handleHapusKos() {
        showAlert("Info", "Klik card kos untuk menghapus!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void handleTampilkanLaporan() {
        System.out.println("\n=== Handle Tampilkan Laporan ===");
        
        String periode = cbPeriode.getValue();
        String filterKos = cbFilterKos.getValue();
        
        System.out.println("Periode: " + periode);
        System.out.println("Filter Kos: " + filterKos);
        System.out.println("Owner ID: " + currentOwnerId);
        
        if (periode == null || filterKos == null) {
            showAlert("Peringatan", "Pilih periode dan kos terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        List<Laporan> laporanData = laporanDAO.getLaporanByPeriode(currentOwnerId, periode, filterKos);
        System.out.println("📊 Data Found: " + laporanData.size() + " records");
        
        ObservableList<Laporan> laporanList = FXCollections.observableArrayList(laporanData);
        tblLaporan.setItems(laporanList);
        
        double totalPemasukan = laporanList.stream().mapToDouble(Laporan::getPemasukan).sum();
        double totalPengeluaran = laporanList.stream().mapToDouble(Laporan::getPengeluaran).sum();
        double keuntungan = totalPemasukan - totalPengeluaran;
        
        lblTotalPemasukan.setText(formatCurrency(totalPemasukan));
        lblTotalPengeluaran.setText(formatCurrency(totalPengeluaran));
        lblKeuntungan.setText(formatCurrency(keuntungan));
        
        System.out.println("💰 Total Pemasukan: " + totalPemasukan);
        System.out.println("💸 Total Pengeluaran: " + totalPengeluaran);
        System.out.println("✅ Laporan displayed");
    }
    
    @FXML
    private void handleExportPDF() {
        ObservableList<Laporan> laporanList = tblLaporan.getItems();
        if (laporanList == null || laporanList.isEmpty()) {
            showAlert("Peringatan", "Tidak ada data untuk di-export!", Alert.AlertType.WARNING);
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Laporan ke PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Laporan_Keuangan_" + System.currentTimeMillis() + ".pdf");
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
                Paragraph title = new Paragraph("LAPORAN KEUANGAN KOS", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);
                
                com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
                double totalPemasukan = laporanList.stream().mapToDouble(Laporan::getPemasukan).sum();
                double totalPengeluaran = laporanList.stream().mapToDouble(Laporan::getPengeluaran).sum();
                double keuntungan = totalPemasukan - totalPengeluaran;
                
                Paragraph summary = new Paragraph();
                summary.add(new Chunk("Total Pemasukan: " + formatCurrency(totalPemasukan) + "\n", boldFont));
                summary.add(new Chunk("Total Pengeluaran: " + formatCurrency(totalPengeluaran) + "\n", boldFont));
                summary.add(new Chunk("Keuntungan Bersih: " + formatCurrency(keuntungan) + "\n", boldFont));
                summary.setSpacingAfter(20);
                document.add(summary);
                
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                
                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
                addTableHeader(table, "Tanggal", headerFont);
                addTableHeader(table, "Kos", headerFont);
                addTableHeader(table, "Kategori", headerFont);
                addTableHeader(table, "Keterangan", headerFont);
                addTableHeader(table, "Pemasukan", headerFont);
                addTableHeader(table, "Pengeluaran", headerFont);
                
                com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 9);
                for (Laporan lap : laporanList) {
                    table.addCell(new Phrase(lap.getTanggal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dataFont));
                    table.addCell(new Phrase(lap.getNamaKos(), dataFont));
                    table.addCell(new Phrase(lap.getKategori(), dataFont));
                    table.addCell(new Phrase(lap.getKeterangan(), dataFont));
                    table.addCell(new Phrase(formatCurrency(lap.getPemasukan()), dataFont));
                    table.addCell(new Phrase(formatCurrency(lap.getPengeluaran()), dataFont));
                }
                
                document.add(table);
                document.close();
                
                showAlert("Berhasil", "Laporan berhasil di-export ke PDF!", Alert.AlertType.INFORMATION);
                
            } catch (Exception e) {
                showAlert("Error", "Gagal export ke PDF: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }
    
    private void addTableHeader(PdfPTable table, String text, com.itextpdf.text.Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }
    
    @FXML
    private void handleExportExcel() {
        ObservableList<Laporan> laporanList = tblLaporan.getItems();
        if (laporanList == null || laporanList.isEmpty()) {
            showAlert("Peringatan", "Tidak ada data untuk di-export!", Alert.AlertType.WARNING);
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Laporan ke Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("Laporan_Keuangan_" + System.currentTimeMillis() + ".xlsx");
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Laporan Keuangan");
                
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                
                Row headerRow = sheet.createRow(0);
                String[] columns = {"Tanggal", "Kos", "Kategori", "Keterangan", "Pemasukan", "Pengeluaran"};
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                int rowNum = 1;
                for (Laporan lap : laporanList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(lap.getTanggal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    row.createCell(1).setCellValue(lap.getNamaKos());
                    row.createCell(2).setCellValue(lap.getKategori());
                    row.createCell(3).setCellValue(lap.getKeterangan());
                    row.createCell(4).setCellValue(lap.getPemasukan());
                    row.createCell(5).setCellValue(lap.getPengeluaran());
                }
                
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                rowNum++;
                Row summaryRow = sheet.createRow(rowNum++);
                Cell summaryCell = summaryRow.createCell(0);
                summaryCell.setCellValue("RINGKASAN");
                summaryCell.setCellStyle(headerStyle);
                
                double totalPemasukan = laporanList.stream().mapToDouble(Laporan::getPemasukan).sum();
                double totalPengeluaran = laporanList.stream().mapToDouble(Laporan::getPengeluaran).sum();
                double keuntungan = totalPemasukan - totalPengeluaran;
                
                Row totalPemasukanRow = sheet.createRow(rowNum++);
                totalPemasukanRow.createCell(0).setCellValue("Total Pemasukan:");
                totalPemasukanRow.createCell(1).setCellValue(totalPemasukan);
                
                Row totalPengeluaranRow = sheet.createRow(rowNum++);
                totalPengeluaranRow.createCell(0).setCellValue("Total Pengeluaran:");
                totalPengeluaranRow.createCell(1).setCellValue(totalPengeluaran);
                
                Row keuntunganRow = sheet.createRow(rowNum++);
                Cell keuntunganLabelCell = keuntunganRow.createCell(0);
                keuntunganLabelCell.setCellValue("Keuntungan Bersih:");
                keuntunganLabelCell.setCellStyle(headerStyle);
                Cell keuntunganValueCell = keuntunganRow.createCell(1);
                keuntunganValueCell.setCellValue(keuntungan);
                keuntunganValueCell.setCellStyle(headerStyle);
                
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
                
                showAlert("Berhasil", "Laporan berhasil di-export ke Excel!", Alert.AlertType.INFORMATION);
                
            } catch (Exception e) {
                showAlert("Error", "Gagal export ke Excel: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }
    
        @FXML
        private void goToDashboard(MouseEvent event) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}