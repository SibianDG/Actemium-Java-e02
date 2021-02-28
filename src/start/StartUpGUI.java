package start;

import domain.PopulateDB;
import domain.facades.UserFacade;
import gui.LoginController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import languages.LanguageResource;
import repository.UserDaoJpa;

public class StartUpGUI extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        try {
            UserDaoJpa userDaoJpa = new UserDaoJpa();
            PopulateDB populateDB = new PopulateDB();
            populateDB.run(userDaoJpa);

            UserFacade domainController = new UserFacade(userDaoJpa);
            LoginController root = new LoginController(domainController);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toString());
            primaryStage.setScene(scene);
            primaryStage.setTitle(LanguageResource.getString("login"));
            //primaryStage.setResizable(true);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
            primaryStage.show();

            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

            primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
    			if (KeyCode.F11.equals(e.getCode()))
    				primaryStage.setFullScreen(!primaryStage.isFullScreen());
    		});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
