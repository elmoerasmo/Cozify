package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmoneyController {
    
    private static final String RUPIAH_FORMAT = "Rp %,.0f";
    
    @FXML private Text tfNomorEmoney;
    @FXML private TextField tfTotalPembayaran;
    @FXML private Button btKonfirmasiPembayaran;
    @FXML private Button btSalinEmoney;
    
    private PaymentViewController paymentViewController;
    
    public void initialize() {
       btSalinEmoney.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(tfNomorEmoney.getText());
            clipboard.setContent(content);
            btSalinEmoney.setText("Tersalin!");
        });

        btKonfirmasiPembayaran.setOnAction(e -> {
            Stage stage = (Stage) btKonfirmasiPembayaran.getScene().getWindow();
            stage.close();
        });
    }
    
    public void setData(double total, String nomorEwallet) {
        this.tfTotalPembayaran.setText(String.format(RUPIAH_FORMAT, total));
        this.tfNomorEmoney.setText(nomorEwallet);
    }

    public void setPaymentCode(String code) {
        tfNomorEmoney.setText(code);
    }
    
    public void setPaymentData(PaymentViewController controller, double total) {
        this.paymentViewController = controller;
        tfTotalPembayaran.setText(String.format(RUPIAH_FORMAT, total));
    }
    
    private void copyNomorEwallet() {
        String walletNumber = tfNomorEmoney.getText();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(walletNumber);
        clipboard.setContent(content);
        btSalinEmoney.setText("Tersalin!");
    }
    
    private void konfirmasiPembayaran() {
        closeWindow();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) btKonfirmasiPembayaran.getScene().getWindow();
        stage.close();
    }
}