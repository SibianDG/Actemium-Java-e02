package gui;

import java.io.IOException;
import java.util.ArrayList;

import domain.UserModel;
import domain.controllers.DomainController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class TableViewPanelController extends GridPane implements Observable {

	private DomainController domainController;
	private UserModel selectedUser;
	private String user;
	private ArrayList<InvalidationListener> listeners = new ArrayList<>();
	
    @FXML
    private Button btnAdd;

    @FXML
    private Text txtFilter;
	
	@FXML
	private TableView<UserModel> tvUsers;
	
	@FXML
	private TableColumn<UserModel, String> usernameCol;
	
	@FXML
	private TableColumn<UserModel, String> fistnameCol;
	
	@FXML
	private TableColumn<UserModel, String> lastnameCol;
	
	@FXML
	private TableColumn<UserModel, String> statusCol;
	
	public TableViewPanelController(DomainController domainController, String user) {
		this.domainController = domainController;
		this.user = user;
		System.out.println(user);
		
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

		TableColumn<UserModel, String> usernameColumn = new TableColumn<>("username");
		tvUsers.getColumns().add(usernameColumn);
		usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

		TableColumn<UserModel, String> statusColumn = new TableColumn<>("status");
		tvUsers.getColumns().add(statusColumn);
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

		ObservableList<UserModel> users;

		if (user.equals("employees")) {
			users = domainController.giveEmployeeList();
		} else {
			users = domainController.giveCustomerList();
		}

		tvUsers.setItems(users);

		tvUsers.setOnMouseClicked((MouseEvent m) -> {
			Object object = tvUsers.getSelectionModel().selectedItemProperty().get();
			setSelectedUser((UserModel) object);
			//System.out.println(selectedUser.getUsername());
		});
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
