package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import domain.ActemiumCustomer;
import domain.Ticket;
import domain.enums.TicketStatus;
import domain.facades.ContractTypeFacade;
import domain.facades.TicketFacade;
import domain.facades.UserFacade;
import gui.controllers.GuiController;
import gui.viewModels.ContractTypeViewModel;
import gui.viewModels.TicketViewModel;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DashboardFrameController <T> extends GuiController {
	
	// Facades
    private final UserFacade userFacade;
	private final TicketFacade ticketFacade;
    private final ContractTypeFacade contractTypeFacade;

    // ViewModels
    private UserViewModel userViewModel;
	private TicketViewModel ticketViewModel;
	private ContractTypeViewModel contractTypeViewModel;

    @FXML
    private GridPane gridDashboard;

    @FXML
    private ImageView imgNotifications;

    @FXML
    private ImageView imgMyAccount;

    @FXML
    private Text txtName;

    @FXML
    private Text txtEmployeeRole;

    @FXML
    private ImageView imgLogout;

    @FXML
    private ImageView imgLogo;

    @FXML
    private HBox hboxMenu;

    @FXML
    private GridPane gridContent;

    private Set<DashboardTile> dashboardTiles = new HashSet<>();

    private TableViewPanelCompanion<T> tableViewPanelCompanion;
    private DetailsPanelController detailsPanelController;

//    public DashboardFrameController(Actemium actemium, UserFacade userFacade) throws FileNotFoundException {
    public DashboardFrameController(UserFacade userFacade, TicketFacade ticketFacade, ContractTypeFacade contractTypeFacade) throws FileNotFoundException {
        super();
        
        this.userFacade = userFacade;        
        this.ticketFacade = ticketFacade;
        this.contractTypeFacade = contractTypeFacade;

        this.userViewModel = new UserViewModel(userFacade);        
        this.ticketViewModel = new TicketViewModel(ticketFacade);        
        this.contractTypeViewModel = new ContractTypeViewModel(contractTypeFacade);        

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

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        gridDashboard.setPrefWidth(primScreenBounds.getWidth());
        gridDashboard.prefHeight(primScreenBounds.getHeight());

        //txtTitle.setText("Dashboard");
        resetGridpane(gridContent);
        initializeGridPane(3, 2, 300, 300);

        String[] itemNames = new String[] {};
        String[] itemIcons = new String[] {};

        switch (userFacade.giveUserRole().toUpperCase()) {
            case "ADMINISTRATOR" -> {
                itemNames = new String[]{"manage employees", "manage customers"};
                itemIcons = new String[]{"icon_manage", "icon_manage"};
            }
            case "SUPPORT_MANAGER" -> {
                itemNames = new String[]{"manage knowledge base", "outstanding tickets", "resolved tickets", "statistics", "manage contract types", "manage contracts"};
                itemIcons = new String[]{"icon_manage", "icon_outstanding", "icon_resolved", "icon_statistics", "icon_manage", "icon_manage"};
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
        hboxMenu.getChildren().clear();
        
        //int width = (int) (primScreenBounds.getWidth()/(itemNames.length+1) - (gap*itemNames.length));
        for (String text : itemNames) {
            //gridMenu.addColumn(i);
            //gridMenu.getColumnConstraints().add(new ColumnConstraints(width, 100, -1, Priority.ALWAYS, HPos.CENTER, false));
            Button button = createMenuItemButton(text, itemNames.length);
            button.setOnMouseClicked(e -> {

                buttonMenusClicked(text);
            });
            hboxMenu.getChildren().add(button);
        }
    }
    
    private Button createMenuItemButton(String s, int numberOfItems){
        if (numberOfItems > 4){
            s = s.replace(" ", "\n");
        }

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        Button button = new Button(s);
        button.getStyleClass().add("menuButton");
        button.setTextFill(Color.WHITE);
        button.setAlignment(Pos.CENTER);
        button.setTextAlignment(TextAlignment.CENTER);
        button.prefWidthProperty().bind(hboxMenu.widthProperty().divide(numberOfItems));
        button.prefHeightProperty().bind(hboxMenu.heightProperty());

        if (numberOfItems < 4) {
            button.setMaxHeight(75);
            button.setMaxWidth((primScreenBounds.getWidth() * 0.7) / (numberOfItems * 1.75));
        }

//        button.setPadding(new Insets(5, paddingX, 5, paddingX));
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

    private void buttonMenusClicked(String name) {
        hboxMenu.getChildren().forEach(child -> child.getStyleClass().remove("menuButton-active"));
        hboxMenu.getChildren().forEach(child -> {
            if (((Button) child).getText().replace("\n", " ").toLowerCase().contains(name.toLowerCase())) {
                child.getStyleClass().add("menuButton-active");
            }
        });

        if (name.toLowerCase().contains("manage") && name.toLowerCase().contains("employee")) {
            //Todo weird => fixed? or still weird?
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, userViewModel, GUIEnum.EMPLOYEE);
            switchToManageScreen(name, tableViewPanelCompanion, userViewModel);
        } else if (name.toLowerCase().contains("manage") && name.toLowerCase().contains("customer")) {
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, userViewModel, GUIEnum.CUSTOMER);
            switchToManageScreen(name, tableViewPanelCompanion, userViewModel);
        } else if (name.toLowerCase().contains("ticket") && name.toLowerCase().contains("outstanding")) {
            TicketStatus.setOutstanding(true);
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, ticketViewModel, GUIEnum.TICKET);
            switchToManageScreen(name, tableViewPanelCompanion, ticketViewModel);
        } else if (name.toLowerCase().contains("ticket") && name.toLowerCase().contains("resolved")) {
            TicketStatus.setOutstanding(false);
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, ticketViewModel, GUIEnum.TICKET);
            switchToManageScreen(name, tableViewPanelCompanion, ticketViewModel);
        } else if(name.toLowerCase().contains("manage") && name.toLowerCase().contains("contract type")) {
            tableViewPanelCompanion = new TableViewPanelCompanion<>(this, contractTypeViewModel, GUIEnum.CONTRACTTYPE);
            switchToManageScreen(name, tableViewPanelCompanion, contractTypeViewModel);
        } else {
            makePopUp(name);
        }
    }

	private void switchToManageScreen(String name, TableViewPanelCompanion<T> tableViewPanelCompanion, ViewModel viewModel) {
		//txtTitle.setText(name);
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
        txtName.setText(String.format("%s %s" , userFacade.giveUserFirstName(), userFacade.giveUserLastName()));
        txtEmployeeRole.setText(userFacade.giveUserRole());
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

        alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        alert.showAndWait();
    }
    
}
