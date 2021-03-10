package gui;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import domain.*;
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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TableViewPanelCompanion<T> extends GridPane implements InvalidationListener {

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
		dashboardFrameController.addListener(this);

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
				this.mainData = (ObservableList<T>) ((UserViewModel) viewModel).giveEmployees();
				this.tableViewData = new FilteredList<>(mainData);
				tableViewData.sorted((Comparator<T>) Comparator.comparing(c -> ((User) c).getFirstName().toLowerCase()).thenComparing(c -> ((User) c).getLastName().toLowerCase()));
				propertyMap.put("Firstname", item -> ((Employee)item).firstNameProperty());
				propertyMap.put("Lastname", item -> ((Employee)item).lastNameProperty());
//				propertyMap.put("Username", item -> ((Employee)item).usernameProperty());
				propertyMap.put("Role", item -> ((Employee)item).roleProperty());
				propertyMap.put("Status", item -> ((Employee)item).statusProperty());
			}
			case CUSTOMER -> {
				this.mainData = (ObservableList<T>) ((UserViewModel) viewModel).giveCustomers();
				this.tableViewData = new FilteredList<>(mainData);
				tableViewData.sorted((Comparator<T>) Comparator.comparing(c -> ((Customer) c).getCompany().getName().toLowerCase()).thenComparing(c -> ((User) c).getFirstName().toLowerCase()).thenComparing(c -> ((User) c).getLastName().toLowerCase()));
				propertyMap.put("Company", item -> ((Customer)item).getCompany().nameProperty());
				propertyMap.put("Status", item -> ((Customer)item).statusProperty());
				propertyMap.put("Firstname", item -> ((Customer)item).firstNameProperty());
				propertyMap.put("Lastname", item -> ((Customer)item).lastNameProperty());
//				propertyMap.put("Username", item -> ((Customer)item).usernameProperty());
			}
			case TICKET -> {
				// obsolete, but just in case we want all the tickets in the future
//				this.mainData = (ObservableList<T>) ((TicketViewModel) viewModel).giveTickets();
				
				if (TicketStatus.isOutstanding()) {
					this.mainData = (ObservableList<T>) ((TicketViewModel) viewModel).giveTicketsOutstanding();
				} else {
					this.mainData = (ObservableList<T>) ((TicketViewModel) viewModel).giveTicketsResolved();
				}
				
//				this.tableView.setPrefWidth(1000.0);
				//this.tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
				this.tableViewData = new FilteredList<>(mainData);
				//propertyMap.put("Number", item -> ((ActemiumTicket) item).numberProperty());
				propertyMap.put("ID", item -> ((Ticket) item).ticketIdProperty());
				propertyMap.put("Type", item -> ((Ticket) item).ticketTypeProperty());
				propertyMap.put("Priority", item -> ((Ticket) item).priorityProperty());
				propertyMap.put("Title", item -> ((Ticket) item).titleProperty());
				propertyMap.put("Status", item -> ((Ticket) item).statusProperty());
			}
			case CONTRACTTYPE -> {
				this.mainData = (ObservableList<T>) ((ContractTypeViewModel) viewModel).giveContractTypes();
				this.tableViewData = new FilteredList<>(mainData);
				propertyMap.put("Name", item -> ((ContractType) item).contractTypeNameProperty());
				propertyMap.put("Timestamp", item -> ((ContractType) item).contractTypestampProperty());
				propertyMap.put("Status", item -> ((ContractType) item).contractTypeStatusProperty());
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
		filterMap.put(GUIEnum.CONTRACTTYPE, new ArrayList<>(Arrays.asList("Name", Timestamp.WORKINGHOURS, ContractTypeStatus.ACTIVE)));

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
	        	if (TicketStatus.isOutstanding()) {
					stringArrayList = new ArrayList<>(Collections.singleton("SELECT STATUS"));
					Arrays.asList(TicketStatus.getOutstandingTicketStatuses()).forEach(e -> e.forEach(string ->stringArrayList.add(string.toString())));
					itemText = "TicketStatus";
				} else {
					stringArrayList = new ArrayList<>(Collections.singleton("SELECT STATUS"));
					Arrays.asList(TicketStatus.getResolvedTicketStatuses()).forEach(e -> e.forEach(string ->stringArrayList.add(string.toString())));
					itemText = "TicketStatus";
				}
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
	        case "UserStatus", "TicketStatus", "ContractStatus", "ContractTypeStatus" -> {
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
					System.out.println("Check filters after check: "+comboBox.getSelectionModel().getSelectedItem().toString());

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

					String selectedItem = comboBox.getSelectionModel().getSelectedItem().toString();

					//There are 2 active ENUMS
					if (currentState.equals(GUIEnum.CONTRACTTYPE) && contractTypeStatusStringArray.contains(selectedItem)) {
						predicates.add(giveFilterPredicate("ContractTypeStatus", selectedItem.toLowerCase()));
					} else {
						if (userStatusStringArray.contains(selectedItem)){
							predicates.add(giveFilterPredicate("UserStatus", selectedItem.toLowerCase()));
						} else if (employeeRoleStringArray.contains(selectedItem)){
							predicates.add(giveFilterPredicate("EmployeeRole", selectedItem.toLowerCase()));
						} else if (userStatusRoleStringArray.contains(selectedItem)){
							predicates.add(giveFilterPredicate("TicketPriority", selectedItem.toLowerCase()));
						} else if (ticketTypeStringArray.contains(selectedItem)){
							predicates.add(giveFilterPredicate("TicketType", selectedItem.toLowerCase()));
						} else if (ticketStatusStringArray.contains(selectedItem)){
							predicates.add(giveFilterPredicate("TicketStatus", selectedItem.toLowerCase()));
						} else if (contractStatusStringArray.contains(selectedItem)){
							predicates.add(giveFilterPredicate("ContractStatus", selectedItem.toLowerCase()));
						} else if (timestampStringArray.contains(selectedItem)){
							predicates.add(giveFilterPredicate("Timestamp", selectedItem.toLowerCase()));
						}
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
			alertChangesOnTabelView();
		});

		/*tableView.setOnKeyPressed( keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				viewModel.delete();
			}
		});*/
	}

	public void alertChangesOnTabelView() {
		boolean showNewObject = true;
		if(viewModel.isFieldModified()) {
			//popup
			Alert alert = new Alert(AlertType.CONFIRMATION);


			alert.setTitle("Fields were modified without saving.");
			alert.setHeaderText("You have unsaved changes!");
			alert.setContentText("Choose your option.");

			alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
			alert.getDialogPane().getStyleClass().add("alert");
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));

			ButtonType discardChanges = new ButtonType("Discard Changes");
			//TODO: make discard red
			//((Button) discardChanges).styleClass().add("btn-red");

			ButtonType buttonTypeCancel = new ButtonType("Keep Editing", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(discardChanges, buttonTypeCancel);

			alert.getDialogPane().getChildren().forEach(node -> {
				if (node instanceof ButtonBar) {
					ButtonBar buttonBar = (ButtonBar) node;
					buttonBar.getButtons().forEach(possibleButtons -> {
						if (possibleButtons instanceof Button) {
							Button b = (Button) possibleButtons;
							if (b.getText().equals("Discard Changes")) {
								b.setStyle("-fx-background-color: #c41010;  -fx-text-fill: #ffffff;");
							} else if (b.getText().equals("Keep Editing")) {
								b.setStyle("-fx-background-color: #1d3d78;  -fx-text-fill: #ffffff;");
							}
						}
					});
				}
			});

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == discardChanges){
				// using this variable with if to avoid duplicate code
				showNewObject = true;
				dashboardFrameController.setEnabled(true);
				viewModel.setFieldModified(false);
			} else {
				// ... user chose CANCEL or closed the dialog
				// nothing happens -> back to same detail panel
				dashboardFrameController.setEnabled(false);
				showNewObject = false;
			}
		}
		// using this if to avoid duplicate code
		if (showNewObject) {
			T data = tableView.getSelectionModel().selectedItemProperty().get();
			if (data instanceof Employee || data instanceof Customer){
				((UserViewModel) viewModel).setSelectedUser((User) data);
			} else if (data instanceof Ticket) {
				((TicketViewModel) viewModel).setSelectedTicket((Ticket) data);
			} else if (data instanceof ContractType) {
				((ContractTypeViewModel) viewModel).setSelectedContractType((ContractType) data);
			}
		}
	}

	@FXML
	void  addOnMouseClicked(MouseEvent event) {
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
				((TicketViewModel) viewModel).setSelectedTicket(null);
			}
			case CONTRACTTYPE -> {
				((ContractTypeViewModel) viewModel).setCurrentState(GUIEnum.CONTRACTTYPE);
				((ContractTypeViewModel) viewModel).setSelectedContractType(null);
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
			
			System.out.println("IN FILTER CONTRACTTYPE");
			System.out.println("filterText " + filterText);
			//TODO
			if (fieldName.length() > 0 && !filterText.contains("select")){

				Predicate<ContractType> newPredicate;

				//TODO change fieldnames
				switch (fieldName) {
					case "Name" -> {
						newPredicate = e -> e.getName().toLowerCase().contains(filterText);
					}
					case "Timestamp" -> {
						newPredicate = e -> e.getTimestamp().toString().toLowerCase().equals(filterText);
					}
					case "ContractTypeStatus" -> {
						newPredicate = e -> e.getContractTypeStatus().toString().toLowerCase().equals(filterText);
					}
					default -> throw new IllegalStateException("Unexpected value: " + fieldName);
				}				
				return newPredicate;				
			}
		} else if (currentState.equals(GUIEnum.CONTRACT)){
			//TODO
			if (fieldName.length() > 0 && !filterText.contains("select")){
				System.out.println("filterText " + filterText);

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

	@Override
	public void invalidated(Observable observable) {
		alertChangesOnTabelView();
	}
}
