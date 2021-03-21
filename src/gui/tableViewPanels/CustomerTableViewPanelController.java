package gui.tableViewPanels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import domain.Customer;
import domain.User;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import gui.DashboardFrameController;
import gui.GUIEnum;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import languages.LanguageResource;


public class CustomerTableViewPanelController<T,E> extends TableViewPanelController {
	
//  @FXML
//  private TableView<T> tableView;
	
	private Map<String, Function<T, Property<E>>> propertyMap = new LinkedHashMap<>();

	private ObservableList<T> mainData;
//	private FilteredList<T> tableViewData;
//	private SortedList<T> tableViewDataSorted;
	
	public CustomerTableViewPanelController(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState, EmployeeRole employeeRole) {
		super(dashboardFrameController, viewModel, currentState, employeeRole);			
		
		this.mainData = (ObservableList<T>) ((UserViewModel) viewModel).giveCustomers();
		this.tableViewData = new FilteredList<>(mainData);
//				tableViewData.sorted((Comparator<T>) Comparator.comparing(c -> ((Customer) c).giveCompany().getName().toLowerCase()).thenComparing(c -> ((User) c).getFirstName().toLowerCase()).thenComparing(c -> ((User) c).getLastName().toLowerCase()));
		propertyMap.put(LanguageResource.getString("company"), item -> (Property<E>)((Customer)item).giveCompany().nameProperty());
		propertyMap.put(LanguageResource.getString("status"), item -> (Property<E>)((Customer)item).statusProperty());
		propertyMap.put(LanguageResource.getString("firstname"), item -> (Property<E>)((Customer)item).firstNameProperty());
		propertyMap.put(LanguageResource.getString("lastname"), item -> (Property<E>)((Customer)item).lastNameProperty());
//				propertyMap.put("Username", item -> ((Customer)item).usernameProperty());
		btnAdd.setText(String.format("%s %s", LanguageResource.getString("add"), currentState.toString().toLowerCase()));			

		initializeFilters();
		initializeTableView();
	}
	
	@FXML
	void addOnMouseClicked(MouseEvent event) {
		if (alertChangesOnTabelView()) {		
			((UserViewModel) viewModel).setCurrentState(GUIEnum.CUSTOMER);
			((UserViewModel) viewModel).setSelectedUser(null);			
		}
	}	
	
