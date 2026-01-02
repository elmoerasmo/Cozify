package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.Kos;
import Model.Pembayaran;
import Model.Session;
import Model.User;
import DAO.PembayaranDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaymentViewController {

    @FXML private TextField tfNamaKos;
    @FXML private TextField tfAlamatKos;
    @FXML private Label lbTipeKos;
    @FXML private Label lbNoKamar;
    @FXML private TextField tfNamaPenyewa;
    @FXML private TextField tfEmail;
    @FXML private TextField tfNoTelepon;
    @FXML private Button btTfBank;
    @FXML private Button btEwallet;
    @FXML private Button btQris;
    @FXML private TextField tfPeriodeSewa;
    @FXML private TextField tfDurasiSewa;
    @FXML private TextField tfHargaSewa;
    @FXML private TextField tfTotalPembayaran;
    @FXML private Button btKonfirmasiPembayaran;

    private Pembayaran pembayaran;
    private String pembayaranDipilih = "";
    private final String tombolDipilih = "-fx-background-color: #4C7BFF; -fx-border-color: #4C7BFF; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";
    private final String tombol = "-fx-background-color: #EEF2FF; -fx-border-color: #4C7BFF; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";

    public void initialize() {
        btTfBank.setOnAction(e -> selectPaymentMethod("Transfer Bank"));
        btEwallet.setOnAction(e -> selectPaymentMethod("E-Wallet"));
        btQris.setOnAction(e -> selectPaymentMethod("QRIS"));
        btKonfirmasiPembayaran.setOnAction(e -> handleKonfirmasiPembayaran());
    }

    /**
     * Dipanggil dari dashboard saat user klik "Bayar"
     */
    public void setKosAndUser(Kos kos, User user) {
        this.pembayaran = new Pembayaran();
        pembayaran.setKos(kos);
        pembayaran.setUser(user);

        pembayaran.setTanggalMasuk(LocalDate.now());
        pembayaran.setTanggalKeluar(LocalDate.now().plusMonths(1));
        pembayaran.setHargaPerBulan(kos.getHarga());
        pembayaran.setTotalPembayaran(kos.getHarga()); // default 1 bulan

        loadDataToForm();
    }

    private void loadDataToForm() {
        if (pembayaran == null || pembayaran.getKos() == null || pembayaran.getUser() == null) return;

        Kos kos = pembayaran.getKos();
        User user = pembayaran.getUser();

        tfNamaKos.setText(kos.getNama());
        tfAlamatKos.setText(kos.getAlamat());
        lbTipeKos.setText("Tipe: " + kos.getTipeKos());
        lbNoKamar.setText("Kamar Tersedia: " + kos.getKamarTersedia());

        tfNamaPenyewa.setText(user.getNama());
        tfEmail.setText(user.getEmail());
        tfNoTelepon.setText(user.getNoTelepon());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        tfPeriodeSewa.setText(pembayaran.getTanggalMasuk().format(formatter)
                + " - " + pembayaran.getTanggalKeluar().format(formatter));
        tfDurasiSewa.setText("1 Bulan");
        tfHargaSewa.setText(String.format("Rp %,.0f", pembayaran.getHargaPerBulan()));
        tfTotalPembayaran.setText(String.format("Rp %,.0f", pembayaran.getTotalPembayaran()));
    }

    private void selectPaymentMethod(String method) {
        pembayaranDipilih = method;
        btTfBank.setStyle(tombol);
        btEwallet.setStyle(tombol);
        btQris.setStyle(tombol);

        switch (method) {
            case "Transfer Bank" -> btTfBank.setStyle(tombolDipilih);
            case "E-Wallet" -> btEwallet.setStyle(tombolDipilih);
            case "QRIS" -> btQris.setStyle(tombolDipilih);
        }
    }

    private void handleKonfirmasiPembayaran() {
        if (pembayaran == null) {
            showAlert("Error", "Data pembayaran tidak tersedia!");
            return;
        }
        if (pembayaranDipilih.isEmpty()) {
            showAlert("Error", "Silakan pilih metode pembayaran terlebih dahulu!");
            return;
        }

        pembayaran.setMetodePembayaran(pembayaranDipilih);
        pembayaran.setStatus("BERHASIL"); // set status

        // Insert ke database
        PembayaranDAO dao = new PembayaranDAO();
        dao.insert(pembayaran);

        // Tampilkan halaman pembayaran sesuai metode
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/" + pembayaranDipilih.replace(" ", "") + ".fxml"));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof QrisController qris) {
                qris.setPaymentViewController(this);
                qris.setPembayaran(pembayaran);
            }

            Stage stage = new Stage();
            stage.setTitle("Pembayaran " + pembayaranDipilih);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void showPembayaranBerhasil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/pembayaranBerhasil.fxml"));
            Parent root = loader.load();

            PembayaranBerhasilController ctrl = loader.getController();
            ctrl.setPembayaran(pembayaran); // kirim data pembayaran untuk ditampilkan

            Stage stage = new Stage();
            stage.setTitle("Pembayaran Berhasil");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();

            // Tutup halaman pembayaran lama
            Stage currentStage = (Stage) btKonfirmasiPembayaran.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
