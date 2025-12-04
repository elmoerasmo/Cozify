package Main;

import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

@Override
public void start(Stage stage) throws Exception {

    // Load FXML langsung dari file system
    File fxmlFile = new File("src/main/java/View/Homepage.fxml");
    URL url = fxmlFile.toURI().toURL();
    FXMLLoader loader = new FXMLLoader(url);
    Parent root = loader.load();

    Scene scene = new Scene(root);
    stage.setTitle("Cozify");
    stage.setScene(scene);
    stage.show();
}

public static void main(String[] args) {
    launch(args);
}
}