	private void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.CUSTOMER, new ArrayList<>(Arrays.asList(LanguageResource.getString("company"), UserStatus.ACTIVE, LanguageResource.getString("firstname"), LanguageResource.getString("lastname"))));
		
		filterMap.get(currentState).forEach(o -> hboxFilterSection.getChildren().add(createElementDetailGridpane(o)));
	}

	private Node createElementDetailGridpane(Object o) {

		if (o instanceof String) {
			String string = (String) o;
			TextField filter = new TextField();
			filter.setPromptText(string);
			filter.setPrefWidth(145);
			filter.setFont(Font.font("Arial", 14));
			filter.setOnKeyTyped(event -> {
				checkFilters();
			});
			return filter;
		} else if (o instanceof Enum) {
			return makeComboBox(o);
		}
		return null;
	}

	private ComboBox makeComboBox(Object o){
		String itemText;
		ArrayList<String> stringArrayList;

		ObservableList list;
		
	    switch(o.getClass().getSimpleName()) {
	        case "UserStatus" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_status").toUpperCase()));
				Arrays.asList(UserStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "UserStatus";
	        }
	        case "EmployeeRole" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_role").toUpperCase()));
				Arrays.asList(EmployeeRole.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "EmployeeRole";
	        }
	        default -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_status").toUpperCase()));
				Arrays.asList(UserStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "Status";
	        }
	    } 	
	    
		list = FXCollections.observableList(stringArrayList);
		ComboBox c = new ComboBox(list);
		
		switch(itemText) {
	        case "UserStatus" -> c.getSelectionModel().select("SELECT STATUS");
	        case "EmployeeRole" -> c.getSelectionModel().select(LanguageResource.getString("select_status").toUpperCase());	        
	        default -> c.getSelectionModel().select(LanguageResource.getString("select").toUpperCase());
	    } 
		//TODO e.g. select technician and admin at the same time
//		c.getSelectionModel().select(SelectionMode.MULTIPLE);
		c.valueProperty().addListener(e -> {
			checkFilters();
		});
		return c;
	}

	private void checkFilters(){		
		List<Predicate> predicates = new ArrayList<>();

		hboxFilterSection.getChildren().forEach(object -> {
			if (object instanceof TextField) {
				TextField textField = (TextField) object;
				if (textField.getText().trim().length() > 0){
					predicates.add(giveFilterPredicate(textField.getPromptText(), textField.getText().trim().toLowerCase()));
				}
			} else if (object instanceof ComboBox) {
				ComboBox comboBox = (ComboBox) object;

				if (comboBox.getSelectionModel().getSelectedItem() != null &&  !comboBox.getSelectionModel().getSelectedItem().toString().contains("SELECT")) {

					ArrayList<UserStatus> userStatusArrayList = new ArrayList<>(Arrays.asList(UserStatus.values()));
					List<String> userStatusStringArray = userStatusArrayList.stream().map(UserStatus::toString).collect(Collectors.toList());
					
					ArrayList<EmployeeRole> employeeRoleArrayList = new ArrayList<>(Arrays.asList(EmployeeRole.values()));
					List<String> employeeRoleStringArray = employeeRoleArrayList.stream().map(EmployeeRole::toString).collect(Collectors.toList());					

					String selectedItem = comboBox.getSelectionModel().getSelectedItem().toString();

					
					if (userStatusStringArray.contains(selectedItem)){
						predicates.add(giveFilterPredicate("UserStatus", selectedItem.toLowerCase()));
					} else if (employeeRoleStringArray.contains(selectedItem)){
						predicates.add(giveFilterPredicate("EmployeeRole", selectedItem.toLowerCase()));
					} 
				}
			}
		});
		// Reset all filters
		tableViewData.setPredicate(p -> true);
		// Create one combined predicate by iterating over the list
		predicates.forEach(p -> setPredicateForFilteredList(p));
	}

	private void initializeTableView() {
		propertyMap.forEach((key, prop) -> {
			TableColumn<T, E> c = createColumn(key, prop);
			tableView.getColumns().add(c);
		});

		tableViewDataSorted = tableViewData.sorted();
		tableViewDataSorted.comparatorProperty().bind(tableView.comparatorProperty());
		
		tableView.setItems(tableViewDataSorted);
		
		tableView.setOnMouseClicked((MouseEvent m) -> {
			if (alertChangesOnTabelView()) {
				T data = (T) tableView.getSelectionModel().selectedItemProperty().get();				
				((UserViewModel) viewModel).setSelectedUser((User) data);				
			}
		});

		/*tableView.setOnKeyPressed( keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				viewModel.delete();
			}
		});*/
	}

	private Predicate giveFilterPredicate(String fieldName, String filterText){
		fieldName = fieldName.toLowerCase();		
			if (fieldName.length() > 0 && !filterText.toLowerCase().contains(LanguageResource.getString("select"))){
				Predicate<Customer> newPredicate;

				if (fieldName.equalsIgnoreCase(LanguageResource.getString("company"))){
					newPredicate = e -> e.giveCompany().getName().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("select_status")) || fieldName.equalsIgnoreCase("userstatus")){
					newPredicate = e -> e.getStatusAsString().toLowerCase().equals(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("firstname"))){
					newPredicate = e -> e.getFirstName().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("lastname"))) {
					newPredicate = e -> e.getLastName().toLowerCase().contains(filterText);
				} else {
					throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
				}

				//switch (fieldName) {
				//	case "company" -> newPredicate = e -> e.giveCompany().getName().toLowerCase().contains(filterText);
				//	case "userstatus" -> newPredicate = e -> e.getStatusAsString().toLowerCase().equals(filterText);
				//	case "firstname" -> newPredicate = e -> e.getFirstName().toLowerCase().contains(filterText);
				//	case "lastname" -> newPredicate = e -> e.getLastName().toLowerCase().contains(filterText);
				//	default -> throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
				//}
				return newPredicate;				
			}		
		return null;
	}
	
}
