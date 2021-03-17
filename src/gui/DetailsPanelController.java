package gui;

import static javafx.scene.paint.Color.BLACK;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.Contract;
import domain.Employee;
import domain.enums.ContractStatus;
import domain.enums.ContractTypeStatus;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.enums.Timestamp;
import domain.enums.UserStatus;
import exceptions.InformationRequiredException;
import gui.viewModels.ContractTypeViewModel;
import gui.viewModels.ContractViewModel;
import gui.viewModels.KnowledgeBaseViewModel;
import gui.viewModels.TicketViewModel;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import languages.LanguageResource;


public class DetailsPanelController extends GridPane implements InvalidationListener {

    private final ViewModel viewModel;
    private boolean editing = false;

    @FXML
    private Text txtDetailsTitle;

    @FXML
    private GridPane gridDetails;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnModify;
    
    @FXML
    private HBox hBoxModify;

    @FXML
    private Text txtErrorMessage;

    public DetailsPanelController(ViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gridDetails.setHgap(5);
        gridDetails.setVgap(5);
        // TODO not working, error viewModel is null ?? how?
        // This whole if block is for the title when nothing is selected
//		if (viewModel instanceof UserViewModel) {
//			if (((UserViewModel) viewModel).getCurrentState()
//					.equals(GUIEnum.EMPLOYEE)) {
//		        txtDetailsTitle.setText("No employee is selected");
//			} else if (((UserViewModel) viewModel).getCurrentState()
//					.equals(GUIEnum.CUSTOMER)) {
//		        txtDetailsTitle.setText("No customer is selected");
//			} else {
//		        txtDetailsTitle.setText("No user is selected");
//			}
//		} else if (viewModel instanceof TicketViewModel) {
//	        txtDetailsTitle.setText("No ticket is selected");
//		}
        clearDetailPane();
    }

