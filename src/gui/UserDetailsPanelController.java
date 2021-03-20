package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import domain.Contract;
import domain.enums.EmployeeRole;
import domain.enums.UserStatus;
import exceptions.InformationRequiredException;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import languages.LanguageResource;

//TODO
// this class can be split up in :
// EmployeeDetailsPanelController
// and
// CustomerDetailsPanelController
// Should we do this?

public class UserDetailsPanelController extends DetailsPanelController { 

    public UserDetailsPanelController(ViewModel viewModel, GridPane gridContent) {
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
    void btnModifyOnAction(ActionEvent event) {
        try {
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
					showPopupMessage("popupSuccess", LanguageResource.getString("user_edit_success"));
				} else {
					showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));
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
                    showPopupMessageAddItem("popupSuccess", LanguageResource.getString("employee_registred_success"));
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
                    showPopupMessageAddItem("popupSuccess", LanguageResource.getString("customer_registred_success"));
                }
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
        
        addGridDetails(((UserViewModel) viewModel).getDetails());
        txtDetailsTitle.setText(String.format("%s %s", LanguageResource.getString("details_of"), ((UserViewModel) viewModel).getNameOfSelectedUser()));
        btnModify.setText(String.format("%s %s", LanguageResource.getString("modify") ,((UserViewModel) viewModel).getCurrentState().toString().toLowerCase()));
        btnDelete.setVisible(true);
        
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
            if (fields.get(i).toLowerCase().contains(LanguageResource.getString("role").toLowerCase())){
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

    private void addGridDetails(Map<String, Map<Boolean, Object>> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key, true);

            Node detail = createElementDetailGridpane(details.get(key), key);
            if (key.toLowerCase().contains(LanguageResource.getString("contracts").toLowerCase())){
                gridDetails.add(makeNewLabel(key, true), 0, i);
                gridDetails.add(detail, 0, i+1);
                setColumnSpan(detail, 2);
            } else {
                gridDetails.add(makeNewLabel(key, true), 0, i);
                gridDetails.add(detail, 1, i);
            }
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
            if (key.equalsIgnoreCase(LanguageResource.getString("password"))){
                string = "********";
            }
           
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
        } else if (o instanceof Enum) {
            node = makeComboBox(o);
            node.setDisable(disable);
        } else if (o instanceof ObservableList) {
            if(((ObservableList) o).size() > 0)
                node = makeTableViewContractsInCustomers(o);
            else
                node = makeNewLabel(LanguageResource.getString("no_items_available"), false);            
        } 
        return node;
    }

    private <T> Node makeTableViewContractsInCustomers(Object o) {
        //ObservableList<String> list = FXCollections.observableArrayList(contracts.stream().map(c -> c.toString()).collect(Collectors.toList()));
        ObservableList<Contract> list = (ObservableList<Contract>) o;
        TableView<Contract> tableView = new TableView<>(list);

//return String.format("%s: %s %s %s until %s", this.getContractIdString(), this.contractType.getName(), this.getStatusAsString(), this.getStartDate().toString(), this.getEndDate().toString());

        TableColumn<Contract, Number> columnID = new TableColumn<>(LanguageResource.getString("ID"));
        columnID.setCellValueFactory(cellData -> cellData.getValue().contractIdProperty());

        TableColumn<Contract, String> columnName = new TableColumn<>(LanguageResource.getString("name"));
        columnName.setCellValueFactory(cellData -> cellData.getValue().contractTypeNameProperty());

        TableColumn<Contract, String> columnStatus = new TableColumn<>(LanguageResource.getString("status"));
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().contractStatusProperty());

        TableColumn<Contract, String> columnStartDate = new TableColumn<>(LanguageResource.getString("start_date"));
        columnStartDate.setCellValueFactory(cellData -> cellData.getValue().contractStartDateProperty());

        TableColumn<Contract, String> columnEndDate = new TableColumn<>(LanguageResource.getString("end_date"));
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
    
    private ComboBox makeComboBox(Object o) {

        ObservableList list;
        if (o instanceof Boolean){
            list = FXCollections.observableList(Arrays.asList(true, false));
        } else {
            switch(o.getClass().getSimpleName()) {
                case "UserStatus" -> list = FXCollections.observableList(Arrays.asList(UserStatus.values()));
                case "EmployeeRole" -> list = FXCollections.observableList(Arrays.asList(EmployeeRole.values()));
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
        
}
