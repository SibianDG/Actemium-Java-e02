package gui;

import java.io.IOException;
import java.util.stream.Collectors;

import domain.UserModel;
import domain.facades.UserFacade;
import gui.viewModels.UserViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class TableViewPanelCompanion extends GridPane {

	private UserFacade domainController;
	private DashboardFrameController dashboardFrameController;
	//private String user;
	private UserViewModel userViewModel;

    @FXML
    private Button btnAdd;

    @FXML
    private Text txtFilter;

	@FXML
	private TextField txfFilterInput;
	
	@FXML
	private TableView<UserModel> tvUsers;
	
	public TableViewPanelCompanion(UserFacade domainController, DashboardFrameController dashboardFrameController, UserViewModel userviewModel) {
		this.domainController = domainController;
		this.dashboardFrameController = dashboardFrameController;
		this.userViewModel = userviewModel;

		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableViewPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		initializeTableView();
	}

	private void initializeTableView() {
		
		TableColumn<UserModel, String> usernameColumn = new TableColumn<>("Username");
		tvUsers.getColumns().add(usernameColumn);
		usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
		
		TableColumn<UserModel, String> firstNameColumn = new TableColumn<>("Firstname");
		tvUsers.getColumns().add(firstNameColumn);
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

		TableColumn<UserModel, String> lastNameColumn = new TableColumn<>("Lastname");
		tvUsers.getColumns().add(lastNameColumn);
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

		TableColumn<UserModel, String> statusColumn = new TableColumn<>("Status");
		tvUsers.getColumns().add(statusColumn);
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

		ObservableList<UserModel> users = userViewModel.getUserList();

		btnAdd.setText("Add "+users.get(0).getClass().getSimpleName().toLowerCase());

		tvUsers.setItems(users);

		tvUsers.setOnMouseClicked((MouseEvent m) -> {
			UserModel user = tvUsers.getSelectionModel().selectedItemProperty().get();
			if (user != null){
				userViewModel.setSelectedUser(user);
			}
		});
	}

	@FXML
	void addOnAction(ActionEvent event) {
		dashboardFrameController.setModifyPane();
	}

	@FXML
	void filterOnKeyTyped(KeyEvent event) {
		String input = txfFilterInput.getText().toLowerCase();

		ObservableList<UserModel> users = userViewModel.getUserList();

		if (!input.isBlank()){
			users = FXCollections.observableArrayList(
					users.stream()
							.filter(u -> u.getUsername().toLowerCase().contains(input) || u.getFirstName().toLowerCase().contains(input) || u.getLastName().toLowerCase().contains(input))
							.collect(Collectors.toList())
			);
		}
		tvUsers.setItems(users);
	}
}
