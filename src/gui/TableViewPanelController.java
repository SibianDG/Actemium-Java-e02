package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import domain.UserModel;
import domain.facades.UserFacade;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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


public class TableViewPanelController extends GridPane implements Observable {

	private UserFacade domainController;
	private DashboardFrameController dashboardController;
	private UserModel selectedUser;
	private String user;
	private ObservableList<UserModel> users;

	private ArrayList<InvalidationListener> listeners = new ArrayList<>();
	
    @FXML
    private Button btnAdd;

    @FXML
    private Text txtFilter;

	@FXML
	private TextField txfFilterInput;
	
	@FXML
	private TableView<UserModel> tvUsers;
	
	public TableViewPanelController(UserFacade domainController, DashboardFrameController dashboardController ,String user) {
		this.domainController = domainController;
		this.dashboardController = dashboardController;
		this.user = user;
		
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

		if (user.equals("employees")) {
			btnAdd.setText("Add Employee");
			users = domainController.giveEmployeeList();
		} else {
			btnAdd.setText("Add Customer");
			users = domainController.giveCustomerList();
		}

		tvUsers.setItems(users);

		tvUsers.setOnMouseClicked((MouseEvent m) -> {
			UserModel user = tvUsers.getSelectionModel().selectedItemProperty().get();
			if (user != null){
				//TODO change it
				setSelectedUser(user);
				domainController.setSelectedUser(user);
				//System.out.println(selectedUser.getUsername());
			}
		});
	}

	@FXML
	void addOnAction(ActionEvent event) {
		dashboardController.setModifyPane();
	}

	@FXML
	void filterOnKeyTyped(KeyEvent event) {
		String input = txfFilterInput.getText().toLowerCase();

		if (user.equals("employees")) {
			users = domainController.giveEmployeeList();
		} else {
			users = domainController.giveCustomerList();
		}

		if (!input.isBlank()){
			users = FXCollections.observableArrayList(
					users.stream()
							.filter(u -> u.getUsername().toLowerCase().contains(input) || u.getFirstName().toLowerCase().contains(input) || u.getLastName().toLowerCase().contains(input))
							.collect(Collectors.toList())
			);
		}
		tvUsers.setItems(users);
	}

	public UserModel getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(UserModel selectedUser) {
		this.selectedUser = selectedUser;
		fireInvalidationEvent();
	}

	protected void fireInvalidationEvent() {
		for (InvalidationListener listener : listeners) {
			listener.invalidated(this);
		}
	}

	@Override
	public void addListener(InvalidationListener invalidationListener) {
		listeners.add(invalidationListener);
	}

	@Override
	public void removeListener(InvalidationListener invalidationListener) {
		listeners.remove(invalidationListener);
	}
}
