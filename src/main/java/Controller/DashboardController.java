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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;

public class DashboardController implements Initializable {

    @FXML private Button profileButton;    
    @FXML private Button btnFavorit;
    @FXML private Button semuaButton, putraButton, putriButton, campurButton, fasilitasButton;
    @FXML private TextField searchField;
    @FXML private VBox vBoxListKos;   
    @FXML private VBox main;
    @FXML private HBox suggestionContainer;
    @FXML private Hyperlink hlSuggestion;
    @FXML private ComboBox<String> cbUrutkan;
    @FXML private FlowPane flowPane;


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
        kosList = kosDAO.getVerifiedKos();   
        allKosList = kosList;
        
        cbUrutkan.getItems().addAll(
            "A - Z",
            "Z - A",
            "Harga Terendah",
            "Harga Tertinggi"
        );

        cbUrutkan.setOnAction(e -> handleSort());
    
        loadKos(kosList);
        
        Platform.runLater(() -> {
            dashboardStage = (Stage) main.getScene().getWindow();
        });
        
        searchField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleSearch();
            }
        });
        profileButton.setOnAction(e -> openProfile());
        btnFavorit.setOnAction(e -> handleGoToFavorit());
        semuaButton.setOnAction(e -> loadKos(kosList));
        putraButton.setOnAction(e -> loadKosFilter("Putra"));
        putriButton.setOnAction(e -> loadKosFilter("Putri"));
        campurButton.setOnAction(e -> loadKosFilter("Campur"));
        fasilitasButton.setOnAction(e -> handleFilterFasilitas());
    }

    private void loadData() {
        vBoxListKos.getChildren().clear();
        List<Kos> list = kosDAO.getAllKos();
        for (Kos k : list) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/KosCard.fxml"));
                Parent card = loader.load();
                
                KosCardController controller = loader.getController();
                controller.setData(k);
                
                vBoxListKos.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadKos(List<Kos> list) {
        vBoxListKos.getChildren().clear();
        for (Kos k : list) {
            if (k.getKamarTersedia() > 0) {
            vBoxListKos.getChildren().add(loadCard(k));
            }
        }
    }

    private void loadKosFilter(String tipe) {
        List<Kos> filtered = kosList.stream()
                .filter(k -> k.getTipeKos().equalsIgnoreCase(tipe))
                .toList();
        loadKos(filtered);
        
    }

    private Parent loadCard(Kos kos) {
        if (kos.getStatus() == null || !kos.getStatus().equalsIgnoreCase("Terverifikasi")) {
            return null; 
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/KosCard.fxml"));
            Parent card = loader.load();

            KosCardController controller = loader.getController();
            controller.setData(kos); 
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void displayKos() {
        flowPane.getChildren().clear();
        List<Kos> allKos = kosDAO.getAllKos(); 

        for (Kos k : allKos) {
            Parent card = loadCard(k);
            if (card != null) {
                flowPane.getChildren().add(card);
            }
        }
    }

    @FXML
    private void handleFilterFasilitas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PilihanFasilitas.fxml"));
            Parent root = loader.load();

            PilihanFasilitasController filterController = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            List<String> kriteria = filterController.getSelectedFasilitas();

            if (kriteria != null && !kriteria.isEmpty()) {
                filterDataFasilitas(kriteria);
            } else {
                loadKos(kosList); 
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void filterDataFasilitas(List<String> kriteria) {
        List<Kos> filtered = kosList.stream()
                .filter(k -> {
                    String fasilitasKos = k.getFasilitas() != null ? k.getFasilitas().toLowerCase() : "";
                    return kriteria.stream().allMatch(f -> fasilitasKos.contains(f.toLowerCase()));
                })
                .toList();

        loadKos(filtered);
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

            loadKos(allKosList); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Profile.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle("Profil Pengguna");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        suggestionContainer.setVisible(false);
        suggestionContainer.setManaged(false);

        if (query.isEmpty()) {
            loadKos(kosList);
            return;
        }

        List<Kos> exactMatch = kosList.stream()
                .filter(k -> k.getNama().toLowerCase().contains(query))
                .toList();

        if (!exactMatch.isEmpty()) {
            loadKos(exactMatch);
        } else {
            String suggestion = findBestSuggestion(query);
            if (suggestion != null) {
                hlSuggestion.setText(suggestion);
                suggestionContainer.setVisible(true);
                suggestionContainer.setManaged(true);
            }
            loadKos(new ArrayList<>()); 
        }
    }

    private String findBestSuggestion(String input) {
        String bestMatch = null;
        int minDistance = 3; 

        for (Kos k : kosList) {
            String nama = k.getNama().toLowerCase();
            String[] words = nama.split("\\s+");

            for (String word : words) {
                int distance = calculateLevenshteinDistance(input, word);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestMatch = k.getNama();
                }
            }
        }
        return bestMatch;
    }

    @FXML
    private void handleApplySuggestion() {
        searchField.setText(hlSuggestion.getText());
        handleSearch();
    }

    // Algoritma Levenshtein Distance
    private int calculateLevenshteinDistance(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];
        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else {
                    dp[i][j] = Math.min(Math.min(
                        dp[i - 1][j - 1] + (x.charAt(i - 1) == y.charAt(j - 1) ? 0 : 1),
                        dp[i - 1][j] + 1),
                        dp[i][j - 1] + 1);
                }
            }
        }
        return dp[x.length()][y.length()];
    }
    
    @FXML
    private void handleSort() {
        String selected = cbUrutkan.getValue();
        if (selected == null || kosList == null) return;

        // Buat list baru untuk diurutkan agar tidak merusak data asli di kosList jika diperlukan
        List<Kos> sortedList = new ArrayList<>(kosList);

        switch (selected) {
            case "A - Z":
                sortedList.sort((k1, k2) -> k1.getNama().compareToIgnoreCase(k2.getNama()));
                break;
            case "Z - A":
                sortedList.sort((k1, k2) -> k2.getNama().compareToIgnoreCase(k1.getNama()));
                break;
            case "Harga Terendah":
                sortedList.sort((k1, k2) -> Double.compare(k1.getHarga(), k2.getHarga()));
                break;
            case "Harga Tertinggi":
                sortedList.sort((k1, k2) -> Double.compare(k2.getHarga(), k1.getHarga()));
                break;
        }

        loadKos(sortedList);
    }
}
