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

import domain.Ticket;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.enums.UserStatus;
import gui.DashboardFrameController;
import gui.GUIEnum;
import gui.viewModels.TicketViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import languages.LanguageResource;


public class TicketTableViewPanelController<T, E> extends TableViewPanelController {
    
	// T = Ticket
	// E = StringProperty or IntegerProperty
	private Map<String, Function<T, Property<E>>> propertyMap = new LinkedHashMap<>();

	private ObservableList<T> mainData;
	
	@SuppressWarnings("unchecked")
	public TicketTableViewPanelController(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState, EmployeeRole employeeRole) {
		super(dashboardFrameController, viewModel, currentState, employeeRole);
		
		if (TicketStatus.isOutstanding()) {
			this.mainData = (ObservableList<T>) ((TicketViewModel) viewModel).giveTicketsOutstanding();
			
			changeTicketPriorityButtons(this.mainData);
						
			this.mainData.addListener((ListChangeListener<T>) c -> {
				while(c.next()) {
					if(c.wasAdded()) {
						changeTicketPriorityButtons(c.getList());

					} else if(c.wasRemoved()) {
						changeTicketPriorityButtons(c.getList());
					}
				}
			});
			
			btnP1.setVisible(true);
			btnP2.setVisible(true);
			btnP3.setVisible(true);	
			
		} else {
			this.mainData = (ObservableList<T>) ((TicketViewModel) viewModel).giveTicketsResolved();
			btnAdd.setVisible(false);
		}				
		
		this.tableViewData = new FilteredList<>(mainData);
		propertyMap.put(LanguageResource.getString("ID"), item -> (Property<E>)((Ticket) item).ticketIdProperty()); // IntegerProperty
		propertyMap.put(LanguageResource.getString("type"), item -> (Property<E>)((Ticket) item).ticketTypeProperty());
		propertyMap.put(LanguageResource.getString("priority"), item -> (Property<E>)((Ticket) item).priorityProperty());
		propertyMap.put(LanguageResource.getString("title"), item -> (Property<E>)((Ticket) item).titleProperty());
		propertyMap.put(LanguageResource.getString("status"), item -> (Property<E>)((Ticket) item).statusProperty());
		btnAdd.setText(String.format("%s %s", LanguageResource.getString("add"), currentState.toString().toLowerCase()));
			
		initializeFilters();
		initializeTableViewSub();
	}
	
	private void changeTicketPriorityButtons(ObservableList<? extends T> observableList) {
		int amountOfP1Tickets, amountOfP2Tickets, amountOfP3Tickets;
		amountOfP1Tickets = observableList.stream().filter((t -> ((Ticket) t).getPriority() == TicketPriority.P1 )).collect(Collectors.toList()).size();
		amountOfP2Tickets = observableList.stream().filter((t -> ((Ticket) t).getPriority() == TicketPriority.P2 )).collect(Collectors.toList()).size();
		amountOfP3Tickets = observableList.stream().filter((t -> ((Ticket) t).getPriority() == TicketPriority.P3 )).collect(Collectors.toList()).size();

		btnP1.setText(String.valueOf(amountOfP1Tickets));		
		btnP2.setText(String.valueOf(amountOfP2Tickets));
		btnP3.setText(String.valueOf(amountOfP3Tickets));
	}
	

	@FXML
	void showFilterOnBtnP1(MouseEvent event) {
		Predicate<Ticket>  newPredicate = e -> e.getPriority().equals(TicketPriority.P1);
		tableViewData.setPredicate((Predicate<T>) newPredicate);
	}
	@FXML
	void showFilterOnBtnP2(MouseEvent event) {
		Predicate<Ticket>  newPredicate = e -> e.getPriority().equals(TicketPriority.P2);
		tableViewData.setPredicate((Predicate<T>) newPredicate);
	}
	@FXML
	void showFilterOnBtnP3(MouseEvent event) {
		Predicate<Ticket>  newPredicate = e -> e.getPriority().equals(TicketPriority.P3);
		tableViewData.setPredicate((Predicate<T>) newPredicate);
	}
		
	@FXML
	void addOnMouseClicked(MouseEvent event) {
		if (alertChangesOnTabelView()) {		
			((TicketViewModel) viewModel).setCurrentState(GUIEnum.TICKET);
			((TicketViewModel) viewModel).setSelectedTicket(null);			
		}
	}
		
