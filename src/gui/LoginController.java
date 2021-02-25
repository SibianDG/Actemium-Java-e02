package gui;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import domain.facades.UserFacade;
import exceptions.BlockedUserException;
import exceptions.PasswordException;
import gui.controllers.GuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import languages.LanguageResource;

public class LoginController extends GuiController {

    private UserFacade domainController;

    @FXML
    private Text txtTitle;

    @FXML
    private Label lblUsername;

    @FXML
    private TextField txfUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private PasswordField pwfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Text txtErrorLogin;

    public LoginController(UserFacade domainController){
        super();
        this.domainController = domainController;


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            txtErrorLogin.setOpacity(0);

            txfUsername.setText("Admin123");
            pwfPassword.setText("Passwd123&");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void loginButtonOnAction(ActionEvent event) {

        try {
            if (txfUsername.getText().isEmpty() || pwfPassword.getText().isEmpty()){ //unnecessary check??? is already checked in domain
                txtErrorLogin.setText(LanguageResource.getString("username_password_mandatory"));
                txtErrorLogin.setOpacity(1);
            } else {
                domainController.signIn(txfUsername.getText(), pwfPassword.getText());
                DashboardFrameController dashboardController = new DashboardFrameController(domainController);
                Scene scene = new Scene(dashboardController);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setTitle(LanguageResource.getString("dashboard")); //TODO review LanguageResource
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IllegalArgumentException | PasswordException | BlockedUserException | EntityNotFoundException e) {
            txtErrorLogin.setText(e.getMessage());
            txtErrorLogin.setOpacity(1);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}

