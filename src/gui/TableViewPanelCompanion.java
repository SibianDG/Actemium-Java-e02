package gui;

import java.io.IOException;
import java.util.stream.Collectors;

import domain.Customer;
import domain.Employee;
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

	//private UserFacade userFacade;
	private final DashboardFrameController dashboardFrameController;
	//private String user;
	private final UserViewModel userViewModel;
	
	private final boolean isManagingEmployees;

    @FXML
    private Button btnAdd;

    @FXML
    private Text txtFilter;

	@FXML
	private TextField txfFilterInput;
	
	@FXML
	private TableView<Employee> tvEmployees;
	
    @FXML
    private TableView<Customer> tvCustomers;
	
	public TableViewPanelCompanion(DashboardFrameController dashboardFrameController, UserViewModel userviewModel, boolean isManagingEmployees) {
		this.dashboardFrameController = dashboardFrameController;
		this.userViewModel = userviewModel;
		this.isManagingEmployees = isManagingEmployees;

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

	
	//TODO We could try 2 different TableView s
	// 1 for customers
		// should display companyName instead of username
	// 1 for employees
		// should display userRole instead of username
	private void initializeTableView() {



		System.out.println(userViewModel.getEmployees());
		System.out.println(userViewModel.getCustomers());


		if(isManagingEmployees) {
			System.out.println("TV EMPLOYEE");
			tvEmployees.setVisible(true);
			tvCustomers.setVisible(false);
			
			TableColumn<Employee, String> firstNameColumn = new TableColumn<>("Firstname");
			tvEmployees.getColumns().add(firstNameColumn);
			firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

			TableColumn<Employee, String> lastNameColumn = new TableColumn<>("Lastname");
			tvEmployees.getColumns().add(lastNameColumn);
			lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
			
			TableColumn<Employee, String> usernameColumn = new TableColumn<>("Username");
			tvEmployees.getColumns().add(usernameColumn);
			usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
			
			TableColumn<Employee, String> statusColumn = new TableColumn<>("Status");
			tvEmployees.getColumns().add(statusColumn);
			statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			TableColumn<Employee, String> roleColumn = new TableColumn<>("Role");
			tvEmployees.getColumns().add(roleColumn);
			roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
			
			ObservableList<Employee> employees = userViewModel.getEmployees();
			System.out.println(employees.size());
			
			tvEmployees.setItems(employees);
			
			btnAdd.setText("Add Employee");
			
			tvEmployees.setOnMouseClicked((MouseEvent m) -> {
				UserModel user = tvEmployees.getSelectionModel().selectedItemProperty().get();
				if (user != null){
					userViewModel.setSelectedUser(user);
				}
			});
			
		} else {
			System.out.println("TV CUSTOMER");
			tvCustomers.setVisible(true);
			tvEmployees.setVisible(false);
		
			TableColumn<Customer, String> firstNameColumn = new TableColumn<>("Firstname");
			tvCustomers.getColumns().add(firstNameColumn);
			firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

			TableColumn<Customer, String> lastNameColumn = new TableColumn<>("Lastname");
			tvCustomers.getColumns().add(lastNameColumn);
			lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

			TableColumn<Customer, String> usernameColumn = new TableColumn<>("Username");
			tvCustomers.getColumns().add(usernameColumn);
			usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
			
			TableColumn<Customer, String> statusColumn = new TableColumn<>("Status");
			tvCustomers.getColumns().add(statusColumn);
			statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
			
			TableColumn<Customer, String> roleColumn = new TableColumn<>("Company");
			tvCustomers.getColumns().add(roleColumn);
			//TODO nameProperty for name in Company
			roleColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

			ObservableList<Customer> customers = userViewModel.getCustomers();
			System.out.println(customers.size());
			tvCustomers.setItems(customers);

			btnAdd.setText("Add Customer");
			
			tvCustomers.setOnMouseClicked((MouseEvent m) -> {
				UserModel user = tvCustomers.getSelectionModel().selectedItemProperty().get();
				if (user != null){
					userViewModel.setSelectedUser(user);
				}
			});
		}
	}

	@FXML
	void addOnAction(ActionEvent event) {
		TableView tableView;
		if(isManagingEmployees) {
			tableView = tvEmployees;
		} else {
			tableView = tvCustomers;
		}
		tableView.getSelectionModel().clearSelection();
		userViewModel.setSelectedUser(null);
	}

	@FXML
	void filterOnKeyTyped(KeyEvent event) {
		String input = txfFilterInput.getText().toLowerCase();
		
		if(userViewModel.getEmployees() != null) {
			ObservableList<Employee> employees = userViewModel.getEmployees();
			
			if (!input.isBlank()){
				employees = FXCollections.observableArrayList(
						employees.stream()
								.filter(u -> u.getUsername().toLowerCase().contains(input) || u.getFirstName().toLowerCase().contains(input) || u.getLastName().toLowerCase().contains(input))
								.collect(Collectors.toList())
				);
			}
			tvEmployees.setItems(employees);
		} else {
			ObservableList<Customer> customers = userViewModel.getCustomers();
			
			if (!input.isBlank()){
				customers = FXCollections.observableArrayList(
						customers.stream()
								.filter(u -> u.getUsername().toLowerCase().contains(input) || u.getFirstName().toLowerCase().contains(input) || u.getLastName().toLowerCase().contains(input))
								.collect(Collectors.toList())
				);
			}
			tvCustomers.setItems(customers);
		}
		
		

		
	}
}