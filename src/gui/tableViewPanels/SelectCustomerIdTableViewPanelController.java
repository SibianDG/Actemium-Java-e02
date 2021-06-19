package gui.tableViewPanels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import domain.Customer;
import domain.User;
import domain.enums.EmployeeRole;
import gui.DashboardFrameController;
import gui.GUIEnum;
import gui.detailPanels.TicketDetailsPanelController;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import languages.LanguageResource;


public class SelectCustomerIdTableViewPanelController<T,E> extends TableViewPanelController {
	
	private final Map<String, Function<T, Property<E>>> propertyMap = new LinkedHashMap<>();
	
	private TextField customerIdTextField;

	private ObservableList<T> mainData;
	
	public SelectCustomerIdTableViewPanelController(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState, EmployeeRole employeeRole) {
		super(dashboardFrameController, viewModel, currentState, employeeRole);			
		
		this.mainData = (ObservableList<T>) ((UserViewModel) viewModel).giveCustomers();
		this.tableViewData = new FilteredList<>(mainData);
		propertyMap.put(LanguageResource.getString("company"), item -> (Property<E>)((Customer)item).giveCompany().nameProperty());
		propertyMap.put(LanguageResource.getString("customer_ID"), item -> (Property<E>)((Customer)item).customerIdProperty());
		propertyMap.put(LanguageResource.getString("firstname"), item -> (Property<E>)((Customer)item).firstNameProperty());
		propertyMap.put(LanguageResource.getString("lastname"), item -> (Property<E>)((Customer)item).lastNameProperty());
//		btnAdd.setText(String.format("%s %s", LanguageResource.getString("add"), currentState.toString().toLowerCase()));			
		btnAdd.setText("Select Customer");			

		initializeFilters();
		initializeTableViewSub();
	}
		
	public void setCustomerIdTextField(TextField customerIdTextField) {
		this.customerIdTextField = customerIdTextField;
	}
	
	@FXML
	void addOnMouseClicked(MouseEvent event) {
		Customer selectedCustomer = (Customer) tableView.getSelectionModel().selectedItemProperty().get();
		customerIdTextField.setText(Integer.toString(selectedCustomer.getCustomerNr()));
		Stage stage = (Stage) this.getScene().getWindow();
		stage.close();
	}	
	
	protected void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.CUSTOMER, new ArrayList<>(Arrays.asList(LanguageResource.getString("company"), LanguageResource.getString("customer_ID"), 
				LanguageResource.getString("firstname"), LanguageResource.getString("lastname"))));
		
		filterMap.get(currentState).forEach(o -> hboxFilterSection.getChildren().add(createFilterNode(o)));
	}

	protected Node createFilterNode(Object o) {
		TextField filter = createTextFieldFilter((String) o);
		filter.setOnKeyTyped(event -> {
			checkFilters();
		});
		return filter;
	}
	
	@Override
	protected ComboBox makeComboBox(Object o) {
		return null;
	}

	protected void checkFilters(){		
		List<Predicate> predicates = new ArrayList<>();

		hboxFilterSection.getChildren().forEach(object -> {
			TextField textField = (TextField) object;
			if (textField.getText().trim().length() > 0){
				predicates.add(giveFilterPredicate(textField.getPromptText(), textField.getText().trim().toLowerCase()));
			}
		});
		// Reset all filters
		tableViewData.setPredicate(p -> true);
		// Create one combined predicate by iterating over the list
		predicates.forEach(this::setPredicateForFilteredList);
	}

	protected void initializeTableViewSub() {
		propertyMap.forEach((key, prop) -> {
			TableColumn<T, E> c = createColumn(key, prop);
			tableView.getColumns().add(c);
		});

		initializeTableViewSuper();
		
		tableView.setOnMouseClicked((MouseEvent m) -> {
				T data = (T) tableView.getSelectionModel().selectedItemProperty().get();				
				((UserViewModel) viewModel).setSelectedUser((User) data);	
		});
	}

	protected Predicate giveFilterPredicate(String fieldName, String filterText){
		fieldName = fieldName.toLowerCase();		
			if (fieldName.length() > 0 && !filterText.toLowerCase().contains(LanguageResource.getString("select"))){
				Predicate<Customer> newPredicate;

				if (fieldName.equalsIgnoreCase(LanguageResource.getString("company"))){
					newPredicate = e -> e.giveCompany().getName().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("customer_ID")) || fieldName.equalsIgnoreCase("customerid")){
					newPredicate = e -> Integer.toString(e.getCustomerNr()).toLowerCase().equals(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("firstname"))){
					newPredicate = e -> e.getFirstName().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("lastname"))) {
					newPredicate = e -> e.getLastName().toLowerCase().contains(filterText);
				} else {
					throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
				}
				return newPredicate;				
			}		
		return null;
	}
	
}
