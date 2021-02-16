package start;

import domain.DomainController;
import gui.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class StartUpGUI extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        try {
            DomainController domainController = new DomainController();
            LoginController root = new LoginController(domainController);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toString());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login"/*ResourceBundle.getBundle().getString()*/);
            primaryStage.setResizable(false);
            //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
