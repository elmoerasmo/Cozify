package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class HomepageController implements Initializable {

    @FXML
    private Pane RegisterPane;
    @FXML
    private TextField usernameReg;
    @FXML
    private PasswordField passwordReg;
    @FXML
    private Pane loginPane;
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;
    @FXML
    private ToggleButton toggleLogin;
    @FXML
    private ToggleButton toggleRegister;

    private final Duration DURATION = Duration.millis(350);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Set posisi awal RegisterPane supaya di luar layar (kanan)
        loginPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                double width = newScene.getWidth();
                RegisterPane.setTranslateX(width);   // Registrasi off-screen
            }
        });

        // Set Toggle default (Login aktif)
        toggleLogin.setSelected(true);
        toggleLogin.setStyle("-fx-background-color: #0094D9; -fx-text-fill: white;");
        toggleRegister.setStyle("-fx-background-color: transparent; -fx-text-fill: #0094D9;");
    }

    @FXML
    private void showLogin(ActionEvent event) {

        // Geser Login ke posisi 0 (muncul)
        TranslateTransition tl = new TranslateTransition(DURATION, loginPane);
        tl.setToX(0);

        // Geser Register ke kanan (hilang)
        TranslateTransition tr = new TranslateTransition(DURATION, RegisterPane);
        tr.setToX(RegisterPane.getScene().getWidth());

        tl.play();
        tr.play();

        // Style toggle
        toggleLogin.setStyle("-fx-background-color: #0094D9; -fx-text-fill: white;");
        toggleRegister.setStyle("-fx-background-color: transparent; -fx-text-fill: #0094D9;");
    }

    @FXML
    private void showRegister(ActionEvent event) {

        // Geser Login ke kiri
        TranslateTransition tl = new TranslateTransition(DURATION, loginPane);
        tl.setToX(-loginPane.getWidth());

        // Geser Register ke posisi 0 (muncul)
        TranslateTransition tr = new TranslateTransition(DURATION, RegisterPane);
        tr.setToX(0);

        tl.play();
        tr.play();

        // Style toggle
        toggleLogin.setStyle("-fx-background-color: transparent; -fx-text-fill: #0094D9;");
        toggleRegister.setStyle("-fx-background-color: #0094D9; -fx-text-fill: white;");
    }
}