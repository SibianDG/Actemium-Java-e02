package gui;

import java.io.IOException;
import java.util.Locale;

import javax.persistence.EntityNotFoundException;

import domain.facades.ContractFacade;
import domain.facades.ContractTypeFacade;
import domain.facades.KnowledgeBaseFacade;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import languages.LanguageResource;

public class LoginController extends GuiController {
		

    private DashboardFrameController dashboardFrameController;
    private UserFacade userFacade;
	private TicketFacade ticketFacade;
    private ContractTypeFacade contractTypeFacade;
    private ContractFacade contractFacade;
    private KnowledgeBaseFacade knowledgeBaseFacade;
	
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

    @FXML
    private Button btnLanguage;

    public LoginController(UserFacade userFacade, TicketFacade ticketFacade, ContractTypeFacade contractTypeFacade, ContractFacade contractFacade, KnowledgeBaseFacade knowledgeBaseFacade){
        super();
        
        this.userFacade = userFacade;        
        this.ticketFacade = ticketFacade;
        this.contractTypeFacade = contractTypeFacade;
        this.contractFacade = contractFacade;
        this.knowledgeBaseFacade = knowledgeBaseFacade;
           
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

        setCorrectLanguagesAllTexts();

        txfUsername.setText("Sup123");
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
                txtErrorLogin.setOpacity(0);
                //DashboardFrameController dashboardController = new DashboardFrameController(userFacade, ticketFacade, contractTypeFacade, this);
                dashboardFrameController = new DashboardFrameController(userFacade, ticketFacade, contractTypeFacade, contractFacade, knowledgeBaseFacade, this);
                Scene scene = new Scene(dashboardFrameController);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setTitle(LanguageResource.getString("dashboard"));
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.setResizable(true);
                stage.centerOnScreen();
                stage.show();
            }
        } catch (IllegalArgumentException | PasswordException | BlockedUserException | EntityNotFoundException e) {
            txtErrorLogin.setText(e.getMessage());
            txtErrorLogin.setOpacity(1);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setCorrectLanguagesAllTexts(){
        txtTitle.setText(LanguageResource.getString("login"));
        lblUsername.setText(LanguageResource.getString("username"));
        txfUsername.setPromptText(LanguageResource.getString("username"));
        lblPassword.setText(LanguageResource.getString("password"));
        pwfPassword.setPromptText(LanguageResource.getString("password"));
    }

    @FXML
    void btnLanguageOnMousePressed(MouseEvent event) {
        if (btnLanguage.getText().equalsIgnoreCase("nl")){
            LanguageResource.setLocale(new Locale("nl"));
            btnLanguage.setText("EN");

        } else {
            LanguageResource.setLocale(Locale.ENGLISH);
            btnLanguage.setText("NL");
        }

        setCorrectLanguagesAllTexts();
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.setTitle(LanguageResource.getString("login"));
    }

}

