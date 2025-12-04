/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.File;
import java.io.IOException;

public class DashboardController {

@FXML
private Label welcomeLabel;

@FXML
private Button logoutButton;

@FXML
private void initialize() {
    // Default welcome message
    welcomeLabel.setText("Selamat datang di Dashboard!");
}

@FXML
private void handleLogout(ActionEvent event) {
    try {
        // Kembali ke Homepage.fxml
        File fxmlFile = new File("src/main/java/View/Homepage.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
        Parent root = loader.load();

        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

}
