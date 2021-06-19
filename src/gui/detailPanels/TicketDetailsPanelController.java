package gui.detailPanels;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import domain.Employee;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import gui.tableViewPanels.SelectCustomerIdTableViewPanelController;
import gui.viewModels.TicketViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import languages.LanguageResource;


public class TicketDetailsPanelController extends DetailsPanelController {
    
	private EmployeeRole signedInEmployeeRole;
    private TicketHistoryPanelController ticketHistoryPanelController;
    private SelectCustomerIdTableViewPanelController selectCustomerIdTableViewPanelController;
    
    private Stage customerIdStage;
    
    public TicketDetailsPanelController(ViewModel viewModel, GridPane gridContent, EmployeeRole signedInEmployeeRole,
    		SelectCustomerIdTableViewPanelController selectCustomerIdTableViewPanelController)  {
        super(viewModel, gridContent);
        this.signedInEmployeeRole = signedInEmployeeRole;
        this.selectCustomerIdTableViewPanelController = selectCustomerIdTableViewPanelController;
        
        initCustomerIdScreen();
    }

    @Override
    public void invalidated(Observable observable) {
        try {
            setDetailOnModifying();
        } catch (NullPointerException e){
            setupPaneNewObject();
        }
    }
    
    @FXML
    void btnHistoryOnAction(ActionEvent event) {
    	// show ticket history
    	
    	// time-of-edit		username (who dit the edit)
    	// 					String with change that happended
    	// 					content of the change
    	
    	// console output
    	
    	ticketHistoryPanelController = new TicketHistoryPanelController((TicketViewModel) super.viewModel, super.gridContent);
		gridContent.add(ticketHistoryPanelController, 1, 0);
    }

