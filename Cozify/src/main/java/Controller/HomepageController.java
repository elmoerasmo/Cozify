/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class HomepageController implements Initializable {

    @FXML
    private Pane RegisterPane;
    @FXML
    private TextField usernameReg;
    @FXML
    private PasswordField passwordReg;
    @FXML
    private Button registerButton;
    @FXML
    private Pane loginPane;
    @FXML
    private TextField usernameLogin;
    @FXML
    private PasswordField passwordLogin;
    @FXML
    private Button loginButton;
    @FXML
    private ToggleButton toggleLogin;
    @FXML
    private ToggleGroup authGroup;
    @FXML
    private ToggleButton toggleRegister;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void showLogin(ActionEvent event) {
    }

    @FXML
    private void showRegister(ActionEvent event) {
    }
    
}