	protected void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.TICKET, new ArrayList<>(Arrays.asList(LanguageResource.getString("ID"), TicketType.SOFTWARE, TicketPriority.P1, LanguageResource.getString("title"), TicketStatus.CREATED)));
		
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
	        case "TicketPriority" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_prio").toUpperCase()));
				Arrays.asList(TicketPriority.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "TicketPriority";
	        }
	        case "TicketType" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_type").toUpperCase()));
				Arrays.asList(TicketType.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "TicketType";
	        }       
		    case "TicketStatus" -> {
	        	if (TicketStatus.isOutstanding()) {
					stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_status").toUpperCase()));
					Arrays.asList(TicketStatus.getOutstandingTicketStatuses()).forEach(e -> e.forEach(string ->stringArrayList.add(string.toString())));
					itemText = "TicketStatus";
				} else {
					stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_status").toUpperCase()));
					Arrays.asList(TicketStatus.getResolvedTicketStatuses()).forEach(e -> e.forEach(string ->stringArrayList.add(string.toString())));
					itemText = "TicketStatus";
				}
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
	        case "TicketStatus" -> c.getSelectionModel().select("SELECT STATUS");
	        case "TicketPriority" -> c.getSelectionModel().select(LanguageResource.getString("select_prio").toUpperCase());
	        case "TicketType" -> c.getSelectionModel().select(LanguageResource.getString("select_type").toUpperCase());
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

					ArrayList<TicketPriority> ticketPriorityArrayList = new ArrayList<>(Arrays.asList(TicketPriority.values()));
					List<String> userStatusRoleStringArray = ticketPriorityArrayList.stream().map(TicketPriority::toString).collect(Collectors.toList());
					
					ArrayList<TicketType> ticketTypeArrayList = new ArrayList<>(Arrays.asList(TicketType.values()));
					List<String> ticketTypeStringArray = ticketTypeArrayList.stream().map(TicketType::toString).collect(Collectors.toList());
					
					ArrayList<TicketStatus> ticketStatusArrayList = new ArrayList<>(Arrays.asList(TicketStatus.values()));
					List<String> ticketStatusStringArray = ticketStatusArrayList.stream().map(TicketStatus::toString).collect(Collectors.toList());					

					String selectedItem = comboBox.getSelectionModel().getSelectedItem().toString();

					//There are 2 active ENUMS					
					if (userStatusRoleStringArray.contains(selectedItem)){
						predicates.add(giveFilterPredicate("TicketPriority", selectedItem.toLowerCase()));
					} else if (ticketTypeStringArray.contains(selectedItem)){
						predicates.add(giveFilterPredicate("TicketType", selectedItem.toLowerCase()));
					} else if (ticketStatusStringArray.contains(selectedItem)){
						predicates.add(giveFilterPredicate("TicketStatus", selectedItem.toLowerCase()));
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
				((TicketViewModel) viewModel).setSelectedTicket((Ticket) data);				
			}
		});
	}

	protected Predicate giveFilterPredicate(String fieldName, String filterText){
		fieldName = fieldName.toLowerCase();
			//TODO
			if (fieldName.length() > 0 && !filterText.toLowerCase().contains(LanguageResource.getString("select"))){
				System.out.println("FIELDNAME: "+fieldName);
				Predicate<Ticket> newPredicate;

				if (fieldName.equalsIgnoreCase(LanguageResource.getString("ID"))){
					newPredicate = e -> e.getTicketIdString().equals(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("select_type")) || fieldName.equalsIgnoreCase("tickettype")){
					newPredicate = e -> e.getTicketTypeAsString().toLowerCase().equals(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("select_prio")) || fieldName.equalsIgnoreCase("ticketpriority")){
					newPredicate = e -> e.getPriorityAsString().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("title"))){
					newPredicate = e -> e.getTitle().toLowerCase().contains(filterText);
				} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("select_status")) || fieldName.equalsIgnoreCase("ticketstatus")) {
					newPredicate = e -> e.getStatusAsString().toLowerCase().equals(filterText);
				} else {
					throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
				}
				return newPredicate;				
			}		
		return null;
	}
		
}
