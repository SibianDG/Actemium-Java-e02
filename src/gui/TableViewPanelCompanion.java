package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import domain.Customer;
import domain.Employee;
import domain.EmployeeRole;
import domain.UserModel;
import domain.UserStatus;
import gui.viewModels.UserViewModel;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class TableViewPanelCompanion<T> extends GridPane {

	//private UserFacade userFacade;
	private final DashboardFrameController dashboardFrameController;
	//private String user;
	private final UserViewModel userViewModel;
	private GUIEnum currentState;

	/*ObservableList<Employee> employees;
	ObservableList<Customer> customers;*/

	@FXML
    private Button btnAdd;

    @FXML
    private Text txtFilter;

	@FXML
	private HBox hboxFilterSection;
	
	@FXML
	private TableView<Employee> tvEmployees;
	
    @FXML
    private TableView<Customer> tvCustomers;
    
    
    
    @FXML
    private TableView<T> tableView;
    
	private Map<String, Function<T, Property<String>>> propertyMap;
	
	ObservableList<T> tableViewData;
	
	public TableViewPanelCompanion(DashboardFrameController dashboardFrameController, UserViewModel userviewModel, GUIEnum currentState) {
		this.dashboardFrameController = dashboardFrameController;
		this.userViewModel = userviewModel;
		this.currentState = currentState;

		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableViewPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		tvCustomers.setVisible(false);
		
		switch(currentState) {
		case EMPLOYEE -> {
					tableViewData = (ObservableList<T>) userViewModel.getEmployees();
					propertyMap.put("Firstname", cellData -> ((ObservableValue<Employee>) cellData).getValue().firstNameProperty());
					propertyMap.put("Lastname", cellData -> ((ObservableValue<Employee>) cellData).getValue().lastNameProperty());
					propertyMap.put("Username", cellData -> ((ObservableValue<Employee>) cellData).getValue().usernameProperty());
					propertyMap.put("Status", cellData -> ((ObservableValue<Employee>) cellData).getValue().statusProperty());
					propertyMap.put("Role", cellData -> ((ObservableValue<Employee>) cellData).getValue().roleProperty());
				}
		case CUSTOMER -> {
					tableViewData = (ObservableList<T>) userViewModel.getCustomers();
					propertyMap.put("Firstname", cellData -> ((ObservableValue<Customer>) cellData).getValue().firstNameProperty());
					propertyMap.put("Lastname", cellData -> ((ObservableValue<Customer>) cellData).getValue().lastNameProperty());
					propertyMap.put("Username", cellData -> ((ObservableValue<Customer>) cellData).getValue().usernameProperty());
					propertyMap.put("Status", cellData -> ((ObservableValue<Customer>) cellData).getValue().statusProperty());
					propertyMap.put("Company", cellData -> ((ObservableValue<Customer>) cellData).getValue().getCompany().nameProperty());
		}
		//case TICKET ->
		}
		
		initializeFilters();
		
		initializeTableView();
	}
	
	private <T> TableColumn<T, String> createColumn(String title, Function<T, Property<String>> prop) {
		TableColumn<T, String> column = new TableColumn<>(title);
		column.setCellValueFactory(cellData -> prop.apply(cellData.getValue()));
		return column;	
	}
	
	

	private void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.EMPLOYEE, new ArrayList<>(Arrays.asList("Name", "Username", UserStatus.ACTIVE, EmployeeRole.ADMINISTRATOR)));
		filterMap.put(GUIEnum.CUSTOMER, new ArrayList<>(Arrays.asList("Name", "Username", UserStatus.ACTIVE, "Company")));

		filterMap.get(currentState).forEach(o -> hboxFilterSection.getChildren().add(createElementDetailGridpane(o)));
	}

	private Node createElementDetailGridpane(Object o) {

		if (o instanceof String) {
			String string = (String) o;
			TextField filter = new TextField();
			filter.setPromptText(string);
			filter.setPrefWidth(150);
			filter.setFont(Font.font("Arial", 14));
			filter.setOnKeyTyped(event -> checkFilters());
			return filter;
		} else if (o instanceof Enum) {
			return makeComboBox(o);
		}
		return null;
	}

	private ComboBox makeComboBox(Object o){
		String itemText;
		ArrayList<String> stringArrayListist;

		ObservableList list;
		if (Arrays.stream(UserStatus.values()).anyMatch(e -> e == o)) {
			stringArrayListist = new ArrayList<>(Collections.singleton("SELECT STATUS"));
			Arrays.asList(UserStatus.values()).forEach(string -> stringArrayListist.add(string.toString()));
			itemText = "UserStatus";
		} else {
			stringArrayListist = new ArrayList<>(Collections.singleton("SELECT ROLE"));
			Arrays.asList(EmployeeRole.values()).forEach(string -> stringArrayListist.add(string.toString()));
			itemText = "Role";
		}
		list = FXCollections.observableList(stringArrayListist);
		ComboBox c = new ComboBox(list);
		if (itemText.equals("UserStatus")){
			c.getSelectionModel().select("SELECT STATUS");
		} else if (itemText.equals("Role")){
			c.getSelectionModel().select("SELECT ROLE");
		}
		c.valueProperty().addListener(e -> {
			checkFilters();
		});
		return c;
	}

	private void checkFilters(){
		/*if(currentState.equals(GUIEnum.EMPLOYEE)) {
			employees = userViewModel.getEmployees();
		} else {
			customers = userViewModel.getCustomers();
		}*/
		hboxFilterSection.getChildren().forEach(object -> {
			if (object instanceof TextField) {
				TextField textField = (TextField) object;
				if (textField.getText().trim().length() > 0)
					filter(textField.getPromptText(), textField.getText().trim().toLowerCase());
			} else if (object instanceof ComboBox) {
				ComboBox comboBox = (ComboBox) object;

				if (comboBox.getSelectionModel().getSelectedItem() != null &&  !comboBox.getSelectionModel().getSelectedItem().toString().contains("SELECT")) {
					ArrayList<EmployeeRole> employeeRoleArrayList = new ArrayList<>(Arrays.asList(EmployeeRole.values()));
					List<String> employeeRoleStringArray = employeeRoleArrayList.stream().map(EmployeeRole::toString).collect(Collectors.toList());

					ArrayList<UserStatus> userStatusArrayList = new ArrayList<>(Arrays.asList(UserStatus.values()));
					List<String> userStatusRoleStringArray = userStatusArrayList.stream().map(UserStatus::toString).collect(Collectors.toList());

					if (employeeRoleStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						filter("Role", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase());
					} else if (userStatusRoleStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						filter("Status", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase());
					}
				}

			}

			//TODO: shouldn't it automatically update with the ObserverableList?
			/*tvEmployees.setItems(employees);
			tvCustomers.setItems(customers);*/

		});
	}

	//TODO We could try 2 different TableView s
	// 1 for customers
		// should display companyName instead of username
	// 1 for employees
		// should display userRole instead of username
	private void initializeTableView() {
		/*if(currentState.equals(GUIEnum.EMPLOYEE)) {
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

			tvEmployees.setItems(employees);
			
			btnAdd.setText("Add Employee");
			
			tvEmployees.setOnMouseClicked((MouseEvent m) -> {
				UserModel user = tvEmployees.getSelectionModel().selectedItemProperty().get();
				if (user != null){
					userViewModel.setSelectedUser(user);
				}
			});
			
		} else {
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
			roleColumn.setCellValueFactory(cellData -> cellData.getValue().getCompany().nameProperty());

			ObservableList<Customer> customers = userViewModel.getCustomers();
			tvCustomers.setItems(customers);

			btnAdd.setText("Add Customer");
			
			tvCustomers.setOnMouseClicked((MouseEvent m) -> {
				UserModel user = tvCustomers.getSelectionModel().selectedItemProperty().get();
				if (user != null){
					userViewModel.setSelectedUser(user);
				}
			});
		}*/
		
		propertyMap.forEach((key, prop) -> {
			TableColumn<T, String> c = createColumn(key, prop);
			tableView.getColumns().add(c);
		});
		
		tableView.setItems(tableViewData);
		
		tableView.setOnMouseClicked((MouseEvent m) -> {
			T data = tableView.getSelectionModel().selectedItemProperty().get();
			if (data != null & (data instanceof Employee || data instanceof Customer)){
				userViewModel.setSelectedUser((UserModel) data);
			}
		});
		
	}

	@FXML
	void addOnAction(ActionEvent event) {
		//TableView tableView;
		if(currentState.equals(GUIEnum.EMPLOYEE)) {
			userViewModel.setCurrentState(GUIEnum.EMPLOYEE);
			//tableView = tvEmployees;
		} else {
			//tableView = tvCustomers;
			userViewModel.setCurrentState(GUIEnum.CUSTOMER);
		}
		//tableView.getSelectionModel().clearSelection();
		userViewModel.setSelectedUser(null);
	}

	private void filter(String fieldName, String filterText){
		
		if(currentState.equals(GUIEnum.EMPLOYEE)) {
			if (fieldName.length() > 0 && !filterText.contains("select")){
				 switch (fieldName) {
					case "Name" -> {
						this.tableViewData = FXCollections.observableArrayList(
							 tableViewData.stream()
									.filter(u -> u.getFirstName().toLowerCase().contains(filterText) || u.getLastName().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));}
					case "Username" -> FXCollections.observableArrayList(
							tableViewData.stream()
									.filter(u -> u.getUsername().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));
					case "Role" -> FXCollections.observableArrayList(
							employees.stream()
							tableViewData	.filter(u -> u.getRole().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));
					default -> FXCollections.observableArrayList(
							tableViewData.stream()
									.filter(u -> u.getStatus().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));
				};
			} else {
				employees = userViewModel.getEmployees();
			}
			tableView.setItems(employees);
		} else {
			ObservableList<Customer> customers = userViewModel.getCustomers();
			if (fieldName.length() > 0 && !filterText.contains("select")){
				customers = switch (fieldName) {
					case "Name" -> FXCollections.observableArrayList(
							customers.stream()
									.filter(u -> u.getFirstName().toLowerCase().contains(filterText) || u.getLastName().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));
					case "Username" -> FXCollections.observableArrayList(
							customers.stream()
									.filter(u -> u.getUsername().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));
					case "Company" -> FXCollections.observableArrayList(
							customers.stream()
									.filter(u -> u.getCompany().getName().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));
					default -> FXCollections.observableArrayList(
							customers.stream()
									.filter(u -> u.getStatus().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));
				};
			} else {
				employees = userViewModel.getEmployees();
			}
			tableView.setItems(customers);
		}
	}

	/*@FXML
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


		

		
	}	 */
	
}