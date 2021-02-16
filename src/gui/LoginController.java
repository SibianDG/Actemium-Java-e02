package gui;

import domein.DomeinController;
import exceptions.FouteAanmeldOfRegistreerGegevensException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.Vertaler;

import java.io.IOException;
import java.util.ResourceBundle;

public class LoginController extends GridPane {

    private DomeinController domeinController;
    private Vertaler vertaler;
    private KeuzeStartController vorigScherm;

    @FXML
    private GridPane gridLanguages;

    @FXML
    private Text titelTaal;

    @FXML
    private TextField txfGebruikersnaam;

    @FXML
    private PasswordField psfWachtwoord;

    @FXML
    private Button btnNextLogin;

    @FXML
    private HBox hbInvalidPassword;

    @FXML
    private Text txtFoutLogin;
    
    @FXML 
    private Button btnTerug;

    public LoginController(DomeinController domeinController, Vertaler vertaler, KeuzeStartController vorigScherm){
        super();
        this.domeinController = domeinController;
        this.vertaler = vertaler;
        this.vorigScherm = vorigScherm;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stelStringInVolgensTaal();
    }

    private void stelStringInVolgensTaal() {
        ResourceBundle bundle = vertaler.getBundle();
        titelTaal.setText(bundle.getString("aanmelden").toUpperCase());
        txfGebruikersnaam.setPromptText("| " + bundle.getString("gebruikersnaam"));
        psfWachtwoord.setPromptText("| " + bundle.getString("wachtwoord"));
        btnNextLogin.setText(bundle.getString("volgende"));
        btnTerug.setText("< " + bundle.getString("terug"));
    }

    @FXML
    void btnNextLoginOnAction(ActionEvent event) {

        try {
            if (txfGebruikersnaam.getText().isEmpty() || psfWachtwoord.getText().isEmpty()){
                txtFoutLogin.setText(vertaler.getBundle().getString("LoginEmpty"));
                txtFoutLogin.setVisible(true);
                hbInvalidPassword.setVisible(true);
            } else {
                domeinController.meldAan(txfGebruikersnaam.getText(), psfWachtwoord.getText());
                HoofdschermController hoofdschermController = new HoofdschermController(domeinController, vertaler);
                Scene scene = new Scene(hoofdschermController);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setTitle("Sokoban");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }
        } catch (FouteAanmeldOfRegistreerGegevensException e) {
            txtFoutLogin.setText(vertaler.getBundle().getString("foutAanmelden"));
            txtFoutLogin.setVisible(true);
            hbInvalidPassword.setVisible(true);
        } catch (Exception e){
            txtFoutLogin.setText(vertaler.getBundle().getString("foutAanmelden"));
            txtFoutLogin.setVisible(true);
            hbInvalidPassword.setVisible(true);
        }
    }
    
    @FXML
    public void btnTerugOnAction(ActionEvent event) {
    	Stage stage = (Stage) this.getScene().getWindow();
    	stage.setScene(vorigScherm.getScene());
    	stage.show();
    }

}

