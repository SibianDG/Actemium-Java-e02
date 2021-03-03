package gui;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import domain.Customer;
import domain.Employee;
import domain.EmployeeRole;
import domain.UserModel;
import domain.UserStatus;
import gui.viewModels.UserViewModel;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

	@FXML
    private Button btnAdd;

    @FXML
    private Text txtFilter;

	@FXML
	private HBox hboxFilterSection;
	
    @FXML
    private TableView<T> tableView;
    
	private Map<String, Function<T, Property<String>>> propertyMap = new LinkedHashMap<>();
	
	private FilteredList<T> tableViewData;
	
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
		
		switch(currentState) {
		case EMPLOYEE -> {
					this.tableViewData = new FilteredList<>((ObservableList<T>) userViewModel.getEmployees(), p -> true);

					propertyMap.put("Firstname", item -> ((Employee)item).firstNameProperty());
					propertyMap.put("Lastname", item -> ((Employee)item).lastNameProperty());
					propertyMap.put("Username", item -> ((Employee)item).usernameProperty());
					propertyMap.put("Status", item -> ((Employee)item).statusProperty());
					propertyMap.put("Role", item -> ((Employee)item).roleProperty());
				}
		case CUSTOMER -> {
					this.tableViewData = new FilteredList<>((ObservableList<T>) userViewModel.getCustomers(), p -> true);
					propertyMap.put("Firstname", item -> ((Customer)item).firstNameProperty());
					propertyMap.put("Lastname", item -> ((Customer)item).lastNameProperty());
					propertyMap.put("Username", item -> ((Customer)item).usernameProperty());
					propertyMap.put("Status", item -> ((Customer)item).statusProperty());
					propertyMap.put("Company", item -> ((Customer)item).getCompany().nameProperty());
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
			filter.setOnKeyTyped(event -> {
				checkFilters();
				System.out.println(((TextField) event.getSource()).getText().trim().length());
			});
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

		hboxFilterSection.getChildren().forEach(object -> {
			if (object instanceof TextField) {
				TextField textField = (TextField) object;
				if (textField.getText().trim().length() > 0)
					filter(textField.getPromptText(), textField.getText().trim().toLowerCase());
				else
					tableViewData.setPredicate(p -> true);
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
					} else {
						tableViewData.setPredicate(p -> true);
					}
				}
			}

			//TODO: shouldn't it automatically update with the ObserverableList?
			/*tvEmployees.setItems(employees);
			tvCustomers.setItems(customers);*/
		});
	}

	private void initializeTableView() {		
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
					case "Name" -> /*FXCollections.observableArrayList(
							 list.stream()
									.filter(u -> ((Employee) u).getFirstName().toLowerCase().contains(filterText) || ((Employee) u).getLastName().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u -> ((Employee) u).getFirstName().toLowerCase().contains(filterText) || ((Employee) u).getLastName().toLowerCase().contains(filterText));

					case "Username" -> /*FXCollections.observableArrayList(
							list.stream()
									.filter(u -> ((Employee) u).getUsername().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u-> ((Employee) u).getUsername().toLowerCase().contains(filterText));

					case "Role" -> /*FXCollections.observableArrayList(
							list.stream().filter(u -> ((Employee) u).getRole().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u -> ((Employee) u).getRole().toLowerCase().contains(filterText));
					default -> /*FXCollections.observableArrayList(
							list.stream()
									.filter(u -> ((Employee) u).getStatus().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u-> ((Employee) u).getStatus().toLowerCase().contains(filterText));

				 }
			}
		} else {
			if (fieldName.length() > 0 && !filterText.contains("select")){
				switch (fieldName) {
					case "Name" -> /*FXCollections.observableArrayList(
							list.stream()
									.filter(u -> ((Customer) u).getFirstName().toLowerCase().contains(filterText) || ((Customer) u).getLastName().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u-> ((Customer) u).getFirstName().toLowerCase().contains(filterText) || ((Customer) u).getLastName().toLowerCase().contains(filterText));
					case "Username" -> /*FXCollections.observableArrayList(
							list.stream()
									.filter(u -> ((Customer) u).getUsername().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u-> ((Customer) u).getUsername().toLowerCase().contains(filterText));
					case "Company" -> /*FXCollections.observableArrayList(
							list.stream()
									.filter(u -> ((Customer) u).getCompany().getName().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u-> ((Customer) u).getCompany().getName().toLowerCase().contains(filterText));
					default -> /*FXCollections.observableArrayList(
							list.stream()
									.filter(u -> ((Customer) u).getStatus().toLowerCase().contains(filterText))
									.collect(Collectors.toList()));*/
							tableViewData.setPredicate(u-> ((Customer) u).getStatus().toLowerCase().contains(filterText));
				}
			}
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