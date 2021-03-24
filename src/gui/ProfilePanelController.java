package gui;

import java.io.IOException;
import java.util.*;

import exceptions.InformationRequiredException;
import gui.viewModels.ProfileViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import languages.LanguageResource;

public class ProfilePanelController extends GridPane  {
	
	private final ProfileViewModel profileViewModel;
	private final DashboardFrameController dashboardFrameController;
	
	@FXML
    private Label lblProfile;

    @FXML
    private GridPane gridProfile;

    @FXML
    private Button btnModifyAccount;

    @FXML
    private Text txtErrorMessage;

    @FXML
    void modifyButtonAccountOnMousePressed(MouseEvent event) {
        try {
            if (profileViewModel.isFieldModified()){
                profileViewModel.modifyProfile(
                        getTextFromGridPane(1),
                        getTextFromGridPane(2),
                        getTextFromGridPane(3),
                        getTextFromGridPane(4),
                        getTextFromGridPane(5),
                        getTextFromGridPane(6),
                        getTextFromGridPane(7)
                );

                showPopupMessage("popupSuccess", LanguageResource.getString("profileEdit_success"));
                txtErrorMessage.setVisible(false);
                profileViewModel.setFieldModified(false);
                emptyPasswordField();


            } else {
                showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));
            }

        } catch (InformationRequiredException ire){
            StringBuilder errorMessage = new StringBuilder();
            ire.getInformationRequired().forEach(e -> {
                errorMessage.append(e).append("\n");
            });
            txtErrorMessage.setText(errorMessage.toString());
            txtErrorMessage.setVisible(true);
            showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));

        } catch (Exception e){
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
            showPopupMessage("popupWarning", LanguageResource.getString("unchangedMessage"));

        }

    }

    private void emptyPasswordField() {
        gridProfile.getChildren().forEach(e -> {
            if (e instanceof HBox) {
                ((HBox) e).getChildren().forEach(h -> {
                    if (h instanceof PasswordField){
                        PasswordField psswdf = (PasswordField) h;
                        psswdf.setText("");
                        psswdf.setPromptText(LanguageResource.getString("change_password"));
                    }
                });
            }
        });
    }

    public ProfilePanelController(ProfileViewModel profileViewModel, DashboardFrameController dashboardFrameController) {
		super();   
        this.profileViewModel = profileViewModel;
        this.dashboardFrameController = dashboardFrameController;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        lblProfile.setText(LanguageResource.getString("your_profile"));
        txtErrorMessage.setVisible(false);
        btnModifyAccount.setText(LanguageResource.getString("modify"));
        initializeGridProfile();
	}


    private String getTextFromGridPane(int i){
        Node node = gridProfile.getChildren().get(2*i+1);
        if (node instanceof HBox) {
            PasswordField passwordField = ((PasswordField) ((HBox) node).getChildren().get(0));
            String password = passwordField.getText();
            if (password == null || password.isBlank()){
                password = "********";
            }
            return password;
        }
        if (node instanceof TextField) {
            return ((TextField) node).getText();
        }
        if (node instanceof Text) {
            return ((Text) node).getText();
        }
        return null;
    }
	
	private void initializeGridProfile() {
		gridProfile.getChildren().clear();
		gridProfile.getColumnConstraints().clear();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(30);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);

        gridProfile.getColumnConstraints().addAll(col0,col1);
        gridProfile.setGridLinesVisible(true);
        
        addGridDetails(profileViewModel.getDetailsSignedInUser());
        gridProfile.setGridLinesVisible(true);
    }
	
	private Label makeNewLabel(String text){
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setTextAlignment(TextAlignment.RIGHT);
        label.setTextFill(Color.rgb(29, 61, 120));
        return label;
    }
	
	private Node makeNewText(Map<Boolean, String> map, String promptText) {
        boolean disable = (boolean) map.keySet().toArray()[0];
        String text = map.get(disable);

        Node node = null;

        if (disable) {
            Text txtItem = new Text(text);
            txtItem.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            node = txtItem;
        } else {
            TextField textField = new TextField(text);
            textField.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            textField.setPromptText(promptText);
            textField.setPadding(new Insets(0));
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue))
                    profileViewModel.setFieldModified(true);
            });
            node = textField;
        }
        return node;

	}
    
    private void addGridDetails(Map<String, Map<Boolean, String>> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key);

            Map<Boolean, String> map = details.get(key);

            if(label.getText().toLowerCase().contains(LanguageResource.getString("password").toLowerCase())) {
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_LEFT);
                PasswordField detail = new PasswordField();
                detail.setPromptText(LanguageResource.getString("change_password"));

                CheckBox checkBox = new CheckBox(LanguageResource.getString("show_password"));
                checkBox.setOnMouseClicked(e -> {
                    if(checkBox.isSelected()){
                        detail.setPromptText(detail.getText());
                        detail.setText("");
                    } else {
                        detail.setText(detail.getPromptText());
                        detail.setPromptText("");
                    }
                });
                detail.setOnKeyTyped(e -> checkBox.setSelected(false));

                detail.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!oldValue.equals(newValue))
                        profileViewModel.setFieldModified(true);
                });
                hbox.getChildren().addAll(detail, checkBox);
                HBox.setHgrow(detail, Priority.ALWAYS);

                gridProfile.add(label, 0, i);
                gridProfile.add(hbox, 1, i);
            } else {
                gridProfile.add(label, 0, i);
                gridProfile.add(makeNewText(details.get(key), key), 1, i);
            }
            
            i++;
         }            
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
        label.getStylesheets().add("file:src/start/styles.css");
        label.getStyleClass().add(popupType);
        popup.getContent().add(label);
        return popup;
    }


    public void showPopupMessage(final String popupType, String message) {
        final Popup popup = createPopup(message, popupType);
        final Stage stage = (Stage) gridProfile.getScene().getWindow();
        popup.setOnShown(e -> {
            popup.setX(popup.getWidth() < 300 ? 840 : 810);
            popup.setY(285 + lblProfile.getLayoutY() + 15 + popup.getHeight()/2);
        });
        popup.show(stage);
    }


    public boolean alertChanges() {
        boolean showNewObject = true;
        if(profileViewModel.isFieldModified()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle(LanguageResource.getString("modifiedWithoutSaving"));
            alert.setHeaderText(LanguageResource.getString("unsavedChanges"));
            alert.setContentText(LanguageResource.getString("chooseOption"));

            alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
            alert.getDialogPane().getStyleClass().add("alert");
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));

            ButtonType discardChanges = new ButtonType(LanguageResource.getString("discardChanges"));
            ButtonType buttonTypeCancel = new ButtonType(LanguageResource.getString("keepEditing"), ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(discardChanges, buttonTypeCancel);

            Node discardButton = alert.getDialogPane().lookupButton(discardChanges);
            discardButton.setId("discardBtn");
            Node CancelButton = alert.getDialogPane().lookupButton(buttonTypeCancel);
            CancelButton.setId("cancelBtn");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == discardChanges){
                // using this variable with if to avoid duplicate code
                showNewObject = true;
                dashboardFrameController.setEnabled(true);
                profileViewModel.setFieldModified(false);
            } else {
                dashboardFrameController.setEnabled(false);
                showNewObject = false;
            }
        }
        return showNewObject;
    }
}
