package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/*public class HoofdschermController extends GridPane {
    private Vertaler vertaler;
    private DomeinController domeinController;

    @FXML
    private GridPane gridHoofdscherm;

    @FXML
    private Button btnSpeelSpel ;

    @FXML
    private Button btnMaakNieuwSpel;
    
    @FXML
    private Button btnWijzigSpel;

    @FXML
    private Button btnAfsluiten;

    @FXML
    private Text titelHoofdscherm;

    public HoofdschermController(DomeinController domeinController, Vertaler vertaler) {
        super();
        this.domeinController = domeinController;
        this.vertaler = vertaler;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Hoofdscherm.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
        
        if(!domeinController.isAdmin()) {
        	btnMaakNieuwSpel.setDisable(true);
        	btnWijzigSpel.setDisable(true);
        }
        stelStringInVolgensTaal();
    }

    @FXML
    void btnSpeelSpelOnAction(ActionEvent event) {
    	loadScene("kiesSpelTitel", new KiesSpelWijzigenController(domeinController, vertaler, this));
    }
    
    @FXML
    void btnMaakNieuwSpelOnAction(ActionEvent event) {
    	loadScene("maakNieuwSpel", new MaakNieuwSpelController(domeinController, vertaler, this));
    }
    
    @FXML
    void btnWijzigSpelOnAction(ActionEvent event) {
    	loadScene("wijzigSpel", new SelecteerSpelController(domeinController, vertaler, this));
    }
    
    @FXML
    void btnAfsluitenOnAction(ActionEvent event) {
    		Platform.exit();
    		System.exit(0);
    }

    private void stelStringInVolgensTaal(){
        ResourceBundle bundle = vertaler.getBundle();
        titelHoofdscherm.setText(bundle.getString("hoofdscherm").toUpperCase());
        btnSpeelSpel.setText(bundle.getString("speelSpel"));
        btnMaakNieuwSpel.setText(bundle.getString("maakSpel"));
        btnWijzigSpel.setText(bundle.getString("wijzigSpel"));
        btnAfsluiten.setText(bundle.getString("afsluiten"));
    }
    
    private void loadScene(String titel, Object controller) {
        Scene scene = new Scene((Parent) controller);
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle(vertaler.getBundle().getString(titel));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

}
*/