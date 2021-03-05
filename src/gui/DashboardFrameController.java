package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import domain.facades.TicketFacade;
import domain.facades.UserFacade;
import domain.manager.Actemium;
import gui.controllers.GuiController;
import gui.viewModels.TicketViewModel;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class DashboardFrameController <T> extends GuiController {

	private final Actemium actemium;
	
	// Facades
    private final UserFacade userFacade;
	//private final TicketFacade ticketFacade;

    private UserViewModel userViewModel;
	//private TicketViewModel ticketViewModel;

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
    private Text txtEmployeeRole;

    @FXML
    private ImageView imgLogo;

    @FXML
    private ImageView imgLogout;

    @FXML
    private Text txtTitle;

    @FXML
    private GridPane gridMenu;

    private Set<DashboardTile> dashboardTiles = new HashSet<>();

    private TableViewPanelCompanion<T> tableViewPanelCompanion;
    private DetailsPanelController detailsPanelController;

//    public DashboardFrameController(Actemium actemium, UserFacade userFacade) throws FileNotFoundException {
    public DashboardFrameController(UserFacade userFacade, Actemium actemium) throws FileNotFoundException {
        super();
        
        this.userFacade = userFacade;

        this.actemium = actemium;

        this.userViewModel = new UserViewModel(userFacade, actemium);

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

        txtTitle.setText("Dashboard");
        resetGridpane(gridContent);
        initializeGridPane(3, 2, 300, 300);

        String[] itemNames = new String[] {};
        String[] itemIcons = new String[] {};

        switch (actemium.giveUserRole().toUpperCase()) {
            case "ADMINISTRATOR" -> {
                itemNames = new String[]{"manage employees", "manage customers"};
                itemIcons = new String[]{"icon_manage", "icon_manage"};
            }
            case "SUPPORT_MANAGER" -> {
                itemNames = new String[]{"manage knowledge base", "create ticket", "oustanding tickets", "resolved tickets", "statistics"};
                itemIcons = new String[]{"icon_manage", "icon_create", "icon_outstanding", "icon_resolved", "icon_statistics"};
            }
            case "TECHNICIAN" -> {
                itemNames = new String[]{"consult knowledge base", "outstanding tickets", "resolved tickets", "statistics"};
                itemIcons = new String[]{"icon_consult", "icon_outstanding", "icon_resolved", "icon_statistics"};
            }
        }
        	
        for(int i = 0; i < itemNames.length; i++) {
        	addDashboardItem(itemNames[i], createImageView(itemIcons[i], i%2), i%3, i/3, i);
        }

        createGridMenu(itemNames);
    }

    private void createGridMenu(String[] itemNames){
        resetGridpane(gridMenu);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        int gap = 50;
        int width = (int) (primScreenBounds.getWidth()/(itemNames.length+1) - (gap*itemNames.length));
        for (int i = 0; i < itemNames.length; i++) {
            String text = itemNames[i];
            gridMenu.addColumn(i);
            gridMenu.getColumnConstraints().add(new ColumnConstraints(width, 100, -1, Priority.ALWAYS, HPos.CENTER, false));
            Button button = createMenuItemButton(text, width);
            setValignment(button, VPos.BOTTOM);
            setHalignment(button, HPos.CENTER);
            button.setOnMouseClicked(e -> buttonMenusClicked(text));
            gridMenu.add(button, i, 0);
            setHgrow(button, Priority.ALWAYS);
            setVgrow(button, Priority.ALWAYS);
        }
        gridMenu.setVgap(gap);
    }
    
    private Button createMenuItemButton(String s, int width){
        Button button = new Button(s);
        button.getStyleClass().add("menuButton");
        button.setTextFill(Color.WHITE);
        button.minWidth(width);
        button.prefWidth(width);
        button.setPadding(new Insets(15, (double)width/2, 15, (double)width/2));
        button.setAlignment(Pos.BOTTOM_CENTER);
        return button;
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
        dashboardTile.setOnMouseClicked(e -> buttonMenusClicked(name) );
        dashboardTiles.add(dashboardTile);
        gridContent.add(dashboardTile, x, y);
        setFillHeight(dashboardTile, true);
        setFillWidth(dashboardTile, true);
        gridContent.setHgap(35);
        gridContent.setVgap(35);

    }

    private void buttonMenusClicked(String name){
        if(name.toLowerCase().contains("manage") && name.toLowerCase().contains("employee")) {
            //Todo weird
            userViewModel.setEmployees(actemium.giveActemiumEmployees());
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, userViewModel, GUIEnum.EMPLOYEE);
            switchToManageScreen(name, tableViewPanelCompanion, userViewModel);
        } else if(name.toLowerCase().contains("manage") && name.toLowerCase().contains("customer")) {
            userViewModel.setCustomers(actemium.giveActemiumCustomers());
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, userViewModel, GUIEnum.CUSTOMER);
            switchToManageScreen(name, tableViewPanelCompanion, userViewModel);
        } else if (name.toLowerCase().contains("ticket") && name.toLowerCase().contains("outstanding")) {
            TicketFacade ticketFacade = new TicketFacade(actemium);
            TicketViewModel viewModel = new TicketViewModel(ticketFacade);
            viewModel.setActemiumTickets(actemium.giveActemiumTickets());
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, viewModel, GUIEnum.TICKET);
            switchToManageScreen(name, tableViewPanelCompanion, viewModel);
        } else {
            makePopUp(name);
        }
    }

	private void switchToManageScreen(String name, TableViewPanelCompanion<T> tableViewPanelCompanion, ViewModel viewModel) {
		txtTitle.setText(name);
		resetGridpane(gridContent);
		
		//tableViewPanelCompanion = new TableViewPanelCompanion(this, userViewModel, currentState);
		detailsPanelController = new DetailsPanelController(viewModel);
		gridContent.add(tableViewPanelCompanion, 0, 0);
		gridContent.add(detailsPanelController, 1, 0);
	}

    private void resetGridpane(GridPane gridPane){
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
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

    private void initializeText() {
        txtName.setText(String.format("%s %s" , actemium.giveUserFirstName(), actemium.giveUserLastName()));
        txtEmployeeRole.setText(actemium.giveUserRole());
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

    @FXML
    void navigateToHome(MouseEvent event) throws FileNotFoundException {
        initializeDashboard();
    }

    private void makePopUp(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);
        alert.showAndWait();
    }
}
