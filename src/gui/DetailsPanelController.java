package gui;

import java.io.IOException;

import domain.UserModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class DetailsPanelController extends GridPane implements InvalidationListener {


    private TableViewPanelController tableViewPanelController;
    private UserModel user;

    @FXML
    private Text txtDetails;

    @FXML
    private GridPane gridDetails;

    @FXML
    private Button btnModify;
	
	public DetailsPanelController(TableViewPanelController tableViewPanelController) {

	    this.tableViewPanelController = tableViewPanelController;
	    tableViewPanelController.addListener(this);

		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
	}

    @Override
    public void invalidated(Observable observable) {
	    gridDetails.getChildren().clear();
        gridDetails.add(new Text(tableViewPanelController.getSelectedUser().getUsername()), 0, 0);
    }
}
