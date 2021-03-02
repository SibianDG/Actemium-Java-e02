package gui;

import domain.Employee;
import domain.EmployeeRole;
import domain.facades.UserFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ModifyPanelController extends GridPane {

	private final UserFacade domainController;
	private final DashboardFrameController dashboardController;
	private String user;


	@FXML
	private Button btnAdd;

	@FXML
	private GridPane gridContent;

	private List<Label> labels;
	private List<TextField> textFields;

	@FXML
	void btnAddOnAction(ActionEvent event) throws FileNotFoundException {
		System.out.println();
		//FIXME

		domainController.registerEmployee(
			getTextFromGridItem(0)
			, getTextFromGridItem(1)
			, getTextFromGridItem(2)
			, getTextFromGridItem(3)
			, getTextFromGridItem(4)
			, getTextFromGridItem(5)
			, getTextFromGridItem(6)
			, EmployeeRole.valueOf(getTextFromGridItem(7))
		);
		dashboardController.initializeDashboard();
	}

	/*private void modifyEmployee(){

		String username = domainController.getSelectedUser().getUsername();
		domainController.modifyEmployee(
				(Employee) domainController.getSelectedUser()
				, username
				, getTextFromGridItem(0)
				, getTextFromGridItem(1)
				, getTextFromGridItem(2)
				, getTextFromGridItem(3)
				, getTextFromGridItem(4)
				, getTextFromGridItem(5)
				, EmployeeRole.valueOf(getTextFromGridItem(6)
		);
	}

	 */

	private String getTextFromGridItem(int i){
		return ((TextField) gridContent.getChildren().get(2*i+1)).getText();
	}

	public ModifyPanelController(UserFacade domainController, DashboardFrameController dashboardController) {
		this.domainController = domainController;
		this.dashboardController = dashboardController;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPanel.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void initialize() {
		ArrayList<String> fields;
/*
		if (user.equals("employees")) {
			fields = new ArrayList<>(Arrays.asList("Name", "Firstname", "Lastname", "Email address", "Phone number", "Role"));
		} else {
			fields = new ArrayList<>(Arrays.asList("Name", "Firstname", "Lastname", "Email address", "Phone number", "Role"));
		}
 */
		fields = new ArrayList<>(Arrays.asList("Username", "Password", "Firstname", "Lastname", "Address", "Phone number","Email address", "Role"));
		addFiledsToGrid(fields);
		gridContent.minWidth(400);
		gridContent.minHeight(400);
		gridContent.setVgap(15);
		gridContent.setHgap(15);
		btnAdd.setText("Add");
	}

	private void addFiledsToGrid(ArrayList<String> fields){
		gridContent.addColumn(0);
		gridContent.addColumn(1);

		Map<String, String> randomValues = Map.of(
				"Username", "Username1"
				, "Password", "Passwd123&"
				, "Firstname", "Names"
				, "Lastname", "Names"
				, "Address", "Address"
				, "Email address", "test@test.com"
				, "Phone number", "091234567"
				, "Role", "SUPPORT_MANAGER"

		);

		for (int i = 0; i < fields.size(); i++) {
			gridContent.addRow(i);

			gridContent.add(makeNewLabel(fields.get(i)), 0, i);

			TextField textField = new TextField(randomValues.get(fields.get(i)));
			textField.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			gridContent.add(textField, 1, i);
		}
	}

	private Label makeNewLabel(String text){
		Label label = new Label(text+":");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		label.setTextFill(Color.rgb(29, 61, 120));
		return label;
	}
}
