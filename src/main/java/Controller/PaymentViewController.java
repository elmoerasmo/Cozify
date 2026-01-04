package Controller;

import DAO.KosDAO;
import DAO.KamarDAO; 
import Model.Kos;
import Model.Kamar; 
import Helper.PembayaranHelper;
import Model.Pembayaran;
import Model.Session;
import Model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.time.LocalDate;

public class PaymentViewController {
    @FXML private BorderPane mainContainer; 
    @FXML private TextField lblNamaKos, tfAlamatKos, tfHargaPerBulan, tfDurasiSewa, tfTotalPembayaran, tfIDReservasi;
    @FXML private Label lbTipeKos;
    @FXML private ComboBox<Kamar> cbPilihKamar; 
    
    private Pembayaran pembayaranSelesai;
    private Kos selectedKos;
    private String selectedMethod = "Belum Dipilih";

    public void setOrderData(Kos kos) {
        this.selectedKos = kos;
        lblNamaKos.setText(kos.getNama());
        tfAlamatKos.setText(kos.getAlamat());
        lbTipeKos.setText(kos.getTipeKos());
        tfHargaPerBulan.setText(String.valueOf((int)kos.getHarga()));
        tfIDReservasi.setText("RSV-PROSES");
        
        KamarDAO kamarDAO = new KamarDAO();
        cbPilihKamar.setItems(FXCollections.observableArrayList(kamarDAO.getKamarTersediaByKos(kos.getIdKos())));
        
        cbPilihKamar.setConverter(new StringConverter<Kamar>() {
            @Override
            public String toString(Kamar k) { return (k == null) ? "" : "Kamar No: " + k.getNomorKamar(); }
            @Override
            public Kamar fromString(String string) { return null; }
        });

        tfDurasiSewa.setText("1");
        kalkulasiTotal();
        
        tfDurasiSewa.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfDurasiSewa.setText(newValue.replaceAll("[^\\d]", ""));
            }
            kalkulasiTotal();
        });
    }

    private void kalkulasiTotal() {
        try {
            int bulan = Integer.parseInt(tfDurasiSewa.getText());
            if (bulan <= 0) bulan = 1;
            double total = selectedKos.getHarga() * bulan;
            tfTotalPembayaran.setText(formatRupiah(total));
        } catch (Exception e) {
            tfTotalPembayaran.setText(formatRupiah(0));
        }
    }

    private String formatRupiah(double val) {
        return "Rp " + String.format("%,.0f", val);
    }

    @FXML private void handleSelectBank() { 
        this.selectedMethod = "Transfer Bank";
        openInstruction("/View/TransferBank.fxml", "Virtual Account", PembayaranHelper.generateVA(Session.getUser().getNoTelepon()));
    }

    @FXML private void handleSelectEmoney() { 
        this.selectedMethod = "E-Money";
        openInstruction("/View/emoney.fxml", "Instruksi E-Money", Helper.PembayaranHelper.generateEMoneyCode());
    }
    
    @FXML private void handleSelectQRIS() { 
        this.selectedMethod = "QRIS";
        try {
            Helper.PembayaranHelper.generateQRCode(Helper.PembayaranHelper.generateRandomQRContent(), 300, 300, "src/main/resources/images/temp_qr.png");
            openInstruction("/View/Qris.fxml", "Scan QRIS Pembayaran", null);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void openInstruction(String fxmlPath, String title, String code) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            double total = selectedKos.getHarga() * Integer.parseInt(tfDurasiSewa.getText());
            Object ctrl = loader.getController();
            if (ctrl instanceof TransferBankController) ((TransferBankController) ctrl).setData(total, code);
            else if (ctrl instanceof EmoneyController) ((EmoneyController) ctrl).setData(total, code);
            else if (ctrl instanceof QrisController) ((QrisController) ctrl).setData(total);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleKonfirmasiBayar() {
        Kamar kamarTerpilih = cbPilihKamar.getSelectionModel().getSelectedItem();

        if (selectedKos == null || selectedMethod.equals("Belum Dipilih") || tfDurasiSewa.getText().isEmpty() || kamarTerpilih == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Harap pilih nomor KAMAR dan METODE PEMBAYARAN!");
            alert.show();
            return;
        }

        Pembayaran bayar = new Pembayaran();
        bayar.setUser(Session.getUser());
        bayar.setKos(selectedKos);
        bayar.setKamar(kamarTerpilih); 
        bayar.setDurasiBulan(Integer.parseInt(tfDurasiSewa.getText()));
        bayar.setTotalPembayaran(selectedKos.getHarga() * bayar.getDurasiBulan());
        bayar.setMetodePembayaran(selectedMethod);
        bayar.setStatus("BERHASIL");
        bayar.setTanggalTransaksi(LocalDate.now());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/konfirmasiPembayaran.fxml"));
            Parent root = loader.load();
            KonfirmasiPembayaranController ctrl = loader.getController();
            ctrl.setKonfirmasiData(bayar, this); 

            Stage stage = new Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    public void setPembayaranUntukBerhasil(Pembayaran p) { this.pembayaranSelesai = p; }

    public void showPembayaranBerhasil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/pembayaranBerhasil.fxml"));
            Parent root = loader.load();
            PembayaranBerhasilController successCtrl = loader.getController();
            successCtrl.setPembayaran(pembayaranSelesai); 
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}