package start;

import domain.ActemiumCompany;
import domain.ActemiumContract;
import domain.ActemiumContractType;
import domain.ActemiumTicket;
import domain.PopulateDB;
import domain.facades.ContractFacade;
import domain.facades.ContractTypeFacade;
import domain.facades.TicketFacade;
import domain.facades.UserFacade;
import domain.manager.Actemium;
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
import repository.GenericDaoJpa;
import repository.UserDaoJpa;

public class StartUpGUI extends Application {


    @Override
    public void start(Stage primaryStage)
    {

        try {
            UserDaoJpa userDaoJpa= new UserDaoJpa();
            GenericDaoJpa<ActemiumCompany> companyDaoJpa = new GenericDaoJpa<>(ActemiumCompany.class);
            GenericDaoJpa<ActemiumTicket> ticketDaoJpa = new GenericDaoJpa<>(ActemiumTicket.class);
            GenericDaoJpa<ActemiumContractType> contractTypeDaoJpa = new GenericDaoJpa<>(ActemiumContractType.class);
            GenericDaoJpa<ActemiumContract> contractDaoJpa = new GenericDaoJpa<>(ActemiumContract.class);
            PopulateDB populateDB = new PopulateDB();
            populateDB.run(userDaoJpa,contractTypeDaoJpa);

            Actemium actemium = new Actemium(userDaoJpa, companyDaoJpa, ticketDaoJpa, contractTypeDaoJpa, contractDaoJpa);
            UserFacade userFacade = new UserFacade(actemium);
            TicketFacade ticketFacade = new TicketFacade(actemium);
            ContractTypeFacade contractTypeFacade = new ContractTypeFacade(actemium);
            ContractFacade contractFacade = new ContractFacade(actemium);
            LoginController root = new LoginController(userFacade, ticketFacade, contractTypeFacade, contractFacade);
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
