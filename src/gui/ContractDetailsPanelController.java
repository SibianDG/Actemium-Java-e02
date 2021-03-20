package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import domain.enums.ContractStatus;
import exceptions.InformationRequiredException;
import gui.viewModels.ContractViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import languages.LanguageResource;


public class ContractDetailsPanelController extends DetailsPanelController {
        
    public ContractDetailsPanelController(ViewModel viewModel, GridPane gridContent) {
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
                    ((ContractViewModel) viewModel).modifyContract(
                            // Only the status can be modified in case a customer didnt pay his bills                        		
                            ContractStatus.valueOf(getTextFromGridItem(3))
                    );
					showPopupMessage("popupSuccess", LanguageResource.getString("contractEdited_succes"));
                } else {
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
                showPopupMessageAddItem("popupSuccess", LanguageResource.getString("contract_created_success"));
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
                
        addGridDetails(((ContractViewModel) viewModel).getDetails());
        txtDetailsTitle.setText(String.format("%s: %s", LanguageResource.getString("details_of_contract"), ((ContractViewModel) viewModel).getIdSelectedContract()));
        btnModify.setText(LanguageResource.getString("modify_contract"));
        btnDelete.setVisible(false);
            
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
            if (itemName.contains(LanguageResource.getString("start").toLowerCase())) {
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
    
    //TODO
    private void addGridDetails(Map<String, Map<Boolean, Object>> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key, true);

            Node detail = createElementDetailGridpane(details.get(key), key);
            
            gridDetails.add(label, 0, i);
            gridDetails.add(detail, 1, i);
            
            i++;
        }
    }

    //TODO
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
        } else if (o instanceof LocalDate) {
            node = makeDatePicker(o);
            node.setDisable(disable);
        }
        return node;
    }

    private ComboBox makeComboBox(Object o){

        ObservableList list;
       
        list = FXCollections.observableList(Arrays.asList(ContractStatus.values()));
        
        ComboBox c = new ComboBox(list);
        c.getSelectionModel().select(o);
        c.valueProperty().addListener(e -> {
            viewModel.setFieldModified(true);
        });

        return c;
    }

    //TODO
    // can be only in super class or only here
    private DatePicker makeDatePicker(Object o) {
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
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
        });        
        return datePicker;
    }
    
}
