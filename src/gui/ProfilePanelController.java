package gui;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import domain.Employee;
import domain.facades.UserFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
	
	private Text makeNewText(String text) {
		Text txt = new Text(text);
		txt.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
		return txt;
		
	}
	
    private Map<String, String> getDetailsSignedInUser(){
        Map<String, String> detailsMap = new LinkedHashMap<>();
//        detailsMap.put("Employee ID", Collections.singletonMap(false, String.valueOf()));
//        detailsMap.put("Username", Collections.singletonMap(true, ));
//        detailsMap.put("Password", Collections.singletonMap(true, ));
        detailsMap.put("Firstname:", userFacade.giveUserFirstName());
        detailsMap.put("Lastname:", userFacade.giveUserLastName());
//        detailsMap.put("Address", Collections.singletonMap(true, ));
//        detailsMap.put("Phone number", Collections.singletonMap(true, ));
//        detailsMap.put("Email", Collections.singletonMap(true, ));
//        detailsMap.put("Company Seniority", Collections.singletonMap(false, String.valueOf()));
        detailsMap.put("Role:", userFacade.giveUserRole());
//        detailsMap.put("Status", Collections.singletonMap(true, ));
        return detailsMap;
	}
    
    private void addGridDetails(Map<String, String> details){
        int i = 0;
        // Using LinkedHashSet so the order of the map values doesn't change
        Set<String> keys = new LinkedHashSet<String>(details.keySet());
        for (String key : keys) {
            Label label = makeNewLabel(key);

            Node detail = makeNewText(details.get(key));

            gridProfile.add(label, 0, i);
            gridProfile.add(detail, 1, i);
            i++;
         }            
    }
    

}
