package gui;

import domain.DomainController;
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

public class DashboardController extends GridPane {
    private DomainController domainController;

    @FXML
    private GridPane gridDashboard;

    @FXML
    private GridPane gridDashButtons;

    @FXML
    private Button btnLogout ;

    @FXML
    private Text txtName;

    @FXML
    private Text txtRole;

    @FXML
    private Text txtDashboard;

    public DashboardController(DomainController domainController) {
        super();
        this.domainController = domainController;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }

        //TODO creating dashboard dynamically based on user role (dashboard buttons)???

        initializeText();

    }

    @FXML
    void bnLogoutOnActon(ActionEvent event) {

    }

    private void initializeText() {
        txtName.setText(String.format("%s %s" , domainController.giveUserFirstName(), domainController.giveUserLastName()));
        txtRole.setText(domainController.giveUserType());
    }



    
    private void loadScene(String title, Object controller) { // method for switching to the next screen
        Scene scene = new Scene((Parent) controller);
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

}
