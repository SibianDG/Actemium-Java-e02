package gui;

import java.io.IOException;

import domain.controllers.DomainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class DetailsPanelController extends GridPane {
	

    @FXML
    private Text txtDetails;

    @FXML
    private GridPane gridDetails;

    @FXML
    private Button btnModify;
	
	public DetailsPanelController() {
		
		
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
	}
}
