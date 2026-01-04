package Controller;

import Model.Kos;
import Model.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class FavoritViewController implements Initializable {

    @FXML private VBox vBoxFavorit;   
    @FXML private Button btnBack;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tampilkanDataFavorit();
    }

    private void tampilkanDataFavorit() {
        if (vBoxFavorit == null) return;
        vBoxFavorit.getChildren().clear();

        for (Kos k : DashboardController.allKosList) {
            if (k.isFavorite()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/KosCard.fxml"));
                    Parent card = loader.load();
                    KosCardController controller = loader.getController();
                    controller.setData(k); 
                    vBoxFavorit.getChildren().add(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @FXML
    private void handleBackToHome() {
        Stage stage = (Stage) btnBack.getScene().getWindow(); 
        stage.close();
    }
}
