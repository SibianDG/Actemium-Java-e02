package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import domain.ActemiumEmployee;
import domain.Customer;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


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
        txtDetailsTitle.setText("Nothing is selected");
        gridDetails.getChildren().clear();
        btnModify.setVisible(false);
        btnDelete.setVisible(false);
        txtErrorMessage.setVisible(false);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        viewModel.delete();
        clearDetailPane();
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

                            //String username, String password, String firstName, String lastName, String status, String companyName, String companyPhone, String companyAddress
                            ((UserViewModel) viewModel).modifyCustomer(
                                    getTextFromGridItem(1)
                                    , getTextFromGridItem(2)
                                    , getTextFromGridItem(8)
                                    , getTextFromGridItem(9)
                                    , getTextFromGridItem(11)
                                    , getTextFromGridItem(4)
                                    , getTextFromGridItem(6)
                                    , getTextFromGridItem(5)
                            );
                        }
                        makePopUp("User edited", "You have successfully edited the user.");
                    } else {
                        makePopUp("User not edited", "You haven't changed anything.");
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
                    if(viewModel.isFieldModified()){
                        ((TicketViewModel) viewModel).modifyTicket(
                                // priority, ticketType, title, description, remarks, attachments, technicians
                                TicketPriority.valueOf(getTextFromGridItem(2))
                                , TicketType.valueOf(getTextFromGridItem(3))
                                , TicketStatus.valueOf(getTextFromGridItem(4))
                                , getTextFromGridItem(0)
                                , getTextFromGridItem(5)
                                , getTextFromGridItem(7)
                                , getTextFromGridItem(8)
                                , new ArrayList<ActemiumEmployee>()
                        );
                        makePopUp("Ticket edited", "You have successfully edited the ticket.");
                    } else {
                        makePopUp("Ticket not edited", "You haven't changed anything.");
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
                        System.out.println("Edited ContractTypeViewModel");
                        makePopUp("Contract type edited", "You have successfully edited the Contract type.");
                    } else {
                        makePopUp("Contract type not edited", "You haven't changed anything.");
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
            }
            editing = false;
            viewModel.setFieldModified(false);
            setDetailOnModifying();

            //TODO: handle the correct error messages, not just all
        } catch (Exception e){
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
        } else if(node instanceof TextField){
            text = ((TextField)node).getText();
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
            clearDetailPane();
        }
    }

    private void setDetailOnModifying(){
        gridDetails.getChildren().clear();
        if (viewModel instanceof UserViewModel) {
            addGridDetails(((UserViewModel) viewModel).getDetails());
            txtDetailsTitle.setText("Details of " + ((UserViewModel) viewModel).getNameOfSelectedUser());
            btnModify.setText("Modify " + ((UserViewModel) viewModel).getCurrentState().toString().toLowerCase());
        } else if (viewModel instanceof TicketViewModel) {
            addGridDetails(((TicketViewModel) viewModel).getDetails());
            txtDetailsTitle.setText("Details of ticket: " + ((TicketViewModel) viewModel).getIdOfSelectedTicket());
            btnModify.setText("Modify Ticket");
        } else if (viewModel instanceof ContractTypeViewModel) {
            addGridDetails(((ContractTypeViewModel) viewModel).getDetails());
            txtDetailsTitle.setText("Details of ContractType: " + ((ContractTypeViewModel) viewModel).getNameSelectedContractType());
            btnModify.setText("Modify ContractType");
        }
        btnModify.setVisible(true);
        btnDelete.setVisible(true);
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
                txtDetailsTitle.setText("Add new employee");
                btnModify.setText("Add new employee");
            } else if (((UserViewModel) viewModel).getCurrentState().equals(GUIEnum.CUSTOMER)) {
                fields = ((UserViewModel) viewModel).getDetailsNewCustomer();
                txtDetailsTitle.setText("Add new customer");
                btnModify.setText("Add new customer");
            } else {
                fields = null;
            }
            btnModify.setVisible(true);
            assert fields != null;
            addItemsToGridNewUser(fields);
        } else if (viewModel instanceof TicketViewModel) {
            if (((TicketViewModel) viewModel).getCurrentState().equals(GUIEnum.TICKET)) {
                fields = ((TicketViewModel) viewModel).getDetailsNewTicket();
                txtDetailsTitle.setText("Add new ticket");
                btnModify.setText("Add new ticket");
            } else {
                fields = null;
            }
            btnModify.setVisible(true);
            assert fields != null;
            addItemsToGridNewTicket(fields);
        } else if (viewModel instanceof ContractTypeViewModel) {
            if (((ContractTypeViewModel) viewModel).getCurrentState().equals(GUIEnum.CONTRACTTYPE)) {
                fields = ((ContractTypeViewModel) viewModel).getDetailsNewContractType();
                txtDetailsTitle.setText("Add new contract type");
                btnModify.setText("Add new contract type");
            } else {
                fields = null;
            }
            btnModify.setVisible(true);
            assert fields != null;
            addItemsToGridNewContractType(fields);
        }
    }

    //todo alles in 1 methode brengen
    private void addNewItemToGrid(ArrayList<String> fields){
        gridDetails.getChildren().clear();
        gridDetails.addColumn(0);
        gridDetails.addColumn(1);

        //addGridDetails();

    }

    private void addItemsToGridNewUser(ArrayList<String> fields){
        gridDetails.getChildren().clear();
        gridDetails.addColumn(0);
        gridDetails.addColumn(1);

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

            gridDetails.add(makeNewLabel(fields.get(i)), 0, i);

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
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }

    private void addItemsToGridNewTicket(ArrayList<String> fields){
        gridDetails.getChildren().clear();
        gridDetails.addColumn(0);
        gridDetails.addColumn(1);

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

            gridDetails.add(makeNewLabel(fields.get(i)), 0, i);

            Node node;
            if (fields.get(i).toLowerCase().contains("priority")) {
                node = makeComboBox(TicketPriority.P3);
            } else if (fields.get(i).toLowerCase().contains("type")) {
                node = makeComboBox(TicketType.OTHER);
            } else {
                TextField textField;
                textField = new TextField(randomValues.get(i));
                textField.setFont(Font.font("Arial", 14));
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }

    private void addItemsToGridNewContractType(ArrayList<String> fields){
        gridDetails.getChildren().clear();
        gridDetails.addColumn(0);
        gridDetails.addColumn(1);

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

            gridDetails.add(makeNewLabel(itemName), 0, i);

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
                node = textField;
            }
            gridDetails.add(node, 1, i);
        }
    }

    private Label makeNewLabel(String text){
        Label label = new Label(text+":");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setTextAlignment(TextAlignment.RIGHT);
        label.setTextFill(Color.rgb(29, 61, 120));
        return label;
    }

    private void addGridDetails(Map<String, Map<Boolean, Object>> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key);

            Node detail = createElementDetailGridpane(details.get(key), key);
            gridDetails.add(label, 0, i);
            gridDetails.add(detail, 1, i);
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
            TextField detail = new TextField(string);
            detail.textProperty().addListener((observable, oldValue, newValue) -> {
                viewModel.setFieldModified(true);
                System.out.println("textfield changed from " + oldValue + " to " + newValue);
            });
            detail.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

            if (string.trim().equals("")){
                detail.setVisible(false);
                detail.setPadding(new Insets(15, 0, 0, 0));
            }
            detail.setDisable(disable);

            node = detail;
        } else if (o instanceof Enum) {
            node = makeComboBox(o);
        } else if (o instanceof Boolean) {
            node = makeComboBox(o);
        }
        if (node != null)
            node.setDisable(disable);
        return node;
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

    private void makePopUp(String headerText, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.setHeaderText(headerText);
        alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        alert.showAndWait();
    }

}
