package start;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartUpGUI extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        try {
            //DomeinController domeinController = new DomeinController();
            //LanguageController root = new LanguageController(domeinController);
            //Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("styles.css").toString());
            //primaryStage.setScene(scene);
            //primaryStage.setTitle("Language");
            //primaryStage.setResizable(false);
            //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
            //primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