    public void clearDetailPane() {
        txtDetailsTitle.setText(LanguageResource.getString("nothingSelected"));
        gridDetails.getChildren().clear();
        btnModify.setVisible(false);
        btnDelete.setVisible(false);
        txtErrorMessage.setVisible(false);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            viewModel.delete();
        } catch (InformationRequiredException ire) {
            StringBuilder errorMessage = new StringBuilder();
            ire.getInformationRequired().forEach(e -> {
                System.out.println(e);
                errorMessage.append(e).append("\n");

            });
            txtErrorMessage.setText(errorMessage.toString());
            txtErrorMessage.setVisible(true);
            clearDetailPane();
        }
    }

    @FXML
    void btnModifyOnAction(ActionEvent event) {

        try {
            if (viewModel instanceof UserViewModel) {
                if (editing) {
                    if(viewModel.isFieldModified()){
                        if (((UserViewModel) viewModel).getCurrentState().equals(GUIEnum.EMPLOYEE)){
                            ((UserViewModel) viewModel).modifyEmployee(
                                    getTextFromGridItem(1)
                                    , getTextFromGridItem(2)
                                    , getTextFromGridItem(3)
                                    , getTextFromGridItem(4)
                                    , getTextFromGridItem(5)
                                    , getTextFromGridItem(6)
                                    , getTextFromGridItem(7)
                                    , EmployeeRole.valueOf(getTextFromGridItem(9))
                                    , UserStatus.valueOf(getTextFromGridItem(10))
                            );
                        } else if (((UserViewModel) viewModel).getCurrentState().equals(GUIEnum.CUSTOMER)){
// username, password, firstName, lastName, status, companyName, companyCountry, companyCity, companyAddress, companyPhone
                            ((UserViewModel) viewModel).modifyCustomer(
                                    getTextFromGridItem(1)
                                    , getTextFromGridItem(2)
                                    , getTextFromGridItem(10)
                                    , getTextFromGridItem(11)
                                    , getTextFromGridItem(13)
                                    , getTextFromGridItem(4)
                                    , getTextFromGridItem(5)
                                    , getTextFromGridItem(6)
                                    , getTextFromGridItem(7)
                                    , getTextFromGridItem(8)
                            );
                        }
//                      makePopUp("User edited", "You have successfully edited the user.");
						showPopupMessage("popupSuccess", "You have successfully edited the user.");
					} else {
//                      makePopUp("User not edited", "You haven't changed anything.");
						showPopupMessage("popupWarning", "You haven't changed anything.");
					}
                } else {
                    if (((UserViewModel) viewModel).getCurrentState().equals(GUIEnum.EMPLOYEE)){

                        ((UserViewModel) viewModel).registerEmployee(
                                getTextFromGridItem(0)
                                , getTextFromGridItem(1)
                                , getTextFromGridItem(2)
                                , getTextFromGridItem(3)
                                , getTextFromGridItem(4)
                                , getTextFromGridItem(5)
                                , EmployeeRole.valueOf(getTextFromGridItem(6))
                        );
                    } else if (((UserViewModel) viewModel).getCurrentState().equals(GUIEnum.CUSTOMER)){
                        ((UserViewModel) viewModel).registerCustomer(
                                getTextFromGridItem(0)
                                , getTextFromGridItem(1)
                                , getTextFromGridItem(2)
                                , getTextFromGridItem(3)
                                , getTextFromGridItem(4)
                                , getTextFromGridItem(5)
                                , getTextFromGridItem(6)
                                , getTextFromGridItem(7)
                        );
                    }
                }
                //TODO
                // This is just a draft version, nothing is correct here,
                // everything still needs to be implemented properly
                // (but you can already modify a ticket
                // and click on add new ticket)
            } else if (viewModel instanceof TicketViewModel) {
                if (editing) {
                    if(viewModel.isFieldModified() && TicketStatus.isOutstanding()){
                        ((TicketViewModel) viewModel).modifyTicketOutstanding(
                                // priority, ticketType, title, description, remarks, attachments, technicians
                                TicketPriority.valueOf(getTextFromGridItem(3))
                                , TicketType.valueOf(getTextFromGridItem(4))
                                , TicketStatus.valueOf(getTextFromGridItem(5))
                                , getTextFromGridItem(0)
                                , getTextFromGridItem(6)
                                , getTextFromGridItem(9)
                                , getTextFromGridItem(10)
                                , ((TicketViewModel) viewModel).getTechniciansAsignedToTicket()
                        );
//                        makePopUp(LanguageResource.getString("ticketEdited"), LanguageResource.getString("ticketEdited_succes"));
						showPopupMessage("popupSuccess", LanguageResource.getString("ticketEdited_succes"));
                    } else if (viewModel.isFieldModified() && !TicketStatus.isOutstanding()){
                        ((TicketViewModel) viewModel).modifyTicketResolved(
                                // solution, quality, supportNeeded
                                getTextFromGridItem(13)
                                , getTextFromGridItem(14)
                                , getTextFromGridItem(15)
                        );
//                        makePopUp(LanguageResource.getString("ticketEdited"), LanguageResource.getString("ticketEdited_succes"));
						showPopupMessage("popupSuccess", LanguageResource.getString("ticketEdited_succes"));
                    } else {
//                        makePopUp(LanguageResource.getString("ticketEdited_false"), LanguageResource.getString("unchangedMessage"));
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
                }
            } else if (viewModel instanceof ContractTypeViewModel) {
                if (editing) {
                    if(viewModel.isFieldModified()){
                        ((ContractTypeViewModel) viewModel).modifyContractType(
                                //        (String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                //boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price)
                                // priority, ticketType, title, description, remarks, attachments, technicians
                                getTextFromGridItem(0), //name
                                ContractTypeStatus.valueOf(getTextFromGridItem(1)), //staus
                                Boolean.parseBoolean(getTextFromGridItem(2)), //email
                                Boolean.parseBoolean(getTextFromGridItem(3)), //phone
                                Boolean.parseBoolean(getTextFromGridItem(4)), //application
                                Timestamp.valueOf(getTextFromGridItem(5)), //Timestamp
                                Integer.parseInt(getTextFromGridItem(6)), //max hand time
                                Integer.parseInt(getTextFromGridItem(7)), //min troughputtime contract
                                Double.parseDouble(getTextFromGridItem(8).replace(",", ".")) //price contract
                        );
//                        makePopUp(LanguageResource.getString("contractTypeEdited"), LanguageResource.getString("contractTypeEdited_succes"));
						showPopupMessage("popupSuccess", LanguageResource.getString("contractTypeEdited_succes"));
                    } else {
//                        makePopUp(LanguageResource.getString("contractTypeEdited_false"), LanguageResource.getString("unchangedMessage"));
						showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));
                    }
                } else {
                    ((ContractTypeViewModel) viewModel).registerContractType(
//"Name", "Status", "Email", "Phone", "Application", "Timestamp ticket creation", "Max handling time", "Min throughput time contract", "Price contract"
                            getTextFromGridItem(0),
                            ContractTypeStatus.valueOf(getTextFromGridItem(1)),
                            Boolean.parseBoolean(getTextFromGridItem(2)),
                            Boolean.parseBoolean(getTextFromGridItem(3)),
                            Boolean.parseBoolean(getTextFromGridItem(4)),
                            Timestamp.valueOf(getTextFromGridItem(5)),
                            Integer.parseInt(getTextFromGridItem(6)),
                            Integer.parseInt(getTextFromGridItem(7)),
                            Double.parseDouble(getTextFromGridItem(8).replace(",", "."))
                    );
                }
            } else if (viewModel instanceof ContractViewModel) {
                if (editing) {
                    if(viewModel.isFieldModified()){
                        ((ContractViewModel) viewModel).modifyContract(
                                // Only the status can be modified in case a customer didnt pay his bills                        		
                                ContractStatus.valueOf(getTextFromGridItem(3))
                        );
//                        makePopUp(LanguageResource.getString("contractEdited"), LanguageResource.getString("contractEdited_succes"));
						showPopupMessage("popupSuccess", LanguageResource.getString("contractEdited_succes"));
                    } else {
//                        makePopUp(LanguageResource.getString("contractEdited_false"), LanguageResource.getString("unchangedMessage"));
						showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));
                    }
                } else {
                    //TODO entering customerID should show corresponding company name
                    ((ContractViewModel) viewModel).registerContract(
                            // customerId, contractTypeName, startDate, duration
                            Long.parseLong(getTextFromGridItem(0))
                            , getTextFromGridItem(1)
                            , LocalDate.parse(getTextFromGridItem(2)) //startDate DatePicker
                            , Integer.parseInt(getTextFromGridItem(3))
                    );
                }
            } else if (viewModel instanceof KnowledgeBaseViewModel) {
                if (editing) {
                    if(viewModel.isFieldModified()){
                        ((KnowledgeBaseViewModel) viewModel).modifyKbItem(
                                getTextFromGridItem(0)
                                , KbItemType.valueOf(getTextFromGridItem(1))
                                , getTextFromGridItem(2)                                
                        );
						showPopupMessage("popupSuccess", LanguageResource.getString("kbItemEdited_succes"));
                    } else {
						showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));
                    }
                } else {
                    ((KnowledgeBaseViewModel) viewModel).registerKbItem(
                            // title, type, text
                    		getTextFromGridItem(0)
                            , KbItemType.valueOf(getTextFromGridItem(1))
                            , getTextFromGridItem(2)   
                    );
                }
            }
            editing = false;
            viewModel.setFieldModified(false);
            setDetailOnModifying();

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

    private String getTextFromGridItem(int i){
        String text;
        Node node = gridDetails.getChildren().get(2*i+1);

        if (node instanceof ComboBox){
            text = ((ComboBox)node).getSelectionModel().getSelectedItem().toString();
        } else if(node instanceof DatePicker){
            text = ((DatePicker)node).getValue().toString();
        } else if(node instanceof TextField){
            text = ((TextField)node).getText();
        } else if(node instanceof TextArea){
            text = ((TextArea)node).getText();
        } else {
            text = "";
        }

        return text;
    }

    @Override
    public void invalidated(Observable observable) {
        try {
            setDetailOnModifying();
        } catch (NullPointerException e){
            setupPaneNewObject();
//            clearDetailPane();
        }
    }

    private void setDetailOnModifying(){
        initGridDetails();

        if (viewModel instanceof UserViewModel) {
            addGridDetails(((UserViewModel) viewModel).getDetails());
            txtDetailsTitle.setText("Details of " + ((UserViewModel) viewModel).getNameOfSelectedUser());
            btnModify.setText("Modify " + ((UserViewModel) viewModel).getCurrentState().toString().toLowerCase());
            btnDelete.setVisible(true);
        } else if (viewModel instanceof TicketViewModel) {
            if (TicketStatus.isOutstanding()) {
                addGridDetails(((TicketViewModel) viewModel).getDetailsOutstanding());
            } else {
                addGridDetails(((TicketViewModel) viewModel).getDetailsResolved());
            }
            txtDetailsTitle.setText("Details of ticket: " + ((TicketViewModel) viewModel).getIdSelectedTicket());
            btnModify.setText("Modify Ticket");
            btnDelete.setVisible(true);
        } else if (viewModel instanceof ContractTypeViewModel) {
            addGridDetails(((ContractTypeViewModel) viewModel).getDetails());
            txtDetailsTitle.setText("Details of ContractType: " + ((ContractTypeViewModel) viewModel).getNameSelectedContractType());
            btnModify.setText("Modify ContractType");
            btnDelete.setVisible(true);
        } else if (viewModel instanceof ContractViewModel) {
            addGridDetails(((ContractViewModel) viewModel).getDetails());
            txtDetailsTitle.setText("Details of Contract: " + ((ContractViewModel) viewModel).getIdSelectedContract());
            btnModify.setText("Modify Contract");
            btnDelete.setVisible(false);
        } else if (viewModel instanceof KnowledgeBaseViewModel) {
            addGridDetails(((KnowledgeBaseViewModel) viewModel).getDetails());
            txtDetailsTitle.setText("Title: " + ((KnowledgeBaseViewModel) viewModel).getTitleSelectedKbItem());
            btnModify.setText("Modify KB item");
            btnDelete.setVisible(false);
        }
        btnModify.setVisible(true);
        txtErrorMessage.setVisible(false);
        editing = true;

    }

    private void setupPaneNewObject(){
        editing = false;
        ArrayList<String> fields = null;

        viewModel.setFieldModified(true);


        if (viewModel instanceof UserViewModel) {
            if (((UserViewModel) viewModel).getCurrentState().equals(GUIEnum.EMPLOYEE)) {
                fields = ((UserViewModel) viewModel).getDetailsNewEmployee();
                txtDetailsTitle.setText(LanguageResource.getString("addEmployee"));
                btnModify.setText(LanguageResource.getString("addEmployee"));
            } else if (((UserViewModel) viewModel).getCurrentState().equals(GUIEnum.CUSTOMER)) {
                fields = ((UserViewModel) viewModel).getDetailsNewCustomer();
                txtDetailsTitle.setText(LanguageResource.getString("addCustomer"));
                btnModify.setText(LanguageResource.getString("addCustomer"));
            } else {
                fields = null;
            }
            btnModify.setVisible(true);
            assert fields != null;
            addItemsToGridNewUser(fields);
        } else if (viewModel instanceof TicketViewModel) {
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
        } else if (viewModel instanceof ContractTypeViewModel) {
            if (((ContractTypeViewModel) viewModel).getCurrentState().equals(GUIEnum.CONTRACTTYPE)) {
                fields = ((ContractTypeViewModel) viewModel).getDetailsNewContractType();
                txtDetailsTitle.setText(LanguageResource.getString("addContractType"));
                btnModify.setText(LanguageResource.getString("addContractType"));
            } else {
                fields = null;
            }
            btnModify.setVisible(true);
            assert fields != null;
            addItemsToGridNewContractType(fields);
        } else if (viewModel instanceof ContractViewModel) {
            if (((ContractViewModel) viewModel).getCurrentState().equals(GUIEnum.CONTRACT)) {
                fields = ((ContractViewModel) viewModel).getDetailsNewContract();
                txtDetailsTitle.setText(LanguageResource.getString("addContract"));
                btnModify.setText(LanguageResource.getString("addContract"));
            } else {
                fields = null;
            }
            btnModify.setVisible(true);
            assert fields != null;
            addItemsToGridNewContract(fields);
        } else if (viewModel instanceof KnowledgeBaseViewModel) {
            if (((KnowledgeBaseViewModel) viewModel).getCurrentState().equals(GUIEnum.KNOWLEDGEBASE)) {
                fields = ((KnowledgeBaseViewModel) viewModel).getDetailsNewKbItem();
                txtDetailsTitle.setText(LanguageResource.getString("addKbItem"));
                btnModify.setText(LanguageResource.getString("addKbItem"));
            } else {
                fields = null;
            }
            btnModify.setVisible(true);
            assert fields != null;
            addItemsToGridNewKbItem(fields);
        }
    }

    //todo alles in 1 methode brengen
    private void addNewItemToGrid(ArrayList<String> fields){
        gridDetails.getChildren().clear();
        gridDetails.getColumnConstraints().clear();
        gridDetails.addColumn(0);
        gridDetails.addColumn(1);

//        addGridDetails();

    }

    private void addItemsToGridNewUser(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> randomValues = Map.of(
                0, "Username9999"
                , 1, "FirstNameeee"
                , 2, "LastNameeee"
                , 3, "Stationstraat 99"
                , 4, "test@gmail.com"
                , 5, "094812384"
                , 6, EmployeeRole.SUPPORT_MANAGER.toString()
        );
        int randomValuesCounter = 0;
        for (int i = 0; i < fields.size(); i++) {
            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(fields.get(i), true), 0, i);

            Node node;
            if (fields.get(i).toLowerCase().contains("role")){
                node = makeComboBox(EmployeeRole.ADMINISTRATOR);
            } else {
                TextField textField;
                if(fields.size() <= randomValues.size()){
                    textField = new TextField(randomValues.get(randomValuesCounter++));
                } else {
                    textField = new TextField();
                }
                textField.setFont(Font.font("Arial", 14));
                textField.setPromptText(fields.get(i));
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }

    private void addItemsToGridNewTicket(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> randomValues = Map.of(
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
            if (fields.get(i).toLowerCase().contains("priority")) {
                node = makeComboBox(TicketPriority.P3);
            } else if (fields.get(i).toLowerCase().contains("type")) {
                node = makeComboBox(TicketType.OTHER);
            } else {
                TextField textField;
                textField = new TextField(randomValues.get(i));
                textField.setFont(Font.font("Arial", 14));
                textField.setPromptText(fields.get(i));
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }

    private void addItemsToGridNewContractType(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> randomValues = Map.of(
                0, "ContractTypeeeeee"
                , 1, "12"
                , 2, "2"
                , 3, "500"
        );

        int randomValuesCounter = 0;

        for (int i = 0; i < fields.size(); i++) {
            String itemName = fields.get(i);

            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(itemName, true), 0, i);

            itemName = itemName.toLowerCase();

            Node node;
            if (itemName.contains("status")) {
                node = makeComboBox(ContractTypeStatus.ACTIVE);
            } else if (itemName.contains("timestamp")) {
                node = makeComboBox(Timestamp.ALWAYS);
            } else if (itemName.contains("email")
                    || itemName.contains("phone")
                    || itemName.contains("application")){
                node = makeComboBox(false);
            } else {
                TextField textField;
                textField = new TextField(randomValues.get(randomValuesCounter++));
                textField.setFont(Font.font("Arial", 14));
                textField.setPromptText(fields.get(i));
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }

    private void addItemsToGridNewContract(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> randomValues = Map.of(
                0, "006"
                , 1, "BasicPhoneSupport"
//                , 2, LocalDate.now().toString()
                , 2, "3"
        );

        int randomValuesCounter = 0;

        for (int i = 0; i < fields.size(); i++) {
            String itemName = fields.get(i);

            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(itemName, true), 0, i);

            itemName = itemName.toLowerCase();

            Node node;
            if (itemName.contains("start")) {
                node = makeDatePicker(LocalDate.now());
            } else {
                TextField textField;
                textField = new TextField(randomValues.get(randomValuesCounter++));
                textField.setFont(Font.font("Arial", 14));
                textField.setPromptText(fields.get(i));
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }
    
    private void addItemsToGridNewKbItem(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> randomValues = Map.of(
                0, "How to Solve Network Problems"
                , 1, KbItemType.OTHER.toString()
                , 2, "Sample text"
        );

        for (int i = 0; i < fields.size(); i++) {
            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(fields.get(i), true), 0, i);

            Node node;
            if (fields.get(i).toLowerCase().contains("text")) {
            	TextArea textArea;
            	textArea = new TextArea(randomValues.get(i));
            	textArea.setFont(Font.font("Arial", 14));
            	textArea.setPromptText(fields.get(i));
                node = textArea;
            } else if (fields.get(i).toLowerCase().contains("type")) {
                node = makeComboBox(KbItemType.OTHER);
            } else {
                TextField textField;
                textField = new TextField(randomValues.get(i));
                textField.setFont(Font.font("Arial", 14));
                textField.setPromptText(fields.get(i));
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }

    private Label makeNewLabel(String text, boolean withColon){
        Label label = new Label(text+ (withColon ? ":" : ""));
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setTextAlignment(TextAlignment.RIGHT);
        if (withColon)
            label.setTextFill(Color.rgb(29, 61, 120));
        else {
            label.setTextFill(BLACK);
        }
        return label;
    }

    private void addGridDetails(Map<String, Map<Boolean, Object>> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key, true);

            Node detail = createElementDetailGridpane(details.get(key), key);
            if (key.toLowerCase().contains("contracts")){
                gridDetails.add(label, 0, i);
                gridDetails.add(detail, 0, i+1);
                setColumnSpan(detail, 2);
            } else {
                gridDetails.add(label, 0, i);
                gridDetails.add(detail, 1, i);
            }
            i++;
        }
    }

    private Node createElementDetailGridpane(Map<Boolean, Object> map, String key) {
        boolean disable = (boolean) map.keySet().toArray()[0];
        Object o = map.get(disable);

        disable = !disable;

        Node node = null;

        if (o instanceof String) {
            String string = (String) o;
            if (key.equals("Password")){
                string = "********";
            }
            
            //TODO transform into generic method
            if(key.equals("Text")
            		|| key.equals("Description")) {
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
                detail.setDisable(disable);
                detail.setPromptText(key);

	            node = detail;
            } else {
            	TextField detail = new TextField(string);
	            
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
	            detail.setDisable(disable);
	            detail.setPromptText(key);
	
	            node = detail;
	        }
        } else if (o instanceof Enum) {
            node = makeComboBox(o);
        } else if (o instanceof Boolean) {
            node = makeComboBox(o);
        } else if (o instanceof ObservableList) {
            if(viewModel instanceof TicketViewModel)
                node = makeViewTechnicians(o);
            else if(viewModel instanceof UserViewModel){
                if(((ObservableList) o).size() > 0)
                    node = makeTableView(o);
                else
                    node = makeNewLabel("No items available", false);
            }
        } else if (o instanceof LocalDate) {
            node = makeDatePicker(o);
        }
        if (node != null)
            node.setDisable(disable);
        return node;
    }

    private <T> Node makeTableView(Object o) {
        //ObservableList<String> list = FXCollections.observableArrayList(contracts.stream().map(c -> c.toString()).collect(Collectors.toList()));
        ObservableList<Contract> list = (ObservableList<Contract>) o;
        TableView<Contract> tableView = new TableView<>(list);

//return String.format("%s: %s %s %s until %s", this.getContractIdString(), this.contractType.getName(), this.getStatus(), this.getStartDate().toString(), this.getEndDate().toString());

        TableColumn<Contract, Number> columnID = new TableColumn<>("ID");
        columnID.setCellValueFactory(cellData -> cellData.getValue().contractIdProperty());

        TableColumn<Contract, String> columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(cellData -> cellData.getValue().contractTypeNameProperty());

        TableColumn<Contract, String> columnStatus = new TableColumn<>("Status");
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().contractStatusProperty());

        TableColumn<Contract, String> columnStartDate = new TableColumn<>("Start Date");
        columnStartDate.setCellValueFactory(cellData -> cellData.getValue().contractStartDateProperty());

        TableColumn<Contract, String> columnEndDate = new TableColumn<>("End Date");
        columnEndDate.setCellValueFactory(cellData -> cellData.getValue().contractEndDateProperty());

        tableView.getColumns().add(columnID);
        tableView.getColumns().add(columnName);
        tableView.getColumns().add(columnStatus);
        tableView.getColumns().add(columnStartDate);
        tableView.getColumns().add(columnEndDate);

        tableView.setSelectionModel(null);
        tableView.getStyleClass().add("ignoreHover");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableView;
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
        MenuButton menuButton = new MenuButton("Select technician");
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
                listView.setMaxHeight(((TicketViewModel) viewModel).getTechniciansAsignedToTicket().size()*25+25);
            });
        }

        //create Listview for technicians for ticket
        listView.setMaxHeight(((TicketViewModel) viewModel).getTechniciansAsignedToTicket().size()*25+25);
        listView.getStylesheets().add("file:src/start/styles.css");
        listView.setId("list-view");
        listView.setSelectionModel(null);
        vBox.getChildren().addAll(menuButton, listView);

        return vBox;
    }

    private ComboBox makeComboBox(Object o){


        ObservableList list;
        if (o instanceof Boolean){
            list = FXCollections.observableList(Arrays.asList(true, false));
        } else {
            switch(o.getClass().getSimpleName()) {
                case "UserStatus" -> list = FXCollections.observableList(Arrays.asList(UserStatus.values()));
                case "EmployeeRole" -> list = FXCollections.observableList(Arrays.asList(EmployeeRole.values()));
                case "TicketPriority" -> list = FXCollections.observableList(Arrays.asList(TicketPriority.values()));
                case "TicketType" -> list = FXCollections.observableList(Arrays.asList(TicketType.values()));
                case "TicketStatus" -> list = FXCollections.observableList(Arrays.asList(TicketStatus.values()));
                case "ContractStatus" -> list = FXCollections.observableList(Arrays.asList(ContractStatus.values()));
                case "ContractTypeStatus" -> list = FXCollections.observableList(Arrays.asList(ContractTypeStatus.values()));
                case "Timestamp" -> list = FXCollections.observableList(Arrays.asList(Timestamp.values()));
                case "KbItemType" -> list = FXCollections.observableList(Arrays.asList(KbItemType.values()));
                default -> list = FXCollections.observableList(Arrays.asList(UserStatus.values()));
            }
        }
        ComboBox c = new ComboBox(list);
        c.getSelectionModel().select(o);
        c.valueProperty().addListener(e -> {
            viewModel.setFieldModified(true);
        });

        return c;
    }

    private DatePicker makeDatePicker(Object o) {

        // Create the DatePicker.
        DatePicker datePicker = new DatePicker((LocalDate) o);

        String pattern = "yyyy-MM-dd";

        datePicker.setPromptText(pattern.toLowerCase());

        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        // Add some action (in Java 8 lambda syntax style).
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
        });

        // Add the DatePicker to the Stage.
