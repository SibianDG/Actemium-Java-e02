package gui.detailPanels;

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
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import gui.viewModels.TicketViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import languages.LanguageResource;


public class TicketDetailsPanelController extends DetailsPanelController {
    
    private TicketHistoryPanelController ticketHistoryPanelController;

    public TicketDetailsPanelController(ViewModel viewModel, GridPane gridContent)  {
        super(viewModel, gridContent);     
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
    	System.out.println("=============HISTORY============\n");
    	System.out.println("==============BEGIN=============\n");
    	System.out.println("History" + txtDetailsTitle.getText().substring(7) + "\n");
    	((TicketViewModel) viewModel).getSelectedTicket().giveTicketChanges()
    				.stream().forEach(System.out::println);
    	System.out.println("===============END==============\n");    	
    	
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
	                    , getTextFromGridItem(5)
	                    , getTextFromGridItem(6)
	                    , getTextFromGridItem(7)
	                    , Long.parseLong(getTextFromGridItem(4))
	            );
	            showPopupMessageAddItem("popupSuccess", LanguageResource.getString("ticket_created_success"));
	        }            
            editing = false;
            viewModel.setFieldModified(false);
            setDetailOnModifying();
            //was trying some different ways of renewing the detailsPane
            // is this better or worse and why?
//            viewModel.fireInvalidationEvent();

            //TODO: handle the correct error messages, not just all
        } catch (InformationRequiredException ire) {

            System.out.println("IRE!! "+ire.getInformationRequired().size());

            StringBuilder errorMessage = new StringBuilder();
            ire.getInformationRequired().forEach(e -> {
                System.out.println(e);
                errorMessage.append(e).append("\n");

            });
            txtErrorMessage.setText(errorMessage.toString());
            txtErrorMessage.setVisible(true);
        } catch (NumberFormatException nfe) {
            txtErrorMessage.setText(LanguageResource.getString("number_invalid"));
            txtErrorMessage.setVisible(true);
        }
        catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void setDetailOnModifying(){
        initGridDetails();        
       
        if (TicketStatus.isOutstanding()) {
            addGridDetails(((TicketViewModel) viewModel).getDetails());
            btnDelete.setVisible(true);
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
        } else {
            fields = null;
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
                , 4, "001"
                , 5, "WieldingRobot stopped functioning this morning at 9am."
                , 6, "Call me asap 094812384"
                , 7, "brokenRobot.png"
        );

        for (int i = 0; i < fields.size(); i++) {
            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(fields.get(i), true), 0, i);

            Node node;
            if (fields.get(i).toLowerCase().contains(LanguageResource.getString("priority").toLowerCase())) {
                node = makeComboBox(TicketPriority.P3);
            } else if (fields.get(i).toLowerCase().contains(LanguageResource.getString("type").toLowerCase())) {
                node = makeComboBox(TicketType.OTHER);
            } else {
                TextField textField;
                textField = new TextField(demoValues.get(i));
                textField.setFont(Font.font("Arial", 14));
                textField.setPromptText(fields.get(i));
                if(fields.get(i).equals(LanguageResource.getString("creation_date"))) {
                	textField.setEditable(false);
                }
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
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
                        
            //TODO transform into generic method
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
                    //detail.setPadding(new Insets(0, 0, 0, 15));
                    detail.setId("textFieldWithPadding");
                }
                // TextArea should still be scrollable when ticket is resolved
                // otherwise the technician and support manager can't read
                // all the text that is written in description
	            detail.setEditable(editable);
                detail.setPromptText(key);

	            node = detail;
            } else {
            	TextField detail = new TextField(string);
	            
	            detail.textProperty().addListener((observable, oldValue, newValue) -> {
	                viewModel.setFieldModified(true);
	            });
	            detail.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
	
	                //detail.setPadding(new Insets(0, 0, 0, 15));
	                detail.setId("textFieldWithPadding");
	            
	            detail.setDisable(disable);
//	            detail.setEditable(editable);
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
        ObservableList<Employee> technicians = (ObservableList<Employee>) o;
        ObservableList<String> stringsList = FXCollections.observableArrayList();
        ObservableList<Employee> allTechnicians = ((TicketViewModel) viewModel).getAllTechnicians();
        List<CheckMenuItem> listTechicians = new ArrayList<>();
        Map<String, Employee> namesAndTechs = new HashMap<>();

        ListView<String> listView = new ListView<>(stringsList);

        allTechnicians.forEach(item -> {
            listTechicians.add(new CheckMenuItem(item.getFirstName() + " " + item.getLastName()));
            namesAndTechs.put(item.getFirstName() + " " + item.getLastName(), item);
        });

        //set the technicians already asigned to ticket marked
        for (Employee tech: technicians) {
            String name = tech.getFirstName() + " " + tech.getLastName();
            listTechicians.forEach(item ->  {
                if (item.getText().equals(name)) {
                    item.setSelected(true);
                    stringsList.add(name);
                    ((TicketViewModel) viewModel).addTechnicianToTicket(tech);
                }
            });
        }

        //create dropdown with all possible employees
        MenuButton menuButton = new MenuButton(LanguageResource.getString("select_technician"));
        menuButton.setId("menu-bar");
        menuButton.getItems().addAll(listTechicians);

        for (final CheckMenuItem tech : listTechicians) {
            tech.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    //technicians.add(((TicketViewModel) viewModel).getEmployeeByName(tech.getText()));
                    stringsList.add(tech.getText());
                    viewModel.setFieldModified(true);
                    //add technician to ticket
                    ((TicketViewModel) viewModel).addTechnicianToTicket(namesAndTechs.get(tech.getText()));
                } else {
                    //technicians.remove(((TicketViewModel) viewModel).getEmployeeByName(tech.getText()));
                    stringsList.remove(tech.getText());
                    viewModel.setFieldModified(true);
                    //add technician to ticket
                    ((TicketViewModel) viewModel).removeTechnician(namesAndTechs.get(tech.getText()));
                }
                // prevents an unnecessary modifyTicket and prevents a useless entry in tickethistory  
                if(((TicketViewModel) viewModel).getTechniciansAsignedToTicket().stream().sorted().collect(Collectors.toList())
                		.equals(technicians.stream().sorted().collect(Collectors.toList()))){
                	viewModel.setFieldModified(false);
                }
                listView.setMaxHeight(((TicketViewModel) viewModel).getTechniciansAsignedToTicket().size()*25+25);
            });
        }

        //create Listview for technicians for ticket
        listView.setMaxHeight(((TicketViewModel) viewModel).getTechniciansAsignedToTicket().size()*25+25);		
        listView.getStylesheets().add("file:src/start/styles.css");
        listView.setId("list-view");
        listView.setSelectionModel(null);
        // only show menuButton when ticket is outstanding
        // hide it when ticket is resolved
        if (!TicketStatus.isOutstanding()) {        	
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
