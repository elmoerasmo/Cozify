package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CozifyDashboard extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
            
            Scene scene = new Scene(root, 1200, 800);
            
            primaryStage.setTitle("Cozify - Dashboard Management System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
            
            System.out.println("Application started successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }
    
    @Override
    public void stop() {
        DAO.DatabaseConnection.closeConnection();
        System.out.println("Application closed.");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}