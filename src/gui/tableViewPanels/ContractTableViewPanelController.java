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

import domain.Contract;
import domain.enums.ContractStatus;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import gui.DashboardFrameController;
import gui.GUIEnum;
import gui.viewModels.ContractViewModel;
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
import languages.LanguageResource;


public class ContractTableViewPanelController<T,E> extends TableViewPanelController {
	
	private Map<String, Function<T, Property<E>>> propertyMap = new LinkedHashMap<>();

	private ObservableList<T> mainData;
	
	public ContractTableViewPanelController(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState, EmployeeRole employeeRole) {
		super(dashboardFrameController, viewModel, currentState, employeeRole);				
		
		this.mainData = (ObservableList<T>) ((ContractViewModel) viewModel).giveContracts();
		this.tableViewData = new FilteredList<>(mainData);
		propertyMap.put(LanguageResource.getString("ID"), item -> (Property<E>)((Contract) item).contractIdProperty()); // IntegerProperty
		propertyMap.put(LanguageResource.getString("company"), item -> (Property<E>)((Contract) item).giveCustomer().giveCompany().nameProperty());
		propertyMap.put(LanguageResource.getString("type"), item -> (Property<E>)((Contract) item).contractTypeNameProperty());
		propertyMap.put(LanguageResource.getString("status"), item -> (Property<E>)((Contract) item).contractStatusProperty());
		btnAdd.setText(String.format("%s %s", LanguageResource.getString("add"), currentState.toString().toLowerCase()));
			
		initializeFilters();
		initializeTableViewSub();
	}
		
	@FXML
	void addOnMouseClicked(MouseEvent event) {
		if (alertChangesOnTabelView()) {
			((ContractViewModel) viewModel).setCurrentState(GUIEnum.CONTRACT);
			((ContractViewModel) viewModel).setSelectedContract(null);
		}
	}
		
	protected void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.CONTRACT, new ArrayList<>(Arrays.asList(LanguageResource.getString("contractId"), LanguageResource.getString("company_name"), LanguageResource.getString("contract_type_name"), ContractStatus.CURRENT)));
		
		filterMap.get(currentState).forEach(o -> hboxFilterSection.getChildren().add(createFilterNode(o)));
	}

	protected Node createFilterNode(Object o) {
		if (o instanceof String) {
			TextField filter = createTextFieldFilter((String) o);
			filter.setOnKeyTyped(event -> {
				checkFilters();
			});
			return filter;
		} else if (o instanceof Enum) {
			return makeComboBox(o);
		}
		return null;
	}

	protected ComboBox makeComboBox(Object o){
		String itemText;
		ArrayList<String> stringArrayList;

		ObservableList list;
		
	    switch(o.getClass().getSimpleName()) {	        
		    case "ContractStatus" -> {
		    	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_status").toUpperCase()));
				Arrays.asList(ContractStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "ContractStatus";
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
	        case "ContractStatus" -> c.getSelectionModel().select("SELECT STATUS");	        
	        default -> c.getSelectionModel().select(LanguageResource.getString("select").toUpperCase());
	    } 
		//TODO e.g. select technician and admin at the same time
//		c.getSelectionModel().select(SelectionMode.MULTIPLE);
		c.valueProperty().addListener(e -> {
			checkFilters();
		});
		return c;
	}

	protected void checkFilters() {		
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
					
					ArrayList<ContractStatus> contractStatusArrayList = new ArrayList<>(Arrays.asList(ContractStatus.values()));
					List<String> contractStatusStringArray = contractStatusArrayList.stream().map(ContractStatus::toString).collect(Collectors.toList());
					
					String selectedItem = comboBox.getSelectionModel().getSelectedItem().toString();
					
					if (contractStatusStringArray.contains(selectedItem)) {
						predicates.add(giveFilterPredicate("ContractStatus", selectedItem.toLowerCase()));					
					}
				}
			}
		});
		// Reset all filters
		tableViewData.setPredicate(p -> true);
		// Create one combined predicate by iterating over the list
		predicates.forEach(p -> setPredicateForFilteredList(p));
	}

	protected void initializeTableViewSub() {
		propertyMap.forEach((key, prop) -> {
			TableColumn<T, E> c = createColumn(key, prop);
			tableView.getColumns().add(c);
		});

		initializeTableViewSuper();
		
		tableView.setOnMouseClicked((MouseEvent m) -> {
			if (alertChangesOnTabelView()) {
				T data = (T) tableView.getSelectionModel().selectedItemProperty().get();
				((ContractViewModel) viewModel).setSelectedContract((Contract) data);
			}
		});
	}

	protected Predicate giveFilterPredicate(String fieldName, String filterText){
		fieldName = fieldName.toLowerCase();		
			if (fieldName.length() > 0 && !filterText.toLowerCase().contains(LanguageResource.getString("select"))){
				Predicate<Contract> newPredicate;
				if (fieldName.equalsIgnoreCase("contract id")){
					newPredicate = e -> e.getContractIdString().equals(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("company_name")) || fieldName.equalsIgnoreCase("timestamp")){
					newPredicate = e -> e.giveCustomer().giveCompany().getName().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("contract_type_name")) || fieldName.equalsIgnoreCase("contract type name")){
					newPredicate = e -> e.giveContractType().getName().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("select_status")) || fieldName.equalsIgnoreCase("contractstatus")){
					newPredicate = e -> e.getStatusAsString().toLowerCase().equals(filterText);
				} else {
					throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
				}				
				return newPredicate;				
			}
		return null;
	}
	
}
