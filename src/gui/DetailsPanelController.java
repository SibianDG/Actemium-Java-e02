package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        this.setVisible(false);

    }

	public void btnModifyOnAction() {
        //Todo modify User
    }

    @Override
    public void invalidated(Observable observable) {
        this.setVisible(true);


        System.out.println(tableViewPanelController.getSelectedUser().getClass());
        gridDetails.getChildren().clear();

        if (tableViewPanelController.getSelectedUser() instanceof Employee ) {
	        fillGridPaneEmployee();
        } else {
	        fillGridPaneCustomer();
        }
    }

    public void fillGridPaneEmployee() {
	    Employee user = (Employee) tableViewPanelController.getSelectedUser();

        Map<String, String> details = Map.of(
                "Username", user.getUsername()
                , "Status", user.getStatus().toString()
                , "Lastname", user.getLastName()
                , "Firstname", user.getFirstName()
                , "Address", user.getAddress()
                , "Email", user.getEmailAddress()
                , "Phone number", user.getPhoneNumber()
                , "Seniority", String.valueOf(user.giveSeniority())
                , "Role", user.getRole().toString()
        );

        addDetailsToGridDetails(details);
    }

    public void fillGridPaneCustomer() {
        Customer user = (Customer) tableViewPanelController.getSelectedUser();
        Map<String, String> details = Map.of(
                "Username", user.getUsername()
                , "Status", user.getStatus().toString()
                , "Company", ""
                , "Name", user.getCompanyName()
                , "Address", user.getCompanyAddress()
                , "Phone number", user.getCompanyPhone()
                , "Contact person", ""
                , "Lastname", user.getLastName()
                , "Firstname", user.getFirstName()
                , "Seniority", String.valueOf(user.giveSeniority())
        );

        addDetailsToGridDetails(details);
    }

    private void addDetailsToGridDetails(Map<String, String> details){
	    int i = 0;
	    for (String key : details.keySet()){
            gridDetails.add(new Text(key), 0, i);
            gridDetails.add(new Text(details.get(key)), 1, i);
            i++;
        }

    }
}
