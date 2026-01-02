package Controller;

import DAO.FavoritDAO;
import Model.Kos;
import Model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class KosCardController {

    @FXML private Label lblNama;
    @FXML private Label lblHarga;
    @FXML private Button btnFavorite;

    private Kos kos;
    private FavoritDAO favoritDAO = new FavoritDAO();

    // Gunakan Session untuk ambil user
    public void setData(Kos kos) {
        this.kos = kos;

        lblNama.setText(kos.getNama());
        lblHarga.setText("Rp" + (int) kos.getHarga());

        // Cek favorite dari DB
        int userId = Session.getUser().getId();
        boolean isFav = favoritDAO.isFavorite(userId, kos.getIdKos());
        kos.setFavorite(isFav);
        updateFavoriteUI();

        // Klik favorite
        btnFavorite.setOnAction(e -> toggleFavorite(userId));
    }

    private void toggleFavorite(int userId) {
        if (kos.isFavorite()) {
            favoritDAO.removeFavorite(userId, kos.getIdKos());
            kos.setFavorite(false);
        } else {
            favoritDAO.addFavorite(userId, kos.getIdKos());
            kos.setFavorite(true);
        }
        updateFavoriteUI();
    }

    private void updateFavoriteUI() {
        if (kos.isFavorite()) {
            btnFavorite.setText("❤️ Favorit");
        } else {
            btnFavorite.setText("♡ Tambahkan Favorit");
        }
    }
}
