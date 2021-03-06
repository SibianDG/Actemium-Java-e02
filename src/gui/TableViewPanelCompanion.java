package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import domain.Contract;
import domain.ContractType;
import domain.Customer;
import domain.Employee;
import domain.Ticket;
import domain.User;
import domain.enums.ContractStatus;
import domain.enums.ContractTypeStatus;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.enums.Timestamp;
import domain.enums.UserStatus;
import gui.viewModels.ContractTypeViewModel;
import gui.viewModels.TicketViewModel;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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
	private final ViewModel viewModel;
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

	private ObservableList<T> mainData;
	private FilteredList<T> tableViewData;
	
	public TableViewPanelCompanion(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState) {
		this.dashboardFrameController = dashboardFrameController;
		this.viewModel = viewModel;
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
				this.mainData = (ObservableList<T>) ((UserViewModel) viewModel).getEmployees();
				this.tableViewData = new FilteredList<>(mainData);

				propertyMap.put("Firstname", item -> ((Employee)item).firstNameProperty());
				propertyMap.put("Lastname", item -> ((Employee)item).lastNameProperty());
//				propertyMap.put("Username", item -> ((Employee)item).usernameProperty());
				propertyMap.put("Role", item -> ((Employee)item).roleProperty());
				propertyMap.put("Status", item -> ((Employee)item).statusProperty());
			}
			case CUSTOMER -> {
				this.mainData = (ObservableList<T>) ((UserViewModel) viewModel).getCustomers();
				this.tableViewData = new FilteredList<>(mainData);
				propertyMap.put("Company", item -> ((Customer)item).getCompany().nameProperty());
				propertyMap.put("Status", item -> ((Customer)item).statusProperty());
				propertyMap.put("Firstname", item -> ((Customer)item).firstNameProperty());
				propertyMap.put("Lastname", item -> ((Customer)item).lastNameProperty());
//				propertyMap.put("Username", item -> ((Customer)item).usernameProperty());
			}
			case TICKET -> {
				this.mainData = (ObservableList<T>) ((TicketViewModel) viewModel).getActemiumTickets();
//				this.tableView.setPrefWidth(1000.0);
				this.tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
				this.tableViewData = new FilteredList<>(mainData);
				//propertyMap.put("Number", item -> ((ActemiumTicket) item).numberProperty());
				propertyMap.put("ID", item -> ((Ticket) item).ticketIdProperty());
				propertyMap.put("Type", item -> ((Ticket) item).ticketTypeProperty());
				propertyMap.put("Priority", item -> ((Ticket) item).priorityProperty());
				propertyMap.put("Title", item -> ((Ticket) item).titleProperty());
				propertyMap.put("Status", item -> ((Ticket) item).statusProperty());
			}
			case CONTRACTTYPE -> {
				this.mainData = (ObservableList<T>) ((ContractTypeViewModel) viewModel).getActemiumContractTypes();
				this.tableViewData = new FilteredList<>(mainData);
				propertyMap.put("Name", item -> ((ContractType) item).getContractTypeNameString());
				propertyMap.put("Status", item -> ((ContractType) item).getContractTypeStatusProperty());
				//propertyMap.put("Number Active Contracts", item -> ((ContractType) item));
			}
		}

		btnAdd.setText("Add "+currentState.toString().toLowerCase());
		initializeFilters();
		initializeTableView();
	}
	
	private <T> TableColumn<T, String> createColumn(String title, Function<T, Property<String>> prop) {
		
		TableColumn<T, String> column = new TableColumn<>(title);
		column.setCellValueFactory(cellData -> prop.apply(cellData.getValue()));
		// Resize policy for Title column in Tickets TableView
		if(title.equals("Title")) {
			//TODO
			column.setPrefWidth(320.00);
		}
		return column;	
	}
	
	private void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.EMPLOYEE, new ArrayList<>(Arrays.asList("Firstname", "Lastname", EmployeeRole.ADMINISTRATOR, UserStatus.ACTIVE)));
		filterMap.put(GUIEnum.CUSTOMER, new ArrayList<>(Arrays.asList("Company", UserStatus.ACTIVE, "Firstname", "Lastname")));
		filterMap.put(GUIEnum.TICKET, new ArrayList<>(Arrays.asList("ID", TicketType.SOFTWARE, TicketPriority.P1, "Title", TicketStatus.CREATED)));
		filterMap.put(GUIEnum.CONTRACTTYPE, new ArrayList<>(Arrays.asList("Name", ContractTypeStatus.ACTIVE)));

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
	        	stringArrayList = new ArrayList<>(Collections.singleton("SELECT STATUS"));
				Arrays.asList(UserStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "UserStatus";
	        }
	        case "EmployeeRole" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton("SELECT ROLE"));
				Arrays.asList(EmployeeRole.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "EmployeeRole";
	        }
	        case "TicketPriority" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton("SELECT PRIO"));
				Arrays.asList(TicketPriority.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "TicketPriority";
	        }
	        case "TicketType" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton("SELECT TYPE"));
				Arrays.asList(TicketType.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "TicketType";
	        }       
		    case "TicketStatus" -> {
		    	stringArrayList = new ArrayList<>(Collections.singleton("SELECT STATUS"));
				Arrays.asList(TicketStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "TicketStatus";
		    }                
		    case "ContractStatus" -> {
		    	stringArrayList = new ArrayList<>(Collections.singleton("SELECT STATUS"));
				Arrays.asList(ContractStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "ContractStatus";
		    }
		    case "ContractTypeStatus" -> {
		    	stringArrayList = new ArrayList<>(Collections.singleton("SELECT STATUS"));
				Arrays.asList(ContractTypeStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "ContractTypeStatus";
		    }
		    case "Timestamp" -> {
		    	stringArrayList = new ArrayList<>(Collections.singleton("SELECT TIMESTAMP"));
				Arrays.asList(Timestamp.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "Timestamp";
		    }
	        default -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton("SELECT STATUS"));
				Arrays.asList(UserStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "Status";
	        }
	    } 	
	    
		list = FXCollections.observableList(stringArrayList);
		ComboBox c = new ComboBox(list);
		
		switch(itemText) {
	        case "UserStatus" -> {
	        	c.getSelectionModel().select("SELECT STATUS");
	        }
	        case "EmployeeRole" -> {
	        	c.getSelectionModel().select("SELECT ROLE");
	        }
	        case "TicketPriority" -> {
	        	c.getSelectionModel().select("SELECT PRIO");
	        }
	        case "TicketType" -> {
	        	c.getSelectionModel().select("SELECT TYPE");
	        }	        
		    case "TicketStatus" -> {
		    	c.getSelectionModel().select("SELECT STATUS");
		    }                
		    case "ContractStatus" -> {
		    	c.getSelectionModel().select("SELECT STATUS");
		    }
		    case "ContractTypeStatus" -> {
		    	c.getSelectionModel().select("SELECT STATUS");
		    }
		    case "Timestamp" -> {
		    	c.getSelectionModel().select("SELECT TIMESTAMP");
		    }
	        default -> {
	        	c.getSelectionModel().select("SELECT");
	        }
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
//		        	
//				 switch(object.getClass().getSimpleName()) {
//			        case "UserStatus" -> {
//			        	predicates.add(giveFilterPredicate("UserStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//			        	System.out.println("yoooo");
//			        }
//			        case "EmployeeRole" -> {
//						predicates.add(giveFilterPredicate("EmployeeRole", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//			        }
//			        case "TicketPriority" -> {
//						predicates.add(giveFilterPredicate("TicketPriority", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//			        }
//			        case "TicketType" -> {
//						predicates.add(giveFilterPredicate("TicketType", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//			        }        
//				    case "TicketStatus" -> {
//						predicates.add(giveFilterPredicate("TicketStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//				    }                
//				    case "ContractStatus" -> {
//						predicates.add(giveFilterPredicate("ContractStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//				    }
//				    case "ContractTypeStatus" -> {
//						predicates.add(giveFilterPredicate("ContractTypeStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//				    }
//				    case "Timestamp" -> {
//						predicates.add(giveFilterPredicate("Timestamp", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//				    }
//			        default -> {
////						predicates.add(giveFilterPredicate("EmployeeRole", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
//			        }
//		        } 
//				}
//				if (comboBox.getSelectionModel().getSelectedItem() != null &&  !comboBox.getSelectionModel().getSelectedItem().toString().contains("SELECT")) {

					ArrayList<UserStatus> userStatusArrayList = new ArrayList<>(Arrays.asList(UserStatus.values()));
					List<String> userStatusStringArray = userStatusArrayList.stream().map(UserStatus::toString).collect(Collectors.toList());
					
					ArrayList<EmployeeRole> employeeRoleArrayList = new ArrayList<>(Arrays.asList(EmployeeRole.values()));
					List<String> employeeRoleStringArray = employeeRoleArrayList.stream().map(EmployeeRole::toString).collect(Collectors.toList());

					ArrayList<TicketPriority> ticketPriorityArrayList = new ArrayList<>(Arrays.asList(TicketPriority.values()));
					List<String> userStatusRoleStringArray = ticketPriorityArrayList.stream().map(TicketPriority::toString).collect(Collectors.toList());
					
					ArrayList<TicketType> ticketTypeArrayList = new ArrayList<>(Arrays.asList(TicketType.values()));
					List<String> ticketTypeStringArray = ticketTypeArrayList.stream().map(TicketType::toString).collect(Collectors.toList());
					
					ArrayList<TicketStatus> ticketStatusArrayList = new ArrayList<>(Arrays.asList(TicketStatus.values()));
					List<String> ticketStatusStringArray = ticketStatusArrayList.stream().map(TicketStatus::toString).collect(Collectors.toList());
					
					ArrayList<ContractStatus> contractStatusArrayList = new ArrayList<>(Arrays.asList(ContractStatus.values()));
					List<String> contractStatusStringArray = contractStatusArrayList.stream().map(ContractStatus::toString).collect(Collectors.toList());
					
					ArrayList<ContractTypeStatus> contractTypeStatusArrayList = new ArrayList<>(Arrays.asList(ContractTypeStatus.values()));
					List<String> contractTypeStatusStringArray = contractTypeStatusArrayList.stream().map(ContractTypeStatus::toString).collect(Collectors.toList());
					
					ArrayList<Timestamp> timestampArrayList = new ArrayList<>(Arrays.asList(Timestamp.values()));
					List<String> timestampStringArray = timestampArrayList.stream().map(Timestamp::toString).collect(Collectors.toList());
					

					if (userStatusStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("UserStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));					
					} else if (employeeRoleStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("EmployeeRole", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
					} else if (userStatusRoleStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("TicketPriority", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
					} else if (ticketTypeStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("TicketType", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
					} else if (ticketStatusStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("TicketStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
					} else if (contractStatusStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("ContractStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
					} else if (contractTypeStatusStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("ContractTypeStatus", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
					} else if (timestampStringArray.contains(comboBox.getSelectionModel().getSelectedItem().toString())){
						predicates.add(giveFilterPredicate("Timestamp", comboBox.getSelectionModel().getSelectedItem().toString().toLowerCase()));
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
			TableColumn<T, String> c = createColumn(key, prop);
			tableView.getColumns().add(c);
		});

		tableView.setItems(tableViewData);
		
		tableView.setOnMouseClicked((MouseEvent m) -> {
			boolean showNewObject = true;
			if(viewModel.isFieldModified()) {
				//popup
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Fields were modified without saving!");
				alert.setHeaderText("Fields were modified without saving!");
				alert.setContentText("Choose your option.");

				ButtonType discardChanges = new ButtonType("Discard Changes");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(discardChanges, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == discardChanges){
					// using this variable with if to avoid duplicate code
					showNewObject = true;
					viewModel.setFieldModified(false);
				} else {
				    // ... user chose CANCEL or closed the dialog
					// nothing happens -> back to same detail panel
					showNewObject = false;
				}
			} 
			// using this if to avoid duplicate code
			if (showNewObject) {
				T data = tableView.getSelectionModel().selectedItemProperty().get();
				if (data instanceof Employee || data instanceof Customer){
					((UserViewModel) viewModel).setSelectedUser((User) data);
				} else if (data instanceof Ticket) {
					((TicketViewModel) viewModel).setSelectedActemiumTicket((Ticket) data);
				} else if (data instanceof ContractType) {
					((ContractTypeViewModel) viewModel).setSelectedActemiumContractType((ContractType) data);
				}
			}
		});		
	}

	@FXML
	void addOnAction(ActionEvent event) {		
		switch(currentState) {
			case EMPLOYEE ->{
				((UserViewModel) viewModel).setCurrentState(GUIEnum.EMPLOYEE);
				((UserViewModel) viewModel).setSelectedUser(null);
			}
			case CUSTOMER -> {
				((UserViewModel) viewModel).setCurrentState(GUIEnum.CUSTOMER);
				((UserViewModel) viewModel).setSelectedUser(null);
			}
			case TICKET -> {
				((TicketViewModel) viewModel).setCurrentState(GUIEnum.TICKET);
				((TicketViewModel) viewModel).setSelectedActemiumTicket(null);
			}
			case CONTRACTTYPE -> {
				((ContractTypeViewModel) viewModel).setCurrentState(GUIEnum.CONTRACTTYPE);
				((ContractTypeViewModel) viewModel).setSelectedActemiumContractType(null);
			}
			default -> {
				//tableView.getSelectionModel().clearSelection();
//				((UserViewModel) viewModel).setSelectedUser(null);
			}		
		}
	}

	private Predicate giveFilterPredicate(String fieldName, String filterText){		
		
		if(currentState.equals(GUIEnum.EMPLOYEE)) {
			if (fieldName.length() > 0 && !filterText.contains("select")){
				
				Predicate<Employee> newPredicate;
				
				switch (fieldName) {				
					case "Firstname" -> {
						newPredicate = e -> e.getFirstName().toLowerCase().contains(filterText);		
					}
					case "Lastname" -> {
						newPredicate = e -> e.getLastName().toLowerCase().contains(filterText);					
					}
					case "UserStatus" -> {
						newPredicate = e -> e.getStatus().toLowerCase().equals(filterText);
					}
					case "EmployeeRole" -> {
						newPredicate = e -> e.getRole().toLowerCase().contains(filterText);
					}
					default -> throw new IllegalStateException("Unexpected value: " + fieldName);
				}				
				return newPredicate;				
			}
		} else if (currentState.equals(GUIEnum.CUSTOMER)){
			if (fieldName.length() > 0 && !filterText.contains("select")){

				Predicate<Customer> newPredicate;
				
				switch (fieldName) {
					case "Company" -> {
						newPredicate = e -> e.getCompany().getName().toLowerCase().contains(filterText);
					}				
					case "UserStatus" -> {
						newPredicate = e -> e.getStatus().toLowerCase().equals(filterText);
					}
					case "Firstname" -> {
						newPredicate = e -> e.getFirstName().toLowerCase().contains(filterText);		
					}
					case "Lastname" -> {
						newPredicate = e -> e.getLastName().toLowerCase().contains(filterText);					
					}						
					default -> throw new IllegalStateException("Unexpected value: " + fieldName);
				}				
				return newPredicate;				
			}
		} else if (currentState.equals(GUIEnum.TICKET)){
			//TODO
			if (fieldName.length() > 0 && !filterText.contains("select")){

				Predicate<Ticket> newPredicate;
				
				switch (fieldName) {
					case "ID" -> {
						newPredicate = e -> e.getTicketIdString().equals(filterText);
					}
					case "TicketType" -> {
						newPredicate = e -> e.getTicketType().toLowerCase().equals(filterText);
					}
					case "TicketPriority" -> {
						newPredicate = e -> e.getPriority().toLowerCase().contains(filterText);
					}
					case "Title" -> {
						newPredicate = e -> e.getTitle().toLowerCase().contains(filterText);
					}
					case "TicketStatus" -> {
						newPredicate = e -> e.getStatus().toLowerCase().equals(filterText);
					}
					//TODO how will we display Company name in our observableList?
//					case "Company" -> {
//						newPredicate = e -> e.getCustomer().getCompany().getName().toLowerCase().contains(filterText);
//					}
					default -> throw new IllegalStateException("Unexpected value: " + fieldName);
				}				
				return newPredicate;				
			}
		} else if (currentState.equals(GUIEnum.CONTRACTTYPE)){
			//TODO
			if (fieldName.length() > 0 && !filterText.contains("select")){

				Predicate<ContractType> newPredicate;

				//TODO change fieldnames
				switch (fieldName) {
					case "Name" -> {
						newPredicate = e -> e.getName().toLowerCase().contains(filterText);
					}
					case "Type" -> {
						newPredicate = e -> e.getTimestamp().toString().toLowerCase().equals(filterText);
					}
					case "Status" -> {
						newPredicate = e -> e.getContractTypeStatus().toString().toLowerCase().equals(filterText);
					}
					default -> throw new IllegalStateException("Unexpected value: " + fieldName);
				}				
				return newPredicate;				
			}
		} else if (currentState.equals(GUIEnum.CONTRACT)){
			//TODO
			if (fieldName.length() > 0 && !filterText.contains("select")){

				Predicate<Contract> newPredicate;
				
				//TODO change fieldnames
				switch (fieldName) {
					case "Type" -> {
						newPredicate = e -> e.getContractType().equals(filterText);
					}
					case "Status" -> {
						newPredicate = e -> e.getStatus().toString().toLowerCase().equals(filterText);
					}
					case "StartDate" -> {
						newPredicate = e -> e.getStartDate().toString().contains(filterText);
					}
					case "EndDate" -> {
						newPredicate = e -> e.getEndDate().toString().contains(filterText);
					}
					default -> throw new IllegalStateException("Unexpected value: " + fieldName);
				}				
				return newPredicate;				
			}
		}
		
		return null;
	}
		
	private void setPredicateForFilteredList(Predicate newPredicate){
		Predicate currentPredicate = tableViewData.getPredicate();
		Predicate resultPredicate;
		if(currentPredicate != null){
			resultPredicate = currentPredicate.and(newPredicate);
		} else {
			resultPredicate = newPredicate;
		}
		tableViewData.setPredicate((Predicate<? super T>) resultPredicate);
	}
	
}
