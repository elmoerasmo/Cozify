package Controller;

import DAO.FavoritDAO;
import Model.Kos;
import Model.Session;
import Model.User;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class KosCardController {

    @FXML private ImageView kosImage;
    @FXML private Label lblTipe;
    @FXML private Label lblNama;
    @FXML private Label lblFasilitas;
    @FXML private Label lblSisaKamar;   
    @FXML private Label lblHarga;
    @FXML private Button btnFavorite; 
    @FXML private Button btnPesan; 

    private Kos kos;
    private final FavoritDAO favoritDAO = new FavoritDAO();

    public void setData(Kos kos) {
        this.kos = kos;

        lblNama.setText(kos.getNama());
        lblTipe.setText(kos.getTipeKos());
        lblFasilitas.setText(kos.getFasilitas());
        lblHarga.setText("Rp " + String.format("%,.0f", kos.getHarga()));

            lblSisaKamar.setText("📦 Sisa " + kos.getKamarTersedia() + " dari " + kos.getTotalKamar() + " kamar");

            if (kos.getKamarTersedia() <= 0) {
                btnPesan.setText("Kamar Penuh");
                btnPesan.setDisable(true);
                btnPesan.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: white; -fx-background-radius: 10;"); 
                lblSisaKamar.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); 
            } else {
                btnPesan.setDisable(false);
                btnPesan.setText("Pesan Sekarang");
                btnPesan.setStyle("-fx-background-color: #4C7BFF; -fx-text-fill: white; -fx-background-radius: 10;");
            }
        
        if (kos.getFoto() != null && !kos.getFoto().isEmpty()) {
            try {
                InputStream is = getClass().getResourceAsStream("/images/" + kos.getFoto());
                if (is != null) {
                    kosImage.setImage(new Image(is));
                } else {
                    System.out.println("File gambar tidak ditemukan: " + kos.getFoto());
                }
            } catch (Exception e) {
                System.out.println("Gagal memuat gambar: " + e.getMessage());
            }
        }

        User user = Session.getUser();
        if (user != null) {
            boolean isFav = favoritDAO.isFavorite(user.getId(), kos.getIdKos());
            kos.setFavorite(isFav);
            updateFavoriteUI();

            btnFavorite.setOnAction(e -> handleToggleFavorite(user.getId()));
        } else {
            btnFavorite.setVisible(false);
        }
    }

    private void handleToggleFavorite(int userId) {
        if (kos.isFavorite()) {
            if (favoritDAO.removeFavorite(userId, kos.getIdKos())) {
                kos.setFavorite(false);
            }
        } else {
            if (favoritDAO.addFavorite(userId, kos.getIdKos())) {
                kos.setFavorite(true);
            }
        }
        updateFavoriteUI();
    }

    private void updateFavoriteUI() {
        if (kos.isFavorite()) {
            btnFavorite.setText("❤ ️Favorit");
            btnFavorite.setStyle("-fx-text-fill: red;");
        } else {
            btnFavorite.setText("♡ Tambahkan Favorit");
            btnFavorite.setStyle("-fx-text-fill: black;");
        }
    }
    
    @FXML
    private void handlePesanSekarang() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/paymentView.fxml"));
            Parent root = loader.load();

            PaymentViewController controller = loader.getController();
            controller.setOrderData(this.kos); //

            Stage stage = (Stage) btnPesan.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
}
}