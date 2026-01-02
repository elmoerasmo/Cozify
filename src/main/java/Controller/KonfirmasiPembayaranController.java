package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Pembayaran;

public class KonfirmasiPembayaranController {
    
    private static final String rupiah = "Rp %,.0f";
    
    @FXML private TextField tfKodeReservasi;
    @FXML private TextField tfnamaKos;
    @FXML private TextField tfnamaPenyewa;
    @FXML private TextField tftotalHarga;
    @FXML private TextField tfmetodePembayaran;
    @FXML private Button btYaKonfirm;
    @FXML private Button btTidakKonfirm;
    
    private PaymentViewController paymentViewController;
    private Pembayaran pembayaran;
    private boolean confirmed = false;
    
    public void initialize() {
        btYaKonfirm.setOnAction(e -> handleConfirm());
        btTidakKonfirm.setOnAction(e -> handleCancel());
    }
    
    public void setPaymentViewController(PaymentViewController controller) {
        this.paymentViewController = controller;
    }
    
    public void setPembayaranData(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
        displayData();
    }
    
    private void displayData() {
        if (pembayaran != null && pembayaran.getKos() != null && pembayaran.getUser() != null) {
            tfKodeReservasi.setText(String.valueOf(pembayaran.getKos().getIdKos())); // ID kos dijadikan kode reservasi
            tfnamaKos.setText(pembayaran.getKos().getNama());
            tfnamaPenyewa.setText(pembayaran.getUser().getNama());
            tftotalHarga.setText(String.format(rupiah, pembayaran.getTotalPembayaran()));
            tfmetodePembayaran.setText(pembayaran.getMetodePembayaran());
        }
    }
    
    private void handleConfirm() {
        confirmed = true;
        if (pembayaran != null) {
            pembayaran.setStatus("Berhasil");
        }
        closeWindow();
        
        if (paymentViewController != null) {
            paymentViewController.showPembayaranBerhasil();
        }
    }
    
    private void handleCancel() {
        confirmed = false;
        closeWindow();
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    private void closeWindow() {
        Stage stage = (Stage) btYaKonfirm.getScene().getWindow();
        stage.close();
    }
}
