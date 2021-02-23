package start;

import domain.PopulateDB;
import domain.controllers.DomainController;
import gui.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

            DomainController domainController = new DomainController(userDaoJpa);
            LoginController root = new LoginController(domainController);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toString());
            primaryStage.setScene(scene);
            primaryStage.setTitle(LanguageResource.getString("login"));
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
