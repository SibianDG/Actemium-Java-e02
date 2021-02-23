package gui;

import domain.EmployeeRole;
import domain.controllers.DomainController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class DashboardController extends GridPane {
    private final DomainController domainController;

    @FXML
    private GridPane gridDashboard;

    @FXML
    private GridPane gridContent;

    @FXML
    private ImageView imgNotifications;

    @FXML
    private ImageView imgMyAccount;

    @FXML
    private Text txtName;

    @FXML
    private Text txtCompany;

    @FXML
    private ImageView imgLogout;

    @FXML
    private Text txtTitle;

    private Set<DashboardTile> dashboardTiles = new HashSet<>();

    public DashboardController(DomainController domainController) throws FileNotFoundException {
        super();
        this.domainController = domainController;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }

        initializeDashboard();
        initializeText();

    }

    public void initializeDashboard() throws FileNotFoundException {
//        String[] itemNames = {"consult Knowledge base", "outstanding tickets", "resolved tickets", "statistics", "create Ticket", "manage Contract"};
//        String[] itemImages = {"icon_consult", "icon_outstanding", "icon_resolved", "icon_statistics", "icon_create", "icon_manage",};

        resetGridpane();
        initializeGridPane(3, 2);

//        Map<String, IntStream> employeeRoleArrayListSet = Map.of(
//                EmployeeRole.ADMINISTRATOR.toString(), IntStream.range(0,6),
//                EmployeeRole.SUPPORT_MANAGER.toString(), IntStream.range(0,5),
//                EmployeeRole.TECHNICIAN.toString(), IntStream.range(0,4)
//        );
//
//
//        employeeRoleArrayListSet.get(domainController.giveUserType()).forEach(r -> {
//            try {
//                addDashboardItem(itemNames[r], createImageView(itemImages[r], r%2), r%3, r/3, r);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//        });
        String[] itemNames = new String[] {};
        String[] itemIcons = new String[] {};
        
        switch(domainController.giveUserType().toUpperCase()) {
        	case "ADMINISTRATOR": {
        		itemNames = new String[] {"manage employees", "manage customers"};
        		itemIcons = new String[] {"icon_manage", "icon_manage"};
        		break;
        		}
        	
        	case "SUPPORT MANAGER": {
        		itemNames = new String[] {"manage knowledge base", "create ticket", "oustanding tickets", "resolved tickets", "statistics"};
        		itemIcons = new String[] {"icon_manage", "icon_create", "icon_outstanding", "icon_resolved", "icon_statistics"};
        		break;
        		}
        	
        	case "TECHNICIAN": {
        		itemNames = new String[] {"consult knowledge base", "oustanding tickets", "resolved tickets", "statistics"};
        		itemIcons = new String[] {"icon_consult", "icon_outstanding", "icon_resolved", "icon_statistics"};
        		break;
        		}
        	}
        	
        for(int i = 0; i < itemNames.length; i++) {
        	addDashboardItem(itemNames[i], createImageView(itemIcons[i], i%2), i%3, i/3, i);
        }
        
    }

    

    public ImageView createImageView(String image, int i) throws FileNotFoundException {

        FileInputStream input = new FileInputStream("src/pictures/dashboard/dashboarditems/" + image + "_" + i + ".png");

        ImageView imageView = new ImageView(new Image(input));
        imageView.setFitHeight(130);
        imageView.setFitWidth(130);
        return imageView;
    }

    private void addDashboardItem(String name, ImageView imageView, int x, int y, int i) {
        DashboardTile dashboardTile = new DashboardTile(imageView, name, i%2);
        dashboardTile.setOnMouseClicked(e -> {
        	
        	if(name.toLowerCase().contains("manage")) {
        		switchToManageScreen(name);
        	} else {
        		makePopUp(name);
        	}
        	    	
         
        });
        dashboardTiles.add(dashboardTile);
        gridContent.add(dashboardTile, x, y);
        setFillHeight(dashboardTile, true);
        setFillWidth(dashboardTile, true);
        gridContent.setHgap(35);
        gridContent.setVgap(35);

    }

	private void switchToManageScreen(String name) {
		txtTitle.setText(name);
		resetGridpane();       
		
		TableViewPanelController tableViewPanelController = new TableViewPanelController(domainController);
		DetailsPanelController detailsPanelController = new DetailsPanelController();

		gridContent.add(tableViewPanelController, 0, 0);
		gridContent.add(detailsPanelController, 1, 0);
//		System.out.println(gridContent.getColumnCount());
//		System.out.println(gridContent.getRowCount());
	}

    private void resetGridpane(){
    	gridContent.getChildren().clear();
    	gridContent.getColumnConstraints().clear();
    	gridContent.getRowConstraints().clear();
    }

    private void initializeGridPane(int x, int y) {

        // initalize central gridpane

        double height = 300;
        double width = 300;

        for (int i = 0; i < x; i++) {
            gridContent.addColumn(i);
            gridContent.getColumnConstraints().add(new ColumnConstraints(width, height, -1, Priority.ALWAYS, HPos.CENTER, false));

        }
        for (int i = 0; i < y; i++) {
            System.out.println(i);
            gridContent.addRow(i);
            gridContent.getRowConstraints().add(new RowConstraints(width, height, -1, Priority.ALWAYS, VPos.CENTER, false));
        }
    }

    private void initializeText() {
        txtName.setText(String.format("%s %s" , domainController.giveUserFirstName(), domainController.giveUserLastName()));
        txtCompany.setText("COMPANY???");
    }

    public void reactionFromTile(){
        txtTitle.setText("Tile!");
    }


    private void loadScene(String title, Object controller) { // method for switching to the next screen
        Scene scene = new Scene((Parent) controller);
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void btnLogOutAction(MouseEvent event) {
        makePopUp("Logout and exit");
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void btnNotificationAction(MouseEvent event) {
        makePopUp("Notifications");

    }

    @FXML
    void btnProfileAction(MouseEvent event) {
        makePopUp("Profile");

    }

    private void makePopUp(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.showAndWait();
    }

    /*protected void fireInvalidationEvent() {
        for (InvalidationListener listener : listeners) {
            listener.invalidated(this);
        }
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        listeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        listeners.remove(invalidationListener);
    }

     */
}
