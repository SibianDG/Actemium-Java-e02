package gui;

import java.io.IOException;

import domain.Customer;
import domain.Employee;
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

	public void btnModifyOnAction() {
        //Todo modify User
    }

    @Override
    public void invalidated(Observable observable) {
        System.out.println(tableViewPanelController.getSelectedUser().getClass());
	    if (tableViewPanelController.getSelectedUser() instanceof Employee ) {
	        fillGridPaneEmployee();
        } else {
	        fillGridPaneCustomer();
        }
    }

    //Todo
    public void fillGridPaneEmployee() {
	    Employee user = (Employee) tableViewPanelController.getSelectedUser();
        gridDetails.getChildren().clear();
        gridDetails.add(new Text("Username"), 0, 0);
        gridDetails.add(new Text(user.getUsername()), 1, 0);
        gridDetails.add(new Text("Status"), 0, 1);
        gridDetails.add(new Text(user.getStatus().toString()), 1, 1);
        gridDetails.add(new Text("Lastname"), 0, 2);
        gridDetails.add(new Text(user.getLastName()), 1, 2);
        gridDetails.add(new Text("Firstname"), 0, 3);
        gridDetails.add(new Text(user.getFirstName()), 1, 3);
        gridDetails.add(new Text("Address"), 0, 4);
        gridDetails.add(new Text(user.getAddress()), 1, 4);
        gridDetails.add(new Text("E-mail"), 0, 5);
        gridDetails.add(new Text(user.getEmailAddress()), 1, 5);
        gridDetails.add(new Text("Phone number"), 0, 6);
        gridDetails.add(new Text(user.getPhoneNumber()), 1, 6);
        gridDetails.add(new Text("Seniority"), 0, 7);
        gridDetails.add(new Text(String.valueOf(user.giveSeniority())), 1, 7);
        gridDetails.add(new Text("Role"), 0, 8);
        gridDetails.add(new Text(user.getRole().toString()), 1, 8);
    }

    //Todo
    public void fillGridPaneCustomer() {
        Customer user = (Customer) tableViewPanelController.getSelectedUser();
        gridDetails.getChildren().clear();
        gridDetails.add(new Text(tableViewPanelController.getSelectedUser().getUsername()), 0, 0);
    }
}
