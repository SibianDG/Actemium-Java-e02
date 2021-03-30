package gui.detailPanels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import domain.Employee;
import domain.Ticket;
import domain.UserModel;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import gui.viewModels.KnowledgeBaseViewModel;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import languages.LanguageResource;


public class KnowledgeBaseDetailsPanelController extends DetailsPanelController {

    public KnowledgeBaseDetailsPanelController(ViewModel viewModel, GridPane gridContent) {
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
                    ((KnowledgeBaseViewModel) viewModel).modifyKbItem(
                            getTextFromGridItem(0)
                            , KbItemType.valueOf(getTextFromGridItem(1))
                            , getTextFromGridItem(2)                                
                            , getTextFromGridItem(3)                                
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
                        , getTextFromGridItem(3)
                );
                showPopupMessageAddItem("popupSuccess", LanguageResource.getString("KBitem_created_success"));
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
       
        addGridDetails(((KnowledgeBaseViewModel) viewModel).getDetails());
        txtDetailsTitle.setText(String.format("%s: %s", LanguageResource.getString("title"), ((KnowledgeBaseViewModel) viewModel).getTitleSelectedKbItem()));
        btnModify.setText(LanguageResource.getString("modify_KB_item"));
        btnDelete.setVisible(true);
        
        btnModify.setVisible(((Employee) ((KnowledgeBaseViewModel) viewModel).getSignedInUser()).getRole() != EmployeeRole.TECHNICIAN);
        txtErrorMessage.setVisible(false);
        editing = true;
    }

    private void setupPaneNewObject(){
        editing = false;
        ArrayList<String> fields = null;
        
        btnHistory.setVisible(false);
        btnDelete.setVisible(false);

        viewModel.setFieldModified(true);
        
        if (((KnowledgeBaseViewModel) viewModel).getCurrentState().equals(GUIEnum.KNOWLEDGEBASE)) {
            fields = ((KnowledgeBaseViewModel) viewModel).getDetailsNewKbItem();
            txtDetailsTitle.setText(LanguageResource.getString("addKbItem"));
            btnModify.setText(LanguageResource.getString("addKbItem"));
        }
        btnModify.setVisible(true);
        assert fields != null;
        addItemsToGridNewKbItem(fields);
        
    }
    
    private void addItemsToGridNewKbItem(ArrayList<String> fields){
        initGridDetails();

        Map<Integer, String> demoValues = Map.of(
                0, "How to Solve Network Problems"
                , 1, KbItemType.OTHER.toString()
                , 2, "Sample text"
        );

        for (int i = 0; i < fields.size(); i++) {
            gridDetails.addRow(i);

            gridDetails.add(makeNewLabel(fields.get(i), true), 0, i);

            Node node;
            if (fields.get(i).toLowerCase().contains(LanguageResource.getString("text").toLowerCase())) {
            	TextArea textArea;
            	textArea = new TextArea(demoValues.get(i));
            	textArea.setFont(Font.font("Arial", 14));
            	textArea.setPromptText(fields.get(i));
                node = textArea;
            } else if (fields.get(i).toLowerCase().contains(LanguageResource.getString("type").toLowerCase())) {
                node = makeComboBox(KbItemType.OTHER);
            } else {
                TextField textField;
                textField = new TextField(demoValues.get(i));
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
            if (key.toLowerCase().contains("ticketsofsametype")){
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
           
            if(key.equalsIgnoreCase(LanguageResource.getString("text"))) {
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
	            detail.setEditable(editable);
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
	                detail.setId("textFieldWithPadding");
	            }
	            detail.setDisable(disable);
	            detail.setPromptText(key);
	
	            node = detail;
	        }
        } else if (o instanceof Enum) {
            node = makeComboBox(o);
            node.setDisable(disable);
        } else if (o instanceof ObservableList) {            
            	if(((ObservableList) o).size() > 0)
            		node = makeTableViewTicketsInKb(o);
                else
                    node = makeNewLabel(LanguageResource.getString("no_items_available"), false);           
        } 
        return node;
    }
    
    private <T> Node makeTableViewTicketsInKb(Object o) {
        ObservableList<Ticket> list = (ObservableList<Ticket>) o;
        TableView<Ticket> tableView = new TableView<>(list);
        TableColumn<Ticket, Number> columnID = new TableColumn<>(LanguageResource.getString("ID"));
        columnID.setCellValueFactory(cellData -> cellData.getValue().ticketIdProperty());

        TableColumn<Ticket, String> columnName = new TableColumn<>(LanguageResource.getString("priority"));
        columnName.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());

        TableColumn<Ticket, String> columnStatus = new TableColumn<>(LanguageResource.getString("title"));
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Ticket, String> columnEndDate = new TableColumn<>(LanguageResource.getString("completion_date"));
        columnEndDate.setCellValueFactory(cellData -> cellData.getValue().completionDateProperty());

        tableView.getColumns().add(columnID);
        tableView.getColumns().add(columnName);
        tableView.getColumns().add(columnStatus);
        tableView.getColumns().add(columnEndDate);

        tableView.setSelectionModel(null);
        tableView.getStyleClass().add("ignoreHover");

        tableView.setMaxHeight(list.size()*25+75);
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableView;
    }
    
    private ComboBox makeComboBox(Object o) {
        ObservableList list;
        
        list = FXCollections.observableList(Arrays.asList(KbItemType.values()));
        
        ComboBox c = new ComboBox(list);
        c.getSelectionModel().select(o);
        c.valueProperty().addListener(e -> {
            viewModel.setFieldModified(true);
        });
        return c;
    }
    
}
