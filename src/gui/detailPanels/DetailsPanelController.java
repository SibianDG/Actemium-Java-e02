package gui.detailPanels;

import static javafx.scene.paint.Color.BLACK;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

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
import gui.viewModels.ViewModel;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import languages.LanguageResource;


public abstract class DetailsPanelController extends GridPane implements InvalidationListener {

    protected final ViewModel viewModel;
    protected boolean editing = false;

    @FXML
	protected Text txtDetailsTitle;

    @FXML
	protected GridPane gridDetails;

    @FXML
	protected Button btnDelete;

    @FXML
	protected Button btnModify;
    
    @FXML
    protected Button btnHistory;
    
    @FXML
    protected HBox hBoxModify;

    @FXML
	protected Text txtErrorMessage;
    
    protected GridPane gridContent;    

    public DetailsPanelController(ViewModel viewModel, GridPane gridContent) {
        this.viewModel = viewModel;
        this.gridContent = gridContent;
        // listener is removed when pressing home button or changing managescreen
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
        
        clearDetailPane();
    }

    public ViewModel getViewModel() {
		return viewModel;
	}

	private void clearDetailPane() {
        txtDetailsTitle.setText(LanguageResource.getString("nothingSelected"));
        gridDetails.getChildren().clear();
        btnModify.setVisible(false);
        btnHistory.setVisible(false);
        btnDelete.setVisible(false);
        txtErrorMessage.setVisible(false);
    }
    
    @FXML
    void btnHistoryOnAction(ActionEvent event) {
    	// implemented in TicketDetailsPanelController
    }

    @FXML
	void btnModifyOnAction(ActionEvent event) {
    	// implemented in subclasses
    }
    
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
        	if(deleteConfirmationAlert()) {
        		viewModel.delete();
        		showPopupMessage("popupDelete", LanguageResource.getString("succesfully_deleted_item"));
        		clearDetailPane();
        	}
        } catch (InformationRequiredException ire) {
            StringBuilder errorMessage = new StringBuilder();
            ire.getInformationRequired().forEach(e -> {
            errorMessage.append(e).append("\n");
            });
            txtErrorMessage.setText(errorMessage.toString());
            txtErrorMessage.setVisible(true);
            clearDetailPane();
        }
    }

    protected String getTextFromGridItem(int i){
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

    protected Label makeNewLabel(String text, boolean withColon){
        Label label = new Label(text+ (withColon ? ":" : ""));
        label.getStyleClass().clear();
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


    private ComboBox makeComboBox(Object o) {
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
    
    protected boolean deleteConfirmationAlert() {
    	boolean confirmed = false;
        String headerText = LanguageResource.getString("delele_confirmation_header");
    	String text = LanguageResource.getString("delele_confirmation_text");
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text);
        alert.setHeaderText(headerText);
        alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        Optional<ButtonType> result = alert.showAndWait();
        confirmed = result.get() == ButtonType.OK;
        return confirmed;
    }
    
    private Popup createPopup(final String message, String popupType) {
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
        label.getStylesheets().add("file:src/start/styles.css");
        label.getStyleClass().add(popupType);
        popup.getContent().add(label);
        return popup;
    }
    
    protected void showPopupMessage(final String popupType, String message) {
        final Popup popup = createPopup(message, popupType);
        final Stage stage = (Stage) gridDetails.getScene().getWindow();
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
				popup.setX(btnModify.localToScreen(btnModify.getBoundsInLocal()).getMaxX() - popup.getWidth()/2 - 500);
				popup.setY(btnModify.localToScreen(btnModify.getBoundsInLocal()).getMaxY() - popup.getHeight() - 5);
            }
        });
        popup.show(stage);
    }
	
    protected void showPopupMessageAddItem(final String popupType, String message) {
        final Popup popup = createPopup(message, popupType);
        final Stage stage = (Stage) gridDetails.getScene().getWindow();
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
				popup.setX(btnModify.localToScreen(btnModify.getBoundsInLocal()).getMaxX() - popup.getWidth()/2 - 360);
				popup.setY(160);
            }
        });
        popup.show(stage);
    }

    protected void initGridDetails() {
        gridDetails.getChildren().clear();
        gridDetails.getColumnConstraints().clear();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(30);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);

        gridDetails.getColumnConstraints().addAll(col0,col1);
    }
    
}
