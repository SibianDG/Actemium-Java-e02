package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import domain.facades.Facade;
import domain.facades.TicketFacade;
import domain.facades.UserFacade;
import gui.controllers.GuiController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class DashboardFrameController extends GuiController {
    // Facades
	private final UserFacade userFacade;
	private final TicketFacade ticketFacade;

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
    private ImageView imgLogo;

    @FXML
    private ImageView imgLogout;

    @FXML
    private Text txtTitle;

    private Set<DashboardTile> dashboardTiles = new HashSet<>();

    public DashboardFrameController(Facade userFacade) throws FileNotFoundException {
        super();
        this.userFacade = (UserFacade) userFacade;
		this.ticketFacade = null;

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

        imgLogo.setCursor(Cursor.HAND);
        imgMyAccount.setCursor(Cursor.HAND);
        imgNotifications.setCursor(Cursor.HAND);
        imgLogout.setCursor(Cursor.HAND);

    }

    public void initializeDashboard() throws FileNotFoundException {
//        String[] itemNames = {"consult Knowledge base", "outstanding tickets", "resolved tickets", "statistics", "create Ticket", "manage Contract"};
//        String[] itemImages = {"icon_consult", "icon_outstanding", "icon_resolved", "icon_statistics", "icon_create", "icon_manage",};

        txtTitle.setText("Dashboard");
        resetGridpane();
        initializeGridPane(3, 2, 300, 300);

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

        switch (userFacade.giveUserType().toUpperCase()) {
            case "ADMINISTRATOR" -> {
                itemNames = new String[]{"manage employees", "manage customers"};
                itemIcons = new String[]{"icon_manage", "icon_manage"};
            }
            case "SUPPORT_MANAGER" -> {
                itemNames = new String[]{"manage knowledge base", "create ticket", "oustanding tickets", "resolved tickets", "statistics"};
                itemIcons = new String[]{"icon_manage", "icon_create", "icon_outstanding", "icon_resolved", "icon_statistics"};
            }
            case "TECHNICIAN" -> {
                itemNames = new String[]{"consult knowledge base", "oustanding tickets", "resolved tickets", "statistics"};
                itemIcons = new String[]{"icon_consult", "icon_outstanding", "icon_resolved", "icon_statistics"};
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
		
		TableViewPanelController tableViewPanelController = new TableViewPanelController(userFacade, this, name.split(" ")[1]);
		DetailsPanelController detailsPanelController = new DetailsPanelController(tableViewPanelController);

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

    private void initializeGridPane(int x, int y, double height, double width) {

        // initalize central gridpane

        for (int i = 0; i < x; i++) {
            gridContent.addColumn(i);
            gridContent.getColumnConstraints().add(new ColumnConstraints(width, height, -1, Priority.ALWAYS, HPos.CENTER, false));

        }
        for (int i = 0; i < y; i++) {
            gridContent.addRow(i);
            gridContent.getRowConstraints().add(new RowConstraints(width, height, -1, Priority.ALWAYS, VPos.CENTER, false));
        }
    }

    public void setModifyPane(){
        txtTitle.setText("Add Employee");
        resetGridpane();
        initializeGridPane(1,1, 500, 500);
        gridContent.add(new ModifyPanelController(userFacade /*, name.split(" ")[1])*/, this), 0, 0);
    }

    private void initializeText() {
        txtName.setText(String.format("%s %s" , userFacade.giveUserFirstName(), userFacade.giveUserLastName()));
        txtCompany.setText("COMPANY???");
    }

    public void reactionFromTile(){
        txtTitle.setText("Tile!");
    }


//    private void loadScene(String title, Object controller) { // method for switching to the next screen
//        Scene scene = new Scene((Parent) controller);
//        Stage stage = (Stage) this.getScene().getWindow();
//        stage.setTitle(title);
//        stage.setResizable(false);
//        stage.setScene(scene);
//        stage.show();
//    }


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

    @FXML
    void navigateToHome(MouseEvent event) throws FileNotFoundException {
        initializeDashboard();
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
