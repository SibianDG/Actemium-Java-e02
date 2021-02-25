package gui;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import gui.viewModels.UserViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class DetailsPanelController extends GridPane implements InvalidationListener {

    private final UserViewModel userViewModel;

    @FXML
    private Text txtDetailsTitle;

    @FXML
    private GridPane gridDetails;

    @FXML
    private Button btnModify;
	
	public DetailsPanelController(UserViewModel userviewModel) {
        this.userViewModel = userviewModel;
        userViewModel.addListener(this);

		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
        this.setVisible(false);

        gridDetails.setHgap(25);
        gridDetails.setVgap(25);

    }

    @FXML
    void btnModifyOnAction(ActionEvent event) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not yet implemented: modify button");
	    alert.setHeaderText("Not yet implemented");
	    alert.showAndWait();
    }

    @Override
    public void invalidated(Observable observable) {
        this.setVisible(true);


        gridDetails.getChildren().clear();

        addDetailsToGridDetails(userViewModel.getDetails());
        
        txtDetailsTitle.setText("Details of "+userViewModel.getNameOfSelectedUser());

    }

    /*public void fillGridPaneEmployee() {
	    Employee user = (Employee) userViewModel.getSelectedUser();

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
        txtDetails.setText("Details of "+ user.getUsername());

    }

    public void fillGridPaneCustomer(Map<String, String> details) {
        Customer user = (Customer) userViewModel.getSelectedUser();
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

        txtDetails.setText("Details of "+ user.getUsername());

    }

     */

    private void addDetailsToGridDetails(Map<String, String> details){
	    int i = 0;
	    // Using LinkedHashSet so the order of the map values doesn't change
	    Set<String> keys = new LinkedHashSet<String>(details.keySet());
	    for (String key : keys) {
	        Text header = new Text(key);
	        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	        header.setFill(Color.rgb(29, 61, 120));

	        Text detail = new Text(details.get(key));
	        detail.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
	        detail.setFill(Color.gray(.3));
	        
            gridDetails.add(header, 0, i);
            gridDetails.add(detail, 1, i);
            i++;
        }

    }
}
