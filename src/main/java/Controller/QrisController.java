package Controller;

import Model.Pembayaran;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;

public class QrisController {
    
    private static final String RUPIAH_FORMAT = "Rp %,.0f";
    @FXML private ImageView ivQris;
    @FXML private TextField tfTotalPembayaran;
    @FXML private Button btKonfirmasiPembayaran;

    private PaymentViewController paymentViewController;
    private Pembayaran pembayaran;

    public void initialize() {
        btKonfirmasiPembayaran.setOnAction(e -> {
            Stage stage = (Stage) btKonfirmasiPembayaran.getScene().getWindow();
            stage.close();
        });
        
        File file = new File("@/images/temp_qr.png");
        if (file.exists()) {
            ivQris.setImage(new Image(file.toURI().toString() + "?t=" + System.currentTimeMillis()));
        }
    }

    public void setPaymentViewController(PaymentViewController controller) {
        this.paymentViewController = controller;
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
        tfTotalPembayaran.setText(String.format("Rp %,.0f", pembayaran.getTotalPembayaran()));
    }
    
    public void setData(double total) {
        this.tfTotalPembayaran.setText(String.format(RUPIAH_FORMAT, total));
    }

    private void konfirmasi() {
        Stage stage = (Stage) btKonfirmasiPembayaran.getScene().getWindow();
        stage.close();

        if (pembayaran != null) {
            pembayaran.setStatus("Berhasil");
        }

        if (paymentViewController != null) {
            paymentViewController.showPembayaranBerhasil();
        }
    }
}
