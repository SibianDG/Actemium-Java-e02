package gui.detailPanels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import domain.enums.ContractStatus;
import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import gui.viewModels.ContractTypeViewModel;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import languages.LanguageResource;


public class ContractTypeDetailsPanelController extends DetailsPanelController {

    public ContractTypeDetailsPanelController(ViewModel viewModel, GridPane gridContent) {
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
					showPopupMessage("popupSuccess", LanguageResource.getString("contractTypeEdited_succes"));
                } else {
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
                showPopupMessageAddItem("popupSuccess", LanguageResource.getString("contracttype_created_success"));
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
       
        addGridDetails(((ContractTypeViewModel) viewModel).getDetails());
        txtDetailsTitle.setText(String.format("%s: %s", LanguageResource.getString("details_of_contracttype"), ((ContractTypeViewModel) viewModel).getNameSelectedContractType()));
        btnModify.setText(LanguageResource.getString("modify_contractType"));
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
    }

    private void addItemsToGridNewContractType(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> demoValues = Map.of(
                0, "ContractTypeeeeee"
                , 1, "12"
                , 2, "2"
                , 3, "500"
        );

        int demoValuesCounter = 0;

        for (int i = 0; i < fields.size(); i++) {
            String itemName = fields.get(i);

            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(itemName, true), 0, i);

            itemName = itemName.toLowerCase();

            Node node;
            if (itemName.contains(LanguageResource.getString("status").toLowerCase())) {
                node = makeComboBox(ContractTypeStatus.ACTIVE);
            } else if (itemName.contains(LanguageResource.getString("timestamp").toLowerCase())) {
                node = makeComboBox(Timestamp.ALWAYS);
            } else if (itemName.contains(LanguageResource.getString("email").toLowerCase())
                    || itemName.contains(LanguageResource.getString("phone").toLowerCase())
                    || itemName.contains(LanguageResource.getString("application").toLowerCase())){
                node = makeComboBox(false);
            } else {
                TextField textField;
                textField = new TextField(demoValues.get(demoValuesCounter++));
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
        } else if (o instanceof Boolean) {
            node = makeComboBox(o);
            node.setDisable(disable);
        } 
        return node;
    }

    private ComboBox makeComboBox(Object o) {
        ObservableList list = null;
        if (o instanceof Boolean){
            list = FXCollections.observableList(Arrays.asList(true, false));
        } else {
            switch(o.getClass().getSimpleName()) {
                case "ContractStatus" -> list = FXCollections.observableList(Arrays.asList(ContractStatus.values()));
                case "ContractTypeStatus" -> list = FXCollections.observableList(Arrays.asList(ContractTypeStatus.values()));
                case "Timestamp" -> list = FXCollections.observableList(Arrays.asList(Timestamp.values()));
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
