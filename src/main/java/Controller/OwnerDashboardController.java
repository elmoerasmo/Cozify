package Controller;

import DAO.KamarDAO;
import DAO.KosDAO;
import DAO.LaporanDAO;
import Model.Kos;
import Model.Laporan;
import javafx.application.Platform;
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
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;

public class OwnerDashboardController implements Initializable {
    
    @FXML private VBox notificationBox;
    @FXML private Label lblNotificationMessage, lblTotalKos, lblTotalKamar, lblKamarTerisi, lblPendapatan;
    @FXML private FlowPane myKosCardsContainer;
    @FXML private ComboBox<String> cbPeriode, cbFilterKos;
    @FXML private Label lblTotalPemasukan, lblTotalPengeluaran, lblKeuntungan;
    @FXML private TableView<Laporan> tblLaporan;
    @FXML private TableColumn<Laporan, String> colTanggal, colKosLaporan, colKategori, colKeterangan;
    @FXML private TableColumn<Laporan, Double> colPemasukan, colPengeluaran;
    @FXML private Button profileButton;    

    private KosDAO kosDAO;
    private LaporanDAO laporanDAO;
    private int currentOwnerId = Model.Session.getUser().getId();
    private NumberFormat currencyFormat;
    public static Stage ownerStage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kosDAO = new KosDAO();
        laporanDAO = new LaporanDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        setupTables();
        setupComboBoxes();
        loadData();
        profileButton.setOnAction(e -> openProfile());
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
        if (cbPeriode != null) cbPeriode.setValue("Bulan Ini"); 
        setupKosFilterComboBox();
    }
    
    private void setupKosFilterComboBox() {
        if (cbFilterKos != null) {
            ObservableList<String> items = FXCollections.observableArrayList("Semua Kos");
            items.addAll(kosDAO.getKosNamesByOwner(currentOwnerId));
            cbFilterKos.setItems(items);
            cbFilterKos.setValue("Semua Kos");
        }
    }
    
    private void loadData() {
        lblTotalKos.setText(String.valueOf(kosDAO.getTotalKosByOwner(currentOwnerId)));
        lblTotalKamar.setText(String.valueOf(kosDAO.getTotalKamarByOwner(currentOwnerId)));
        lblKamarTerisi.setText(String.valueOf(kosDAO.getKamarTerisiByOwner(currentOwnerId)));
        lblPendapatan.setText(formatCurrency(laporanDAO.getPendapatanBulanIni(currentOwnerId)));
        
        if (kosDAO.hasRejectedKosByOwner(currentOwnerId)) {
            notificationBox.setVisible(true);
            notificationBox.setManaged(true);
            lblNotificationMessage.setText("Anda memiliki kos yang ditolak admin.");
        }
        loadMyKosCards();
    }
    
    private void loadMyKosCards() {
        myKosCardsContainer.getChildren().clear();
        kosDAO.getKosByOwner(currentOwnerId).forEach(kos -> myKosCardsContainer.getChildren().add(createMyKosCard(kos)));
    }
    
    private VBox createMyKosCard(Kos kos) {
        VBox card = new VBox(0);
        card.setPrefWidth(340);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3); -fx-cursor: hand;");
        
        StackPane imagePane = new StackPane();
        imagePane.setPrefHeight(200);
        
        try {
            InputStream is = getClass().getResourceAsStream("/images/" + kos.getFoto());
            if (is != null) {
                ImageView iv = new ImageView(new Image(is));
                iv.setFitWidth(340); iv.setFitHeight(200); iv.setPreserveRatio(false);
                imagePane.getChildren().add(iv);
            } else {
                addPlaceholderImage(imagePane);
            }
        } catch (Exception e) {
            addPlaceholderImage(imagePane);
        }
        
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(15));
        Label name = new Label(kos.getNama());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        Label price = new Label(formatCurrency(kos.getHarga()) + "/bulan");
        price.setStyle("-fx-text-fill: #2196F3;");
        
        infoBox.getChildren().addAll(name, price, createStatusBadge(kos.getStatus()));
        card.getChildren().addAll(imagePane, infoBox);
        card.setOnMouseClicked(e -> showMyKosDetailDialog(kos));
        return card;
    }


    private void showMyKosDetailDialog(Kos kos) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Detail Kos - " + kos.getNama());

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        ImageView iv = new ImageView();
        try {
            InputStream is = getClass().getResourceAsStream("/images/" + kos.getFoto());
            if (is != null) iv.setImage(new Image(is));
            else iv.setImage(new Image(getClass().getResourceAsStream("/images/placeholder.png")));
        } catch (Exception e) { e.printStackTrace(); }
        iv.setFitWidth(440); iv.setPreserveRatio(true);

        VBox fListContainer = new VBox(8);
        fListContainer.getChildren().add(new Label("Fasilitas:"));
        if (kos.getFasilitas() != null && !kos.getFasilitas().isEmpty()) {
            for (String f : kos.getFasilitas().split(", ")) {
                fListContainer.getChildren().add(new Label("• " + f));
            }
        }

        HBox btnBox = new HBox(15);
        btnBox.setAlignment(Pos.CENTER);
        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> { dialog.close(); showKosForm(kos); });
        Button btnDel = new Button("Hapus");
        btnDel.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnDel.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setContentText("Yakin mau hapus kos " + kos.getNama() + "?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (kosDAO.deleteKos(kos.getIdKos())) { dialog.close(); loadData(); }
            }
        });
        btnBox.getChildren().addAll(btnEdit, btnDel);

        content.getChildren().addAll(iv, new Label(kos.getNama()), new Separator(), fListContainer, new Separator(), btnBox);
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        dialog.setScene(new Scene(sp, 500, 650));
        dialog.showAndWait();
    }

    private void showKosForm(Kos kos) {
        Platform.runLater(() -> {
            try {
                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setTitle(kos == null ? "Tambah Kos Baru" : "Edit Data Kos");

                VBox form = new VBox(12);
                form.setPadding(new Insets(20));
                form.setStyle("-fx-background-color: white;");

                TextField txtNama = new TextField(kos != null ? kos.getNama() : "");
                TextField txtAlamat = new TextField(kos != null ? kos.getAlamat() : "");
                TextArea txtDesk = new TextArea(kos != null ? kos.getDeskripsi() : "");
                txtDesk.setPrefRowCount(3); txtDesk.setWrapText(true);
                TextField txtHarga = new TextField(kos != null ? String.valueOf((int)kos.getHarga()) : "");
                TextField txtKamar = new TextField(kos != null ? String.valueOf(kos.getTotalKamar()) : "");
                if(kos != null) txtKamar.setDisable(true);

                ComboBox<String> cbTipe = new ComboBox<>(FXCollections.observableArrayList("Putra", "Putri", "Campur"));
                cbTipe.setValue(kos != null ? kos.getTipeKos() : "Campur");

                final String[] pathDB = { (kos != null) ? kos.getFoto() : "" };
                Label lblFInfo = new Label(pathDB[0].isEmpty() ? "Belum ada foto" : pathDB[0]);
                Button btnPilihFoto = new Button("Pilih Foto Kos");
                btnPilihFoto.setOnAction(e -> {
                    FileChooser fc = new FileChooser();
                    File f = fc.showOpenDialog(dialog);
                    if (f != null) {
                        saveImageToResources(f);
                        pathDB[0] = f.getName();
                        lblFInfo.setText("Terpilih: " + pathDB[0]);
                    }
                });

                GridPane gridF = new GridPane(); gridF.setHgap(15); gridF.setVgap(10);
                CheckBox c1 = new CheckBox("Kamar Mandi Dalam"); CheckBox c2 = new CheckBox("Kloset Duduk");
                CheckBox c3 = new CheckBox("Kloset Jongkok"); CheckBox c4 = new CheckBox("Kamar Mandi Luar");
                CheckBox c5 = new CheckBox("Air panas"); CheckBox c6 = new CheckBox("Kasur");
                CheckBox c7 = new CheckBox("Meja Belajar"); CheckBox c8 = new CheckBox("TV");
                CheckBox c9 = new CheckBox("Lemari / Storage"); CheckBox c10 = new CheckBox("AC");
                gridF.add(c1,0,0); gridF.add(c2,1,0); gridF.add(c3,0,1); gridF.add(c4,1,1);
                gridF.add(c5,0,2); gridF.add(c6,1,2); gridF.add(c7,0,3); gridF.add(c8,1,3);
                gridF.add(c9,0,4); gridF.add(c10,1,4);

                if (kos != null && kos.getFasilitas() != null) {
                    String f = kos.getFasilitas();
                    c1.setSelected(f.contains("Kamar Mandi Dalam")); c2.setSelected(f.contains("Kloset Duduk"));
                    c3.setSelected(f.contains("Kloset Jongkok")); c4.setSelected(f.contains("Kamar Mandi Luar"));
                    c5.setSelected(f.contains("Air panas")); c6.setSelected(f.contains("Kasur"));
                    c7.setSelected(f.contains("Meja Belajar")); c8.setSelected(f.contains("TV"));
                    c9.setSelected(f.contains("Lemari / Storage")); c10.setSelected(f.contains("AC"));
                }

                Button btnSave = new Button(kos == null ? "TAMBAH KOS" : "UPDATE DATA");
                btnSave.setMaxWidth(Double.MAX_VALUE);
                btnSave.setPrefHeight(40);
                btnSave.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

                btnSave.setOnAction(e -> {
                    try {
                        if(txtNama.getText().isEmpty() || txtHarga.getText().isEmpty()) {
                            showAlert("Peringatan", "Nama dan Harga wajib diisi!", Alert.AlertType.WARNING);
                            return;
                        }

                        List<String> fl = new ArrayList<>();
                        if(c1.isSelected()) fl.add("Kamar Mandi Dalam"); if(c2.isSelected()) fl.add("Kloset Duduk");
                        if(c3.isSelected()) fl.add("Kloset Jongkok"); if(c4.isSelected()) fl.add("Kamar Mandi Luar");
                        if(c5.isSelected()) fl.add("Air panas"); if(c6.isSelected()) fl.add("Kasur");
                        if(c7.isSelected()) fl.add("Meja Belajar"); if(c8.isSelected()) fl.add("TV");
                        if(c9.isSelected()) fl.add("Lemari / Storage"); if(c10.isSelected()) fl.add("AC");

                        Kos n = (kos == null) ? new Kos() : kos;
                        n.setIdPemilik(currentOwnerId);
                        n.setNama(txtNama.getText());
                        n.setAlamat(txtAlamat.getText());
                        n.setDeskripsi(txtDesk.getText());
                        n.setHarga(Double.parseDouble(txtHarga.getText().replaceAll("[^0-9]", "")));
                        n.setTipeKos(cbTipe.getValue());
                        n.setFoto(pathDB[0]);
                        n.setFasilitas(String.join(", ", fl));

                        if (kos == null) {
                            int total = Integer.parseInt(txtKamar.getText());
                            n.setTotalKamar(total); n.setKamarTersedia(total);
                            int idGenerated = kosDAO.insertKosAndGetId(n);
                            if (idGenerated != -1) new KamarDAO().batchInsertKamar(idGenerated, total);
                        } else {
                            kosDAO.updateKos(n);
                        }
                        dialog.close(); loadData();
                    } catch (Exception ex) {
                        showAlert("Error", "Input tidak valid!", Alert.AlertType.ERROR);
                    }
                });

                form.getChildren().addAll(new Label("Nama Kos"), txtNama, new Label("Alamat"), txtAlamat, new Label("Deskripsi"), txtDesk,
                        new Label("Harga"), txtHarga, new Label("Tipe"), cbTipe, new Label("Total Kamar"), txtKamar,
                        new Label("Foto"), new HBox(10, btnPilihFoto, lblFInfo), new Label("Fasilitas"), gridF, btnSave);

                ScrollPane sp = new ScrollPane(form);
                sp.setFitToWidth(true); sp.setPrefHeight(650);
                dialog.setScene(new Scene(sp, 500, 700));
                dialog.show();

            } catch (Exception ex) { ex.printStackTrace(); }
        });
    }


    private void saveImageToResources(File sourceFile) {
        try {
            Path targetDir = Paths.get("src/main/resources/images");
            if (!Files.exists(targetDir)) Files.createDirectories(targetDir);
            Files.copy(sourceFile.toPath(), targetDir.resolve(sourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addPlaceholderImage(StackPane imagePane) {
        imagePane.setStyle("-fx-background-color: #ddd;");
        imagePane.getChildren().add(new Label("🏠"));
    }

    private String formatCurrency(double amount) {
        return currencyFormat.format(amount).replace(",00", "");
    }

    private Label createStatusBadge(String status) {
        Label badge = new Label(status == null ? "Pending" : status);
        badge.setStyle("-fx-padding: 5 10; -fx-background-radius: 10; -fx-background-color: #eee;");
        return badge;
    }


    @FXML private void handleTambahKos() { showKosForm(null); }
    
    @FXML private void handleCloseNotification() { notificationBox.setVisible(false); notificationBox.setManaged(false); }
    
    @FXML
    private void handleHapusKos() {
        showAlert("Informasi", "Untuk menghapus kos, silakan klik pada Kartu (Card) kos yang ingin dihapus, lalu klik tombol 'Hapus' di jendela detail.", Alert.AlertType.INFORMATION);
    }
    
    @FXML 
    private void handleTampilkanLaporan() {
        List<Laporan> data = laporanDAO.getLaporanByPeriode(currentOwnerId, cbPeriode.getValue(), cbFilterKos.getValue());
        tblLaporan.setItems(FXCollections.observableArrayList(data));
        double in = data.stream().mapToDouble(Laporan::getPemasukan).sum();
        double out = data.stream().mapToDouble(Laporan::getPengeluaran).sum();
        lblTotalPemasukan.setText(formatCurrency(in));
        lblTotalPengeluaran.setText(formatCurrency(out));
        lblKeuntungan.setText(formatCurrency(in - out));
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
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
                Paragraph title = new Paragraph("LAPORAN KEUANGAN KOS", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);
                
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                String[] headers = {"Tanggal", "Kos", "Kategori", "Keterangan", "Pemasukan", "Pengeluaran"};
                for (String h : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(h));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);
                }
                
                for (Laporan lap : laporanList) {
                    table.addCell(lap.getTanggal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    table.addCell(lap.getNamaKos());
                    table.addCell(lap.getKategori());
                    table.addCell(lap.getKeterangan());
                    table.addCell(formatCurrency(lap.getPemasukan()));
                    table.addCell(formatCurrency(lap.getPengeluaran()));
                }
                
                document.add(table);
                document.close();
                showAlert("Berhasil", "Laporan berhasil di-export!", Alert.AlertType.INFORMATION);
            } catch (Exception e) { e.printStackTrace(); }
        }
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
            Parent root = FXMLLoader.load(getClass().getResource("/View/Dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
    @FXML
    private void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Profile.fxml"));
            Parent root = loader.load();
            ProfileController pc = loader.getController();
            pc.setSource("OWNER_DASHBOARD");
            Stage s = new Stage();
            s.initStyle(StageStyle.UNDECORATED);
            s.setScene(new Scene(root));
            s.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void setStage(Stage stage) { 
        ownerStage = stage; 
    }
}