    @FXML
    void btnModifyOnAction(ActionEvent event) {
        try {            
	        if (editing) {
	            if(viewModel.isFieldModified() && TicketStatus.isOutstanding()){
	                ((TicketViewModel) viewModel).modifyTicketOutstanding(
	                        // priority, ticketType, title, description, remarks, attachments, technicians
	                        TicketPriority.valueOf(getTextFromGridItem(3))
	                        , TicketType.valueOf(getTextFromGridItem(4))
	                        , TicketStatus.valueOf(getTextFromGridItem(5))
	                        , getTextFromGridItem(0)
	                        , getTextFromGridItem(6)
	                        , getTextFromGridItem(10)
	                        , getTextFromGridItem(11)
	                        , ((TicketViewModel) viewModel).getTechniciansAsignedToTicket()
	                );
					showPopupMessage("popupSuccess", LanguageResource.getString("ticketEdited_succes"));
	            } else if (viewModel.isFieldModified() && !TicketStatus.isOutstanding()){
	                ((TicketViewModel) viewModel).modifyTicketResolved(
	                        // solution, quality, supportNeeded
	                        getTextFromGridItem(13)
	                        , getTextFromGridItem(14)
	                        , getTextFromGridItem(15)
	                );
					showPopupMessage("popupSuccess", LanguageResource.getString("ticketEdited_succes"));
	            } else {
					showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));
	            }
	        } else {
	            ((TicketViewModel) viewModel).registerTicket(
	                    // priority, ticketType, title, description, remarks, attachments, customerId
	                    TicketPriority.valueOf(getTextFromGridItem(2))
	                    , TicketType.valueOf(getTextFromGridItem(3))
	                    , getTextFromGridItem(0)
	                    , getTextFromGridItem(6)
	                    , getTextFromGridItem(7)
	                    , getTextFromGridItem(8)
	                    , Integer.parseInt(getTextFromGridItem(5))
	            );
	            showPopupMessageAddItem("popupSuccess", LanguageResource.getString("ticket_created_success"));
	        }            
            editing = false;
            viewModel.setFieldModified(false);
            setDetailOnModifying();
            
        } catch (InformationRequiredException ire) {

            StringBuilder errorMessage = new StringBuilder();
            ire.getInformationRequired().forEach(e -> {
                errorMessage.append(e).append("\n");

            });
            txtErrorMessage.setText(errorMessage.toString());
            txtErrorMessage.setVisible(true);
        } catch (NumberFormatException nfe) {
            txtErrorMessage.setText(LanguageResource.getString("number_invalid"));
            txtErrorMessage.setVisible(true);
        }
        catch (Exception e){
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void setDetailOnModifying(){
        initGridDetails();        


        if (TicketStatus.isOutstanding()) {
            addGridDetails(((TicketViewModel) viewModel).getDetails());
            btnDelete.setVisible(((Employee) ((TicketViewModel) viewModel).getSignedInUser()).getRole() != EmployeeRole.TECHNICIAN);
        } else {
            addGridDetails(((TicketViewModel) viewModel).getDetails());
            btnDelete.setVisible(false);
        }
        txtDetailsTitle.setText(String.format("%s: %s", LanguageResource.getString("details_of_ticket"), ((TicketViewModel) viewModel).getIdSelectedTicket()));
        btnModify.setText(LanguageResource.getString("modify_ticket"));
        btnHistory.setVisible(true);
        
        btnModify.setVisible(true);
        txtErrorMessage.setVisible(false);
        editing = true;
    }

    private void setupPaneNewObject(){
        editing = false;
        ArrayList<String> fields = null;
        
        btnHistory.setVisible(false);
        btnDelete.setVisible(false);

        viewModel.setFieldModified(true);
       
        if (((TicketViewModel) viewModel).getCurrentState().equals(GUIEnum.TICKET)) {
            fields = ((TicketViewModel) viewModel).getDetailsNewTicket();
            txtDetailsTitle.setText(LanguageResource.getString("addTicket"));
            btnModify.setText(LanguageResource.getString("addTicket"));
        }
        btnModify.setVisible(true);
        assert fields != null;
        addItemsToGridNewTicket(fields);        
    }

    private void addItemsToGridNewTicket(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> demoValues = Map.of(
                0, "WieldingRobot05 Defect"
                , 1, LocalDate.now().toString()
                , 2, TicketPriority.P3.toString()
                , 3, TicketType.OTHER.toString()
                , 5, "001"
                , 6, "WieldingRobot stopped functioning this morning at 9am."
                , 7, "Call me asap 094812384"
                , 8, "brokenRobot.png"
        );

        for (int i = 0; i < fields.size(); i++) {
            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(fields.get(i), true), 0, i);

            Node node;
            if (fields.get(i).toLowerCase().contains(LanguageResource.getString("priority").toLowerCase())) {
                node = makeComboBox(TicketPriority.P3);
            } else if (fields.get(i).toLowerCase().contains(LanguageResource.getString("type").toLowerCase())) {
                node = makeComboBox(TicketType.OTHER);
            }  else if (fields.get(i).toLowerCase().contains(LanguageResource.getString("technicians").toLowerCase())){
                node = makeViewTechnicians(null);
            } else {
                TextField textField;
                textField = new TextField(demoValues.get(i));
                textField.setFont(Font.font("Arial", 14));
                textField.setPromptText(fields.get(i));
                if(fields.get(i).equals(LanguageResource.getString("creation_date"))) {
                	textField.setEditable(false);
                }
                if(fields.get(i).equals(LanguageResource.getString("customer_ID"))) {
                	setCustomerIdTextField(textField);
                	textField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            			if (KeyCode.F4.equals(e.getCode())) {  
            				customerIdStage.show();
            				Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            		        customerIdStage.setX((primScreenBounds.getWidth() - customerIdStage.getWidth()) / 2);
            		        customerIdStage.setY((primScreenBounds.getHeight() - customerIdStage.getHeight()) / 2);
            			}
            		});
                }
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }
    
    private void initCustomerIdScreen() {    	
        Stage customerIdStage = new Stage();
        Scene scene = new Scene(selectCustomerIdTableViewPanelController);
        customerIdStage.setScene(scene);        
        customerIdStage.setTitle(LanguageResource.getString("select_customer"));        
        customerIdStage.getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        
        this.customerIdStage = customerIdStage;
	}
    
    private void setCustomerIdTextField(TextField customerIdTextField) {
    	selectCustomerIdTableViewPanelController.setCustomerIdTextField(customerIdTextField);
	}

	private void addGridDetails(Map<String, Map<Boolean, Object>> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key, true);

            Node detail = createElementDetailGridpane(details.get(key), key);
           
            gridDetails.add(makeNewLabel(key, true), 0, i);
            gridDetails.add(detail, 1, i);
            
            i++;
        }
    }

    private Node createElementDetailGridpane(Map<Boolean, Object> map, String key) {
        boolean disable = (boolean) map.keySet().toArray()[0];
        boolean editable = disable;
        Object o = map.get(disable);

        disable = !disable;

        Node node = null;

        if (o instanceof String) {
            String string = (String) o;
                        
            if(key.equalsIgnoreCase(LanguageResource.getString("description"))
            		|| key.equalsIgnoreCase(LanguageResource.getString("comments"))) {
            	TextArea detail = new TextArea(string);
            	
            	detail.textProperty().addListener((observable, oldValue, newValue) -> {
                    viewModel.setFieldModified(true);
                });
                detail.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

                if (string.equals("")){
                    detail.setVisible(false);
                    detail.setPadding(new Insets(15, 0, 0, 0));
                } else {
                    detail.setId("textFieldWithPadding");
                }
                // TextArea should still be scrollable when ticket is resolved
                // otherwise the technician and support manager can't read
                // all the text that is written in description
	            detail.setEditable(editable);
                detail.setPromptText(key);
                
                detail.setWrapText(true);

	            node = detail;
            } else {
            	TextField detail = new TextField(string);
	            
	            detail.textProperty().addListener((observable, oldValue, newValue) -> {
	                viewModel.setFieldModified(true);
	            });
	            detail.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	
                detail.setId("textFieldWithPadding");
	            
	            detail.setDisable(disable);
	            detail.setPromptText(key);
	
	            node = detail;
	        }
        } else if (o instanceof Enum) {
        	node = makeComboBox(o);
        	node.setDisable(disable);
        } else if (o instanceof Boolean) {
            node = makeComboBox(o);
            node.setDisable(disable);
        } else if (o instanceof ObservableList) {
            node = makeViewTechnicians(o);           
        } 
        return node;
    }
    
    private <T> Node makeViewTechnicians(Object o) {
        VBox vBox = new VBox();
        ObservableList<Employee> technicians = null;
        if (o != null){
            technicians = (ObservableList<Employee>) o;
        }
        ObservableList<String> stringsList = FXCollections.observableArrayList();
        ObservableList<Employee> allTechnicians = ((TicketViewModel) viewModel).getAllTechnicians();
        List<CheckMenuItem> listTechicians = new ArrayList<>();
        Map<String, Employee> namesAndTechs = new HashMap<>();

        ListView<String> listView = new ListView<>(stringsList);

        if (o != null) {
            allTechnicians = allTechnicians.stream().filter(t -> {
                TicketType ticketType = ((TicketViewModel) super.viewModel).getSelectedTicket().getTicketType();
                return t.getSpecialties().contains(ticketType);
            }).collect(Collectors.collectingAndThen(toList(), FXCollections::observableArrayList));
        }


        allTechnicians.forEach(item -> {
            listTechicians.add(new CheckMenuItem(item.getFirstName() + " " + item.getLastName()));
            namesAndTechs.put(item.getFirstName() + " " + item.getLastName(), item);
        });

        if (o != null) {
            //set the technicians already asigned to ticket marked
            for (Employee tech : technicians) {
                String name = tech.getFirstName() + " " + tech.getLastName();
                listTechicians.forEach(item -> {
                    if (item.getText().equals(name)) {
                        item.setSelected(true);
                        stringsList.add(name);
                        ((TicketViewModel) viewModel).addTechnicianToTicket(tech);
                    }
                });
            }
        }

        //create dropdown with all possible employees
        MenuButton menuButton = new MenuButton(LanguageResource.getString("select_technician"));
        menuButton.setId("menu-bar");
        menuButton.getItems().addAll(listTechicians);

        for (final CheckMenuItem tech : listTechicians) {
            ObservableList<Employee> finalTechnicians = technicians;
            tech.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    stringsList.add(tech.getText());
                    viewModel.setFieldModified(true);
                    //add technician to ticket
                    ((TicketViewModel) viewModel).addTechnicianToTicket(namesAndTechs.get(tech.getText()));
                } else {
                    stringsList.remove(tech.getText());
                    viewModel.setFieldModified(true);
                    //add technician to ticket
                    ((TicketViewModel) viewModel).removeTechnician(namesAndTechs.get(tech.getText()));
                }
                if (o != null) {
                    // prevents an unnecessary modifyTicket and prevents a useless entry in tickethistory
                    if (((TicketViewModel) viewModel).getTechniciansAsignedToTicket().stream().sorted().collect(toList())
                            .equals(finalTechnicians.stream().sorted().collect(toList()))) {
                        viewModel.setFieldModified(false);
                    }
                }
                listView.setMaxHeight(((TicketViewModel) viewModel).getTechniciansAsignedToTicket().size()*25+25);
            });
        }

        //create Listview for technicians for ticket
        listView.setMaxHeight(((TicketViewModel) viewModel).getTechniciansAsignedToTicket().size()*25+25);		
        listView.getStylesheets().add("file:src/start/styles.css");
        listView.setId("list-view");
        // only show menuButton when ticket is outstanding
        // hide it when ticket is resolved
        if (!TicketStatus.isOutstanding() || !signedInEmployeeRole.equals(EmployeeRole.SUPPORT_MANAGER)) {        	
			listView.setMinHeight(50);
			vBox.getChildren().addAll(listView);
		} else {
			vBox.getChildren().addAll(menuButton, listView);
		}
        return vBox;
    }

    private ComboBox makeComboBox(Object o){

        ObservableList list = null;
        if (o instanceof Boolean){
            list = FXCollections.observableList(Arrays.asList(true, false));
        } else {
            switch(o.getClass().getSimpleName()) {
                case "TicketPriority" -> list = FXCollections.observableList(Arrays.asList(TicketPriority.values()));
                case "TicketType" -> list = FXCollections.observableList(Arrays.asList(TicketType.values()));
                case "TicketStatus" -> list = FXCollections.observableList(Arrays.asList(TicketStatus.values()));
            }
        }
        ComboBox c = new ComboBox(list);
        c.getSelectionModel().select(o);
        c.valueProperty().addListener(e -> {
            viewModel.setFieldModified(true);
        });

        return c;
    }
    
}
