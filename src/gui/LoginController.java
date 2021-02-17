package gui;

import java.io.IOException;

import domain.DomainController;
import exceptions.PasswordException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import languages.LanguageResource;

public class LoginController extends GridPane {

    private DomainController domainController;

    @FXML
    private GridPane gridLogin;

    @FXML
    private Text titleLogin;

    @FXML
    private TextField txfUsername;

    @FXML
    private PasswordField pwfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private HBox hbInvalidPassword;

    @FXML
    private Text txtErrorLogin;

    public LoginController(DomainController domainController){
        super();
        this.domainController = domainController;


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {

        try {
            if (txfUsername.getText().isEmpty() || pwfPassword.getText().isEmpty()){ //unnecessary check??? get already caught in domain
                txtErrorLogin.setText(LanguageResource.getString("username_password_mandatory"));
                txtErrorLogin.setVisible(true);
                hbInvalidPassword.setVisible(true);
            } else {
                domainController.signIn(txfUsername.getText(), pwfPassword.getText());

                DashboardController dashboardController = new DashboardController(domainController);
                Scene scene = new Scene(dashboardController);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setTitle(LanguageResource.getString("dashboard")); //TODO review LanguageResource
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IllegalArgumentException e) {
            txtErrorLogin.setText(e.getMessage());
            txtErrorLogin.setVisible(true);
            hbInvalidPassword.setVisible(true);
        }catch (PasswordException e) {
            txtErrorLogin.setText(e.getMessage());
            txtErrorLogin.setVisible(true);
            hbInvalidPassword.setVisible(true);
        } catch (Exception e){
            txtErrorLogin.setText(e.getMessage());
            txtErrorLogin.setVisible(true);
            hbInvalidPassword.setVisible(true);
        }
    }

}

