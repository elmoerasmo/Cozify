package Controller;

import DAO.UserDAO;
import Model.Session;
import Model.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProfileController {

    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblPhone;
    @FXML private Button btnRegisterOwner;
    @FXML private Button btnOwnerDashboard;
    @FXML private Button btnAdminDashboard;
    @FXML private Button btnClose;
    @FXML private Button btnLogout;

    private final UserDAO userDAO = new UserDAO();
    private User currentUser;
    private String source = "USER"; 

    public void setSource(String source) {
        this.source = source;
        setupRoleButtons(); 
    }

    @FXML
    private void initialize() {
        currentUser = Session.getUser();
        refresh();
        setupRoleButtons();
    }

    private void setupRoleButtons() {
        if (currentUser == null) return;

        btnRegisterOwner.setVisible(false);
        btnRegisterOwner.setManaged(false);
        btnOwnerDashboard.setVisible(false);
        btnOwnerDashboard.setManaged(false);
        btnAdminDashboard.setVisible(false);
        btnAdminDashboard.setManaged(false);

        String role = currentUser.getRole();
        String status = currentUser.getStatus();

        if ("CUSTOMER".equalsIgnoreCase(role)) {
            btnRegisterOwner.setVisible(true);
            btnRegisterOwner.setManaged(true);
        } 
        else if ("OWNER".equalsIgnoreCase(role)) {
            if ("Terverifikasi".equalsIgnoreCase(status)) {
                btnOwnerDashboard.setVisible(true);
                btnOwnerDashboard.setManaged(true);
                
                if ("OWNER_DASHBOARD".equals(source)) {
                    btnOwnerDashboard.setText("Kembali ke Dashboard User");
                    btnOwnerDashboard.setStyle("-fx-background-color: #4C7BFF; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold;");
                } else {
                    btnOwnerDashboard.setText("Owner Dashboard");
                    btnOwnerDashboard.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold;");
                }
            }
        }
        else if ("ADMIN".equalsIgnoreCase(role)) {
            btnAdminDashboard.setVisible(true);
            btnAdminDashboard.setManaged(true);
            
            if ("ADMIN_DASHBOARD".equals(source)) {
                btnAdminDashboard.setText("Kembali ke Dashboard User");
                btnAdminDashboard.setStyle("-fx-background-color: #4C7BFF; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold;");
            } else {
                btnAdminDashboard.setText("Admin Dashboard");
                btnAdminDashboard.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold;");
            }
        }
    }

    @FXML
    private void handleOwnerDashboard() {
        if ("OWNER_DASHBOARD".equals(source)) {
            backToUserDashboard();
        } else {
            openSpecialDashboard("/View/OwnerDashboard.fxml", "Owner Dashboard", "OWNER");
        }
    }

    @FXML
    private void handleAdminDashboard() {
        if ("ADMIN_DASHBOARD".equals(source)) {
            backToUserDashboard();
        } else {
            openSpecialDashboard("/View/AdminDashboard.fxml", "Admin Dashboard", "ADMIN");
        }
    }

    private void backToUserDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cozify Dashboard");
            
            DashboardController.dashboardStage = stage;
            
            stage.show();
            handleClose();

            if (OwnerDashboardController.ownerStage != null) OwnerDashboardController.ownerStage.close();
            if (AdminDashboardController.adminStage != null) AdminDashboardController.adminStage.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSpecialDashboard(String fxmlPath, String title, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

            if ("OWNER".equals(role)) {
                OwnerDashboardController controller = loader.getController();
                controller.setStage(stage);
            } else {
                AdminDashboardController controller = loader.getController();
                controller.setStage(stage);
            }

            stage.show();
            handleClose();
            if (DashboardController.dashboardStage != null) DashboardController.dashboardStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EditProfile.fxml"));
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

    @FXML
    private void handleRegisterOwner() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin mendaftar sebagai pemilik kos?", ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
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
    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin logout?", ButtonType.OK, ButtonType.CANCEL);
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Session.clear();
            ((Stage) btnLogout.getScene().getWindow()).close();
            if (DashboardController.dashboardStage != null) DashboardController.dashboardStage.close();
            if (OwnerDashboardController.ownerStage != null) OwnerDashboardController.ownerStage.close();
            if (AdminDashboardController.adminStage != null) AdminDashboardController.adminStage.close();

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/View/Homepage.fxml"));
                Stage homepageStage = new Stage();
                homepageStage.setScene(new Scene(root));
                homepageStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClose() {
        ((Stage) btnClose.getScene().getWindow()).close();
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