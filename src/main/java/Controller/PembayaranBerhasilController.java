package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Pembayaran;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class PembayaranBerhasilController {
    
    private static final String RUPIAH_FORMAT = "Rp %,.0f";
    
    @FXML private TextField tfIDReservasi, tfNamaKos, tfAlamatKos, tfNamaPenyewa, tfTotalHarga, tfMetodePembayaran;
    @FXML private Label lbTipeKos, lbNoKamar, lbProgress1, lbProgress2, lbProgress3;
    @FXML private Button btPembayaranBerhasil;
   
    private Pembayaran pembayaran;
   
    @FXML
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
        
        tfIDReservasi.setText("RSV-" + pembayaran.getIdPembayaran());
        
        if (pembayaran.getKos() != null) {
            tfNamaKos.setText(pembayaran.getKos().getNama());
            tfAlamatKos.setText(pembayaran.getKos().getAlamat());
            lbTipeKos.setText("Tipe: " + pembayaran.getKos().getTipeKos());
            lbNoKamar.setText("Kamar No: " + pembayaran.getKos().getIdKos());
        }
        
        if (pembayaran.getUser() != null) {
            tfNamaPenyewa.setText(pembayaran.getUser().getNama());
        }
        
        tfTotalHarga.setText(String.format(RUPIAH_FORMAT, pembayaran.getTotalPembayaran()));
        tfMetodePembayaran.setText(pembayaran.getMetodePembayaran());
    }
    
    private void updateProgressIndicator() {
        if (lbProgress1 != null) lbProgress1.setOpacity(1.0);
        if (lbProgress2 != null) lbProgress2.setOpacity(1.0);
        if (lbProgress3 != null) {
            lbProgress3.setOpacity(1.0);
            lbProgress3.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-background-radius: 100;");
        }
    }
    
    private void handleSelesai() {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Dashboard.fxml"));
        Stage stage = (Stage) btPembayaranBerhasil.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Cozify - Dashboard");
    } catch (IOException e) {
        System.err.println("Gagal balik ke Dashboard: " + e.getMessage());
        ((Stage) btPembayaranBerhasil.getScene().getWindow()).close();
    }
}
}