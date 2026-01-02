package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TransferBankController {
    
    private static final String rupiah = "Rp %,.0f";
    
    @FXML private Text tfNomorRekening;
    @FXML private TextField tFTransBank;
    @FXML private Button btKonfirmasiPembayaran;
    @FXML private Button btSalinRek;
    
    private PaymentViewController paymentViewController;
    
    public void initialize() {
        btSalinRek.setOnAction(e -> copyNomorRekening());
        btKonfirmasiPembayaran.setOnAction(e -> konfirmasiPembayaran());
    }
    
    public void setPaymentViewController(PaymentViewController controller) {
        this.paymentViewController = controller;
    }
    
    public void setTotalPembayaran(double total) {
        tFTransBank.setText(String.format(rupiah, total));
    }
    
    private void copyNomorRekening() {
        String accountNumber = tfNomorRekening.getText();
        copyToClipboard(accountNumber);
        System.out.println("Nomor rekening berhasil disalin: " + accountNumber);
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