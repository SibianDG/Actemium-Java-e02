package gui;

import domain.DomainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardItemController extends GridPane {


    @FXML
    private GridPane gridDashboardItem;

    @FXML
    private ImageView imvIcon;

    @FXML
    private Text txtTitle;

    public DashboardItemController(DomainController domainController) {
        super();

        initializeText();

    }

    //TODO: OnAction Event


    public void setData(String image, String text){

    }


    private void initializeText() {
        //txtName.setText(String.format("%s %s" , domainController.giveUserFirstName(), domainController.giveUserLastName()));
        //txtRole.setText(domainController.giveUserType());
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
