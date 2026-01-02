package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.Pembayaran;

public class QrisController {

    @FXML private TextField tfTotalPembayaran;
    @FXML private Button btKonfirmasiPembayaran;

    private PaymentViewController paymentViewController;
    private Pembayaran pembayaran;

    public void initialize() {
        btKonfirmasiPembayaran.setOnAction(e -> konfirmasi());
    }

    public void setPaymentViewController(PaymentViewController controller) {
        this.paymentViewController = controller;
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
        tfTotalPembayaran.setText(String.format("Rp %,.0f", pembayaran.getTotalPembayaran()));
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
