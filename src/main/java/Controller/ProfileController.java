package Controller;

import DAO.UserDAO;
import Model.Session;
import Model.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProfileController {

    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Button btnRegisterOwner;
    @FXML private Button btnOwnerDashboard;
    @FXML private Button btnClose;
    @FXML private Button btnLogout;

    private final UserDAO userDAO = new UserDAO();
    private User currentUser;
    private Stage dashboardStage;

    public void setDashboardStage(Stage stage) {
        this.dashboardStage = stage;
    }

    @FXML
    private void initialize() {
        currentUser = Session.getUser();
        refresh();
        setupRoleButtons();
    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/View/EditProfile.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Edit Profile");
            stage.initOwner(btnClose.getScene().getWindow());
            stage.showAndWait();

            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupRoleButtons() {
        if ("CUSTOMER".equalsIgnoreCase(currentUser.getRole())) {
            btnRegisterOwner.setVisible(true);
        }
        if ("OWNER".equalsIgnoreCase(currentUser.getRole())
                && "Terverifikasi".equalsIgnoreCase(currentUser.getStatus())) {
            btnOwnerDashboard.setVisible(true);
        }
    }

    @FXML
    private void handleRegisterOwner() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Daftar Owner");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin ingin mendaftar sebagai pemilik kos?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        if (userDAO.upgradeToOwner(currentUser.getId())) {
            currentUser.setRole("OWNER");
            currentUser.setStatus("Menunggu Verifikasi");
            Session.setUser(currentUser);
            showAlert("Berhasil", "Pendaftaran owner berhasil.\nMenunggu verifikasi admin.");
            setupRoleButtons();
        } else {
            showAlert("Gagal", "Gagal mendaftar sebagai owner");
        }
    }

    @FXML
    private void handleOwnerDashboard() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/View/OwnerDashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Owner Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        ((Stage) btnClose.getScene().getWindow()).close();
    }

    @FXML
    private void handleLogout() {
        // Konfirmasi logout
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin ingin logout?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        // Hapus session
        Session.clear();

        // Tutup Profile dan Dashboard (jika terbuka)
        Stage profileStage = (Stage) btnClose.getScene().getWindow();
        profileStage.close();

        // Tutup dashboard
        if (DashboardController.dashboardStage != null) {
            DashboardController.dashboardStage.close();
            DashboardController.dashboardStage = null;
        }

        // Kembali ke login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loader.load()));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void refresh() {
        currentUser = Session.getUser();
        if (currentUser != null) {
            lblName.setText(currentUser.getNama());
            lblEmail.setText(currentUser.getEmail());
            lblPhone.setText(currentUser.getNoTelepon() != null ? currentUser.getNoTelepon() : "-");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
