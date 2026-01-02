package Controller;

import DAO.UserDAO;
import Model.User;
import Model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {

    @FXML private Pane loginPane;
    @FXML private Pane RegisterPane;

    @FXML private TextField usernameLogin;
    @FXML private PasswordField passwordLogin;
    @FXML private Label infoLabelLog;

    @FXML private TextField emailReg;
    @FXML private TextField usernameReg;
    @FXML private PasswordField passwordReg;
    @FXML private Label infoLabelReg;

    @FXML private ToggleButton toggleLogin;
    @FXML private ToggleButton toggleRegister;

    private UserDAO userDAO = new UserDAO();

    // ================= Toggle Login/Register =================
    @FXML
    private void showLogin() {
        loginPane.setVisible(true);
        RegisterPane.setVisible(false);
        infoLabelLog.setText("");
        infoLabelReg.setText("");
    }

    @FXML
    private void showRegister() {
        loginPane.setVisible(false);
        RegisterPane.setVisible(true);
        infoLabelLog.setText("");
        infoLabelReg.setText("");
    }

    // ================= Login =================
    @FXML
    private void handleLogin() {
        String input = usernameLogin.getText().trim();
        String password = passwordLogin.getText().trim();

        if (input.isEmpty() || password.isEmpty()) {
            infoLabelLog.setText("Username/email dan password harus diisi!");
            return;
        }

        User user = userDAO.loginUser(input, password);
        if (user == null) {
            infoLabelLog.setText("Username/email atau password salah!");
            return;
        }
        
        Session.setUser(user);
        openDashboard(user);
    }

    // ================= Register =================
    @FXML
    private void handleRegister() {
        String email = emailReg.getText().trim();
        String username = usernameReg.getText().trim();
        String password = passwordReg.getText().trim();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            infoLabelReg.setText("Semua field harus diisi!");
            return;
        }

        if (userDAO.isUsernameExist(username)) {
            infoLabelReg.setText("Username sudah digunakan!");
            return;
        }

        if (userDAO.isEmailExist(email)) {
            infoLabelReg.setText("Email sudah digunakan!");
            return;
        }

        User newUser = new User();
        newUser.setNama(username);
        newUser.setEmail(email);
        newUser.setRole("CUSTOMER"); // default role
        newUser.setStatus("PENDING"); // default status
        newUser.setPassword(password);

        if (userDAO.registerUser(newUser)) {
            infoLabelReg.setText("Registrasi berhasil! Silakan login.");
            showLogin();
        } else {
            infoLabelReg.setText("Registrasi gagal. Coba lagi.");
        }
    }

    
    private void openDashboard(User user) {
        try {
            FXMLLoader loader;
            Stage stage = new Stage();

            switch (user.getRole().toUpperCase()) {
                case "ADMIN":
                    loader = new FXMLLoader(getClass().getResource("/View/AdminDashboard.fxml"));
                    break;
                    
                case "OWNER":
                if ("Terverifikasi".equalsIgnoreCase(user.getStatus())) {
                    // Owner sudah diverifikasi → OwnerDashboard
                    loader = new FXMLLoader(getClass().getResource("/View/OwnerDashboard.fxml"));
                } else {
                    // Owner belum diverifikasi → Dashboard default (misal CustomerDashboard)
                    loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
                }
                break;
            
                case "CUSTOMER":
                    loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
                    break;
                default:
                    infoLabelLog.setText("Role tidak dikenal!");
                    return;
            }

            stage.setScene(new Scene(loader.load()));
            stage.setTitle(user.getRole() + " Dashboard");
            stage.show();

            // Kirim data user ke controller dashboard jika implements ControllerWithUser
            Object controller = loader.getController();
            if (controller instanceof ControllerWithUser) {
                ((ControllerWithUser) controller).setUser(user);
            }

            // Tutup window login
            usernameLogin.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
            infoLabelLog.setText("Error loading dashboard!");
        }
    }
}