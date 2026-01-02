package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import DAO.UserDAO;
import Model.Session;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class EditProfileController {

    @FXML private TextField txtNama;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private final UserDAO userDAO = new UserDAO();
    private User user;

    @FXML
    private void initialize() {
        user = Session.getUser();

        if (user != null) {
            txtNama.setText(user.getNama());
            txtEmail.setText(user.getEmail());
            txtPhone.setText(user.getNoTelepon());
        }
    }

    @FXML
    private void handleSave() {
        user.setNama(txtNama.getText());
        user.setEmail(txtEmail.getText());
        user.setNoTelepon(txtPhone.getText());

        if (userDAO.updateProfile(user)) {
            Session.setUser(user);
            close();
        } else {
            showAlert("Gagal", "Gagal menyimpan perubahan");
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        ((Stage) btnSave.getScene().getWindow()).close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}
