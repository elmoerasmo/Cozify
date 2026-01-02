package Controller;

import DAO.KosDAO;
import Model.Kos;
import Model.Session;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Button profileButton;    
    @FXML private Button btnFavorit;
    @FXML private Button semuaButton, putraButton, putriButton, campurButton, fasilitasButton;
    @FXML private TextField searchField;
    @FXML private VBox vBoxListKos;

    public static List<Kos> allKosList = new ArrayList<>();
    private KosDAO kosDAO = new KosDAO();
    private List<Kos> kosList;
    private User currentUser;
    public static Stage dashboardStage; 
    
    public void setStage(Stage stage) {
           dashboardStage = stage;
       }
     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = Session.getUser();
        kosList = kosDAO.getAllKos();
        allKosList = kosList;
        loadKos(kosList);
       

        profileButton.setOnAction(e -> openProfile());
        btnFavorit.setOnAction(e -> handleGoToFavorit());
        semuaButton.setOnAction(e -> loadKos(kosList));
        putraButton.setOnAction(e -> loadKosFilter("Putra"));
        putriButton.setOnAction(e -> loadKosFilter("Putri"));
        campurButton.setOnAction(e -> loadKosFilter("Campur"));
        fasilitasButton.setOnAction(e -> handleFilterFasilitas());
    }

    
    private void loadKos(List<Kos> list) {
        vBoxListKos.getChildren().clear();
        for (Kos k : list) {
            vBoxListKos.getChildren().add(loadCard(k));
        }
    }

    private void loadKosFilter(String tipe) {
        List<Kos> filtered = kosList.stream()
                .filter(k -> k.getTipeKos().equalsIgnoreCase(tipe))
                .toList();
        loadKos(filtered);
    }

    private Parent loadCard(Kos kos) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/KosCard.fxml"));
            Parent card = loader.load();

            KosCardController controller = loader.getController();
            controller.setData(kos); // <-- otomatis ambil user dari Session
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return new VBox();
        }
    }

    private void handleFilterFasilitas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PilihanFasilitas.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGoToFavorit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FavoritView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadKos(allKosList); // refresh setelah favorit ditutup
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Profile.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil Pengguna");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