//    	StackPane root = new StackPane();
//    	root.getChildren().add(datePicker);
//    	stage.setScene(new Scene(root, 500, 650));
//    	stage.show();
        return datePicker;
    }

    // replaced by showPopUpMessage
    private void makePopUp(String headerText, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.setHeaderText(headerText);
        alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        alert.showAndWait();
    }
    
    public Popup createPopup(final String message, String popupType) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                popup.hide();
            }
        });
//        label.getStylesheets().add("/css/styles.css");
        label.getStylesheets().add("file:src/start/styles.css");
        label.getStyleClass().add(popupType);
        popup.getContent().add(label);
        return popup;
    }

    //TODO fix positioning
    // Can it be relative to a button such as btnModify or btnAdd
    // Temp fix
	public void showPopupMessage(final String popupType, String message) {
        final Popup popup = createPopup(message, popupType);
        final Stage stage = (Stage) gridDetails.getScene().getWindow();
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
				popup.setX(1300 - popup.getWidth()/2);
				popup.setY(260 + hBoxModify.getLayoutY() - popup.getHeight()/2);
            }
        });
        popup.show(stage);
    }

    private void initGridDetails() {
        gridDetails.getChildren().clear();
        gridDetails.getColumnConstraints().clear();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(30);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);

        gridDetails.getColumnConstraints().addAll(col0,col1);
    }
    
}
