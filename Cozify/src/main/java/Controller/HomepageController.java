package Controller;

import DAO.UserDAO;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;


public class HomepageController {

    @FXML private Pane RegisterPane;
    @FXML private Pane loginPane;

    @FXML private TextField usernameReg;
    @FXML private TextField emailReg;
    @FXML private PasswordField passwordReg;
    @FXML private Label infoLabelLog;    
    @FXML private Label infoLabelReg;

    
    @FXML private TextField usernameLogin;
    @FXML private TextField emailLogin;
    @FXML private PasswordField passwordLogin;

    @FXML private ToggleButton toggleLogin;
    @FXML private ToggleButton toggleRegister;
    
    @FXML private Button registerButton;    
    @FXML private Button loginButton;


    private final Duration DURATION = Duration.millis(350);
    private UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        // Set posisi awal RegisterPane off-screen
        RegisterPane.setTranslateX(600);
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
        tl.setToX(loginPane.getWidth()+ 800);

        // Geser Register ke posisi 0 (muncul)
        TranslateTransition tr = new TranslateTransition(DURATION, RegisterPane);
        tr.setToX(0);

        tl.play();
        tr.play();

        // Style toggle
        toggleLogin.setStyle("-fx-background-color: transparent; -fx-text-fill: #0094D9;");
        toggleRegister.setStyle("-fx-background-color: #0094D9; -fx-text-fill: white;");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameReg.getText();
        String email = emailReg.getText();
        String password = passwordReg.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            infoLabelReg.setText("Harap isi semua field.");
            return;
        }

        User newUser = new User(username, email, password);
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            infoLabelReg.setText("Registrasi berhasil!");
            usernameReg.clear();
            emailReg.clear();
            passwordReg.clear();

            // buka Dashboard
            try {
                File fxmlFile = new File("src/main/java/View/Dashboard.fxml"); 
                FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL()); 
                Parent dashboardRoot = loader.load(); 
                Stage stage = (Stage) registerButton.getScene().getWindow(); 
                Scene scene = new Scene(dashboardRoot); stage.setScene(scene); stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                infoLabelReg.setText("Gagal membuka dashboard.");
            }
        } else {
            infoLabelReg.setText("Registrasi gagal. Coba lagi.");
        }
    }
    @FXML
    private void handleLogin(ActionEvent event) {       
    String username = usernameLogin.getText();
    String password = passwordLogin.getText();

    if (username.isEmpty() || password.isEmpty()) {
        infoLabelLog.setText("Harap isi semua field.");
        return;
    }

    User user = userDAO.loginUser(username, password); // loginUser mengembalikan User atau null

    if (user != null) {
        try {
            // Load Dashboard.fxml langsung dari file system
            File fxmlFile = new File("src/main/java/View/Dashboard.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent dashboardRoot = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            infoLabelLog.setText("Gagal membuka dashboard.");
        }
    } else {
        infoLabelLog.setText("Username atau password salah.");
    }

    }

}
