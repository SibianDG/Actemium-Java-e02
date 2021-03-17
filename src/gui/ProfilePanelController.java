package gui;

import java.io.IOException;
import java.util.*;

import exceptions.InformationRequiredException;
import gui.viewModels.ProfileViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
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
import languages.LanguageResource;

public class ProfilePanelController extends GridPane  {
	
	//private UserFacade userFacade;
	private final ProfileViewModel profileViewModel;
	
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
            profileViewModel.modifyProfile(
                    getTextFromGridPane(1),
                    getTextFromGridPane(2),
                    getTextFromGridPane(3),
                    getTextFromGridPane(4),
                    getTextFromGridPane(5),
                    getTextFromGridPane(6),
                    getTextFromGridPane(7)
            );

        } catch (InformationRequiredException ire){
            StringBuilder errorMessage = new StringBuilder();
            ire.getInformationRequired().forEach(e -> {
                System.out.println(e);
                errorMessage.append(e).append("\n");
            });
            txtErrorMessage.setText(errorMessage.toString());
            txtErrorMessage.setVisible(true);
        }

    }

    private String getTextFromGridPane(int i){
        Node node = gridProfile.getChildren().get(2*i+1);
        if (node instanceof HBox) {
            String password = ((PasswordField) ((HBox) node).getChildren().get(0)).getText();
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

	public ProfilePanelController(ProfileViewModel profileViewModel) {
		super();   
        this.profileViewModel = profileViewModel;
        
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
        initializeGridProfile();
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
        System.out.println("Columns: "+ gridProfile.getColumnCount());
        System.out.println("Row: "+ gridProfile.getRowCount());

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

            if(label.getText().toLowerCase().contains("password")) {
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_LEFT);
                PasswordField detail = new PasswordField();
                detail.setPromptText("Change password");

                CheckBox checkBox = new CheckBox("Show password");
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
    

}
