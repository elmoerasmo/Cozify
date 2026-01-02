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
        btSalinEmoney.setOnAction(e -> copyNomorEwallet());
        btKonfirmasiPembayaran.setOnAction(e -> konfirmasiPembayaran());
    }
    
    public void setPaymentViewController(PaymentViewController controller) {
        this.paymentViewController = controller;
    }
    
    public void setTotalPembayaran(double total) {
        tfTotalPembayaran.setText(String.format(RUPIAH_FORMAT, total));
    }
    
    private void copyNomorEwallet() {
        String walletNumber = tfNomorEmoney.getText();
        copyToClipboard(walletNumber);
        System.out.println("Nomor e-wallet berhasil disalin: " + walletNumber);
    }
    
    private void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
    
    private void konfirmasiPembayaran() {
        closeWindow();
        if (paymentViewController != null) {
            paymentViewController.showPembayaranBerhasil();
        }
    }
    
    private void closeWindow() {
        Stage stage = (Stage) btKonfirmasiPembayaran.getScene().getWindow();
        stage.close();
    }
}