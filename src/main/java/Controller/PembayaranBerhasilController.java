package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Pembayaran;

public class PembayaranBerhasilController {
    
    private static final String rupiah = "Rp %,.0f";
    
    @FXML private TextField tfIDReservasi;
    @FXML private TextField tfNamaKos;
    @FXML private TextField tfAlamatKos;
    @FXML private Label lbTipeKos;
    @FXML private Label lbNoKamar;
    @FXML private TextField tfNamaPenyewa;
    @FXML private TextField tfTotalHarga;
    @FXML private TextField tfMetodePembayaran;
    @FXML private Button btPembayaranBerhasil;
    @FXML private Label lbProgress1;
    @FXML private Label lbProgress2;
    @FXML private Label lbProgress3;
   
    private Pembayaran pembayaran;
   
    public void initialize() {
        btPembayaranBerhasil.setOnAction(e -> handleSelesai());
        updateProgressIndicator();
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
        displayData();
    }

    private void displayData() {
        if (pembayaran == null) return;
        
        // ID Reservasi bisa pakai ID Kos
        tfIDReservasi.setText(pembayaran.getKos() != null ? String.valueOf(pembayaran.getKos().getIdKos()) : "-");
        tfNamaKos.setText(pembayaran.getKos() != null ? pembayaran.getKos().getNama() : "-");
        tfAlamatKos.setText(pembayaran.getKos() != null ? pembayaran.getKos().getAlamat() : "-");
        lbTipeKos.setText("Tipe: " + (pembayaran.getKos() != null ? pembayaran.getKos().getTipeKos() : "-"));
        lbNoKamar.setText("Kamar No. " + (pembayaran.getKos() != null ? pembayaran.getKos().getKamarTersedia() : "-"));
        
        tfNamaPenyewa.setText(pembayaran.getUser() != null ? pembayaran.getUser().getNama() : "-");
        tfTotalHarga.setText(String.format(rupiah, pembayaran.getTotalPembayaran()));
        tfMetodePembayaran.setText(pembayaran.getMetodePembayaran() != null ? pembayaran.getMetodePembayaran() : "-");
    }
    
    private void updateProgressIndicator() {
        if (lbProgress1 != null) lbProgress1.setOpacity(1.0);
        if (lbProgress2 != null) lbProgress2.setOpacity(1.0);
        if (lbProgress3 != null) lbProgress3.setOpacity(1.0);
    }
    
    private void handleSelesai() {
        System.out.println("Pembayaran selesai");
        closeWindow();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) btPembayaranBerhasil.getScene().getWindow();
        stage.close();
    }
}
