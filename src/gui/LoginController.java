package gui;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import domain.facades.TicketFacade;
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
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import languages.LanguageResource;

public class LoginController extends GuiController {
		
    private UserFacade userFacade;
	private TicketFacade ticketFacade;
	
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

    public LoginController(UserFacade userFacade, TicketFacade ticketFacade){
        super();
        
        this.userFacade = userFacade;
        
        this.ticketFacade = ticketFacade;


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        txtErrorLogin.setOpacity(0);

        txfUsername.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
                tryLogin();
        });

        pwfPassword.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
                tryLogin();
        });

        txfUsername.setText("Admin123");
        pwfPassword.setText("Passwd123&");


    }

    @FXML
    void loginButtonOnAction(ActionEvent event) {
        tryLogin();
    }

    private void tryLogin(){
        try {
            if (txfUsername.getText().isEmpty() || pwfPassword.getText().isEmpty()){ //unnecessary check??? is already checked in domain
                txtErrorLogin.setText(LanguageResource.getString("username_password_mandatory"));
                txtErrorLogin.setOpacity(1);
            } else {
                userFacade.signIn(txfUsername.getText(), pwfPassword.getText());
                DashboardFrameController dashboardController = new DashboardFrameController(userFacade, ticketFacade);
                Scene scene = new Scene(dashboardController);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setTitle(LanguageResource.getString("dashboard")); //TODO review LanguageResource
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.setResizable(true);
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

