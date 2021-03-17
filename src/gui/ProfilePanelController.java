package gui;

import java.io.IOException;
import java.util.*;

import domain.facades.UserFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import languages.LanguageResource;

public class ProfilePanelController extends GridPane  {
	
	private UserFacade userFacade;
	
	@FXML
    private Label lblProfile;

    @FXML
    private GridPane gridProfile;

    @FXML
    private Button btnModifyAccount;

    @FXML
    void modifyButtonAccountOnMousePressed(MouseEvent event) {

    }

	public ProfilePanelController(UserFacade userFacade) {
		super();   
        this.userFacade = userFacade;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        lblProfile.setText(LanguageResource.getString("your_profile"));
        initializeGridProfile();
	}
	
	private void initializeGridProfile() {
		gridProfile.getChildren().clear();
		gridProfile.getColumnConstraints().clear();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(40);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60);

        gridProfile.getColumnConstraints().addAll(col0,col1);
        
        addGridDetails(getDetailsSignedInUser());
		
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
	
    private Map<String, Map<Boolean, String>> getDetailsSignedInUser(){

        Map<String, Map<Boolean, String>> detailsMap = new LinkedHashMap<>();
        detailsMap.put("Employee nr:", Collections.singletonMap(true, userFacade.giveUserEmployeeId()));
        detailsMap.put("Username:", Collections.singletonMap(true, userFacade.giveUserUsername()));
        detailsMap.put("Password:", Collections.singletonMap(false, "*".repeat(userFacade.giveUserPassword().length())));
        detailsMap.put("Firstname:", Collections.singletonMap(false, userFacade.giveUserFirstName()));
        detailsMap.put("Lastname:", Collections.singletonMap(false, userFacade.giveUserLastName()));
        detailsMap.put("Address:", Collections.singletonMap(false, userFacade.giveUserAddress()));
        detailsMap.put("Phone number:", Collections.singletonMap(false, userFacade.giveUserPhoneNumber()));
        detailsMap.put("Email:", Collections.singletonMap(false, userFacade.giveUserEmailAddress()));
        detailsMap.put("Company Seniority:", Collections.singletonMap(true, userFacade.giveUserSeniority()));
        detailsMap.put("Role:", Collections.singletonMap(true, userFacade.giveUserRole().toLowerCase()));
        detailsMap.put("Status:", Collections.singletonMap(true, userFacade.giveUserStatus().toLowerCase()));
        return detailsMap;
	}
    
    private void addGridDetails(Map<String, Map<Boolean, String>> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key);

            Map<Boolean, String> map = details.get(key);

            if(label.getText().toLowerCase().contains("password")) {
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


                //Text txtShow= new Text("Show");
                //Color blue = Color.rgb(29, 61, 120);
                //txtShow.setFill(blue);
                //txtShow.setCursor(Cursor.HAND);
                //txtShow.setUnderline(true);
                //txtShow.setOnMouseClicked(e -> {
                //    if (((TextField)detail).getText().contains("****")){
                //        ((TextField)detail).setText(userFacade.giveUserPassword());
                //    } else {
                //        ((TextField)detail).setText("*".repeat(8));
                //    }
                //});
                gridProfile.add(label, 0, i);
                gridProfile.add(detail, 1, i);
                gridProfile.add(checkBox, 2, i);
            } else {
                gridProfile.add(label, 0, i);
                gridProfile.add(makeNewText(details.get(key), key), 1, i);
            }
            
            i++;
         }            
    }
    

}
