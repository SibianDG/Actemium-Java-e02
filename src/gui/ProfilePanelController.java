package gui;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import domain.facades.UserFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	
	private TextField makeNewText(String text, String promptText) {
        TextField textField = new TextField(text);
        textField.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        textField.setPromptText(promptText);
		return textField;
		
	}
	
    private Map<String, String> getDetailsSignedInUser(){

        Map<String, String> detailsMap = new LinkedHashMap<>();
        detailsMap.put("Employee nr:", userFacade.giveUserEmployeeId());
        detailsMap.put("Username:", userFacade.giveUserUsername());
        detailsMap.put("Password:", "*".repeat(userFacade.giveUserPassword().length()));
        detailsMap.put("Firstname:", userFacade.giveUserFirstName());
        detailsMap.put("Lastname:", userFacade.giveUserLastName());
        detailsMap.put("Address:", userFacade.giveUserAddress());
        detailsMap.put("Phone number:", userFacade.giveUserPhoneNumber());
        detailsMap.put("Email:", userFacade.giveUserEmailAddress());
        detailsMap.put("Company Seniority:", userFacade.giveUserSeniority());
        detailsMap.put("Role:", userFacade.giveUserRole());
        detailsMap.put("Status:", userFacade.giveUserStatus());
        return detailsMap;
	}
    
    private void addGridDetails(Map<String, String> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key);

            TextField detail = makeNewText(details.get(key), key);

            if(label.getText().toLowerCase().contains("password")) {
            	gridProfile.add(label, 0, i);
                gridProfile.add(detail, 1, i);
                Text txtShow= new Text("Show");
                Color blue = Color.rgb(29, 61, 120);
                txtShow.setFill(blue);
                txtShow.setCursor(Cursor.HAND);
                txtShow.setUnderline(true);
                txtShow.setOnMouseClicked(e -> {
                	detail.setText(userFacade.giveUserPassword());
                });
                gridProfile.add(txtShow, 2, i);
            } else {
            gridProfile.add(label, 0, i);
            gridProfile.add(detail, 1, i);
            }
            
            i++;
         }            
    }
    

}
