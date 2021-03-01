package gui;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import domain.EmployeeRole;
import domain.UserStatus;
import gui.viewModels.UserViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class DetailsPanelController extends GridPane implements InvalidationListener {

    private final UserViewModel userViewModel;
    private boolean modifying = false;

    @FXML
    private Text txtDetailsTitle;

    @FXML
    private GridPane gridDetails;

    @FXML
    private Button btnModify;

    @FXML
    private Text txtErrorMessage;
	
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

        gridDetails.setHgap(10);
        gridDetails.setVgap(10);
        txtDetailsTitle.setText("No user is selected");
        btnModify.setVisible(false);
        txtErrorMessage.setVisible(false);
    }

    @FXML
    void btnModifyOnAction(ActionEvent event) {

	    try {

            if (!modifying) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Modify Not yet implemented: modify button");
                alert.setHeaderText("Nothing is changed");
                alert.showAndWait();
            } else {
                userViewModel.modifyEmployee(
                        getTextFromGridItem(8)
                        , getTextFromGridItem(1)
                        , getTextFromGridItem(2)
                        , getTextFromGridItem(3)
                        , getTextFromGridItem(4)
                        , getTextFromGridItem(5)
                        , EmployeeRole.valueOf(getTextFromGridItem(7))
                        , UserStatus.valueOf(getTextFromGridItem(8))
                );
            }

            setDetailOnModifying();

        //TODO: handle the correct error messages, not just all
        } catch (Exception e){
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private String getTextFromGridItem(int i){
        System.out.println( ((TextField) gridDetails.getChildren().get(2*i+1)).getText());
        return ((TextField) gridDetails.getChildren().get(2*i+1)).getText();
    }

    @Override
    public void invalidated(Observable observable) {
	    try {
            setDetailOnModifying();
        } catch (NullPointerException e){
	        modifying = false;
	        setupPaneNewUser();
        }
    }

    private void setDetailOnModifying(){
        gridDetails.getChildren().clear();

        addDetailsToGridDetails(userViewModel.getDetails());
        btnModify.setVisible(true);
        txtErrorMessage.setVisible(false);

        txtDetailsTitle.setText("Details of "+userViewModel.getNameOfSelectedUser());
    }

    private void setupPaneNewUser(){
	    modifying = false;
        ArrayList<String> fields = userViewModel.getDetailsNewEmployee();
        txtDetailsTitle.setText("Add new user");
        btnModify.setText("Add new user");
        btnModify.setVisible(true);
        addItemsToGridNewUser(fields);
    }


    private void addItemsToGridNewUser(ArrayList<String> fields){
        gridDetails.getChildren().clear();
        gridDetails.addColumn(0);
        gridDetails.addColumn(1);

        Map<Integer, String> randomValues = Map.of(
                0, "Username9999"
                , 1, "FirstNameeee"
                , 2, "LastNameeee"
                , 3, "Stationstraat 99"
                , 4, "test@gmail.com"
                , 5, "094812384"
                , 6, EmployeeRole.SUPPORT_MANAGER.toString()

        );

        for (int i = 0; i < fields.size(); i++) {
            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(fields.get(i)), 0, i);

            TextField textField = new TextField(randomValues.get(i));
            textField.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            gridDetails.add(textField, 1, i);
        }
    }

    private Label makeNewLabel(String text){
        Label label = new Label(text+":");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label.setTextFill(Color.rgb(29, 61, 120));
        return label;
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

    private void addDetailsToGridDetails(Map<String, Object> details){
	    int i = 0;
	    // Using LinkedHashSet so the order of the map values doesn't change
	    Set<String> keys = new LinkedHashSet<String>(details.keySet());
	    for (String key : keys) {
	        Label label = makeNewLabel(key);

	        Node detail = createElementDetailGridpane(details.get(key));
            gridDetails.add(label, 0, i);
            gridDetails.add(detail, 1, i);
            i++;
        }

    }

    private Node createElementDetailGridpane(Object o) {

        if (o instanceof String) {
            TextField detail = new TextField((String) o);
            detail.textProperty().addListener((observable, oldValue, newValue) -> {
                modifying = true;
                System.out.println("textfield changed from " + oldValue + " to " + newValue);
            });
            //Text detailText = new Text(details.get(key));
            detail.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
            return detail;
        } else if (o instanceof Enum) {
            ObservableList list;
            if (Arrays.stream(UserStatus.values()).anyMatch(e -> e == o)) {
                list = FXCollections.observableList(Arrays.asList(UserStatus.values()));
            } else {
                list = FXCollections.observableList(Arrays.asList(EmployeeRole.values()));
            }
            ComboBox c = new ComboBox(list);
            c.getSelectionModel().select(o);
            return c;
        }
        return null;
    }
}
