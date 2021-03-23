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

import domain.ContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.EmployeeRole;
import domain.enums.Timestamp;
import domain.enums.UserStatus;
import gui.DashboardFrameController;
import gui.GUIEnum;
import gui.viewModels.ContractTypeViewModel;
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


public class ContractTypeTableViewPanelController<T,E> extends TableViewPanelController {
	
	private final Map<String, Function<T, Property<E>>> propertyMap = new LinkedHashMap<>();

	private ObservableList<T> mainData;
	
	public ContractTypeTableViewPanelController(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState, EmployeeRole employeeRole) {
		super(dashboardFrameController, viewModel, currentState, employeeRole);				
				
		this.mainData = (ObservableList<T>) ((ContractTypeViewModel) viewModel).giveContractTypes();
		this.tableViewData = new FilteredList<>(mainData);
		propertyMap.put(LanguageResource.getString("name"), item -> (Property<E>)((ContractType) item).contractTypeNameProperty());
		propertyMap.put(LanguageResource.getString("timestamp"), item -> (Property<E>)((ContractType) item).contractTypestampProperty());
		propertyMap.put(LanguageResource.getString("status"), item -> (Property<E>)((ContractType) item).contractTypeStatusProperty());
		btnAdd.setText(String.format("%s %s", LanguageResource.getString("add"), currentState.toString().toLowerCase()));
		
		initializeFilters();
		initializeTableViewSub();
	}
	
	@FXML
	void addOnMouseClicked(MouseEvent event) {
		if (alertChangesOnTabelView()) {
			((ContractTypeViewModel) viewModel).setCurrentState(GUIEnum.CONTRACTTYPE);
			((ContractTypeViewModel) viewModel).setSelectedContractType(null);		
		}
	}	
	
	protected void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.CONTRACTTYPE, new ArrayList<>(Arrays.asList(LanguageResource.getString("name"), Timestamp.WORKINGHOURS, ContractTypeStatus.ACTIVE)));
		
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
		    case "ContractTypeStatus" -> {
		    	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_status").toUpperCase()));
				Arrays.asList(ContractTypeStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "ContractTypeStatus";
		    }
		    case "Timestamp" -> {
		    	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_timestamp").toUpperCase()));
				Arrays.asList(Timestamp.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "Timestamp";
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
	        case "ContractTypeStatus" -> c.getSelectionModel().select("SELECT STATUS");
	        case "Timestamp" -> c.getSelectionModel().select(LanguageResource.getString("select_timestamp").toUpperCase());
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
					
					ArrayList<ContractTypeStatus> contractTypeStatusArrayList = new ArrayList<>(Arrays.asList(ContractTypeStatus.values()));
					List<String> contractTypeStatusStringArray = contractTypeStatusArrayList.stream().map(ContractTypeStatus::toString).collect(Collectors.toList());
					
					ArrayList<Timestamp> timestampArrayList = new ArrayList<>(Arrays.asList(Timestamp.values()));
					List<String> timestampStringArray = timestampArrayList.stream().map(Timestamp::toString).collect(Collectors.toList());					
					
					String selectedItem = comboBox.getSelectionModel().getSelectedItem().toString();

					if (contractTypeStatusStringArray.contains(selectedItem)) {
						predicates.add(giveFilterPredicate("ContractTypeStatus", selectedItem.toLowerCase()));
					} else if (timestampStringArray.contains(selectedItem)){
						predicates.add(giveFilterPredicate("Timestamp", selectedItem.toLowerCase()));						
					}
				}
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
			if (alertChangesOnTabelView()) {
				T data = (T) tableView.getSelectionModel().selectedItemProperty().get();				
				((ContractTypeViewModel) viewModel).setSelectedContractType((ContractType) data);
			}
		});
	}

	protected Predicate giveFilterPredicate(String fieldName, String filterText){
		fieldName = fieldName.toLowerCase();			
			if (fieldName.length() > 0 && !filterText.toLowerCase().contains(LanguageResource.getString("select"))){
				Predicate<ContractType> newPredicate;
				if (fieldName.equalsIgnoreCase(LanguageResource.getString("name"))){
					newPredicate = e -> e.getName().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("timestamp")) || fieldName.equalsIgnoreCase("timestamp")){
					newPredicate = e -> e.getTimestampAsString().toLowerCase().equals(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("select_status")) || fieldName.equalsIgnoreCase("contracttypestatus")){
					newPredicate = e -> e.getStatusAsString().toLowerCase().equals(filterText);
				} else {
					throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
				}
				return newPredicate;				
			}		
		return null;
	}
		
}
