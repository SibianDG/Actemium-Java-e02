package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import domain.Employee;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.UserStatus;
import domain.facades.ContractFacade;
import domain.facades.ContractTypeFacade;
import domain.facades.KnowledgeBaseFacade;
import domain.facades.TicketFacade;
import domain.facades.UserFacade;
import gui.detailPanels.ContractDetailsPanelController;
import gui.detailPanels.ContractTypeDetailsPanelController;
import gui.detailPanels.DetailsPanelController;
import gui.detailPanels.KnowledgeBaseDetailsPanelController;
import gui.detailPanels.TicketDetailsPanelController;
import gui.detailPanels.UserDetailsPanelController;
import gui.tableViewPanels.ContractTableViewPanelController;
import gui.tableViewPanels.ContractTypeTableViewPanelController;
import gui.tableViewPanels.CustomerTableViewPanelController;
import gui.tableViewPanels.EmployeeTableViewPanelController;
import gui.tableViewPanels.KnowledgeBaseTableViewPanelController;
import gui.tableViewPanels.TableViewPanelController;
import gui.tableViewPanels.TicketTableViewPanelController;
import gui.viewModels.ChartViewModel;
import gui.viewModels.ContractTypeViewModel;
import gui.viewModels.ContractViewModel;
import gui.viewModels.KnowledgeBaseViewModel;
import gui.viewModels.ProfileViewModel;
import gui.viewModels.TicketViewModel;
import gui.viewModels.UserViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import languages.LanguageResource;

public class DashboardFrameController <T,E> extends GridPane implements InvalidationListener {
	
	// Facades
    private final UserFacade userFacade;
	private final TicketFacade ticketFacade;
    private final ContractTypeFacade contractTypeFacade;
    private final ContractFacade contractFacade;
    private final KnowledgeBaseFacade knowledgeBaseFacade;

    // ViewModels
    private final UserViewModel userViewModel;
	private final TicketViewModel ticketViewModel;
	private final ContractTypeViewModel contractTypeViewModel;
	private final ContractViewModel contractViewModel;
	private final KnowledgeBaseViewModel knowledgeBaseViewModel;
	private final ProfileViewModel profileViewModel;
	private final ChartViewModel chartViewModel;
	
	//LoginController
	private LoginController loginController;

    //variable for enable menu button on change table view
    private boolean enabled = true;

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

    private final Set<DashboardTile> dashboardTiles = new HashSet<>();

    private TableViewPanelController<T,E> tableViewPanelCompanion;
    private DetailsPanelController detailsPanelController;
    private final ProfilePanelController profilePanelController;

    private final Map<String, String> textToImageMap = Map.of(
            LanguageResource.getString("manage_employees"), "icon_manage_user"
            , LanguageResource.getString("manage_customers"), "icon_manage_user"
            , LanguageResource.getString("manage_knowledge_base"), "icon_manage"
            , LanguageResource.getString("outstanding_tickets"), "icon_outstanding"
            , LanguageResource.getString("resolved_tickets"), "icon_resolved"
            , LanguageResource.getString("statistics"), "icon_statistics"
            , LanguageResource.getString("manage_contracts"), "icon_manage_contracts"
            , LanguageResource.getString("manage_contract_types"), "icon_manage_contract_types"
            , LanguageResource.getString("consult_knowledge_base"), "icon_consult"
    );

    public DashboardFrameController(UserFacade userFacade, TicketFacade ticketFacade, ContractTypeFacade contractTypeFacade, 
    		ContractFacade contractFacade, KnowledgeBaseFacade knowledgeBaseFacade, LoginController loginController) throws FileNotFoundException  {
        super();
        
        this.userFacade = userFacade;        
        this.ticketFacade = ticketFacade;
        this.contractTypeFacade = contractTypeFacade;
        this.contractFacade = contractFacade;
        this.knowledgeBaseFacade = knowledgeBaseFacade;

        this.userViewModel = new UserViewModel(userFacade);        
        this.ticketViewModel = new TicketViewModel(ticketFacade, userFacade);        
        this.contractTypeViewModel = new ContractTypeViewModel(contractTypeFacade);    
        this.contractViewModel = new ContractViewModel(contractFacade);    
        this.knowledgeBaseViewModel = new KnowledgeBaseViewModel(knowledgeBaseFacade, ticketFacade, userFacade);
        this.profileViewModel = new ProfileViewModel(userFacade);
        this.chartViewModel = new ChartViewModel(ticketFacade, userFacade, contractTypeFacade, contractFacade);

        this.loginController = loginController;
        this.profilePanelController = new ProfilePanelController(profileViewModel, this);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }

        profileViewModel.addListener(this);

        initializeDashboard();
        initializeText();

        imgLogo.setCursor(Cursor.HAND);
        imgMyAccount.setCursor(Cursor.HAND);
        if (((Employee) userViewModel.getSignedInUser()).getRole() == EmployeeRole.ADMINISTRATOR) {
            imgNotifications.setVisible(false);
            imgNotifications.setDisable(true);
        }

        if (((Employee) userViewModel.getSignedInUser()).getRole() == EmployeeRole.SUPPORT_MANAGER) {
            if ((int) userViewModel.giveCustomers().stream().filter(c -> c.getStatus() == UserStatus.IN_REQUEST).count() == 0 &&
                    (int) ticketViewModel.giveTicketsOutstanding().stream().filter(t -> t.giveTechnicians().size() == 0).count() == 0)
                imgNotifications.setImage(new Image("file:src/pictures/dashboard/icon_bell.png"));
            else
                imgNotifications.setImage(new Image("file:src/pictures/dashboard/icon_bell_notification.png"));
        } else if (((Employee) userViewModel.getSignedInUser()).getRole() == EmployeeRole.TECHNICIAN) {
            if (ticketViewModel.giveTicketsOutstanding().size() == 0)
                imgNotifications.setImage(new Image("file:src/pictures/dashboard/icon_bell.png"));
            else
                imgNotifications.setImage(new Image("file:src/pictures/dashboard/icon_bell_notification.png"));
        }

        imgNotifications.setCursor(Cursor.HAND);
        imgLogout.setCursor(Cursor.HAND);
    }

    public void initializeDashboard() throws FileNotFoundException {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        
        gridDashboard.setPrefWidth(primScreenBounds.getWidth());
        gridDashboard.prefHeight(primScreenBounds.getHeight());

        resetGridpane(gridContent);
        initializeGridPane(3, 2, 300, 300);

        String[] itemNames = new String[] {};

        switch (userFacade.giveUserRole().toUpperCase()) {
            case "ADMINISTRATOR" -> {
                itemNames = new String[]{LanguageResource.getString("manage_employees"), LanguageResource.getString("manage_customers")};
            }
            case "SUPPORT_MANAGER" -> {
                itemNames = new String[]{LanguageResource.getString("manage_knowledge_base"), LanguageResource.getString("outstanding_tickets")
                        , LanguageResource.getString("resolved_tickets"), LanguageResource.getString("statistics"), LanguageResource.getString("manage_contract_types")
                        , LanguageResource.getString("manage_contracts")};
            }
            case "TECHNICIAN" -> {
                itemNames = new String[]{LanguageResource.getString("consult_knowledge_base"), LanguageResource.getString("outstanding_tickets"),
                        LanguageResource.getString("resolved_tickets"), LanguageResource.getString("statistics")};
            }
        }
        	
        for(int i = 0; i < itemNames.length; i++) {
        	if(itemNames[i].equals(LanguageResource.getString("outstanding_tickets"))) {
        		StringBuilder text = new StringBuilder();
        		text.append(itemNames[i]);

        		int amountOfP1Tickets, amountOfP2Tickets, amountOfP3Tickets;
        		amountOfP1Tickets = (int) ticketViewModel.giveTicketsOutstanding().stream().filter((t -> t.getPriority() == TicketPriority.P1)).count();
        		amountOfP2Tickets = (int) ticketViewModel.giveTicketsOutstanding().stream().filter((t -> t.getPriority() == TicketPriority.P2)).count();
        		amountOfP3Tickets = (int) ticketViewModel.giveTicketsOutstanding().stream().filter((t -> t.getPriority() == TicketPriority.P3)).count();

        		text.append(String.format("%n(P1: %d, P2: %d, P3: %d)", amountOfP1Tickets, amountOfP2Tickets, amountOfP3Tickets));

        		addDashboardItem(text.toString(), createImageView(textToImageMap.get(itemNames[i]),i%2, 130), i%3, i/3, i);
        	}  else {
            addDashboardItem(itemNames[i], createImageView(textToImageMap.get(itemNames[i]),i%2, 130), i%3, i/3, i);
        	}
        }
        createGridMenu(itemNames);
    }

    private void createGridMenu(String[] itemNames) throws FileNotFoundException {
        hboxMenu.getChildren().clear();
        for (String text : itemNames) {
            Button button = createMenuItemButton(text, itemNames.length);
            button.setOnMouseClicked(e -> {
                if(tableViewPanelCompanion != null)
                    tableViewPanelCompanion.alertChangesOnTabelView();
             
                if (profilePanelController != null) 
                    profilePanelController.alertChanges();   
                              
                if (enabled) {
                    try {
                        buttonMenusClicked(text);
                    } catch (IOException ignored) { }
                }                               
            });
            hboxMenu.getChildren().add(button);
        }
    }
    
    private Button createMenuItemButton(String s, int numberOfItems) throws FileNotFoundException {
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
        button.setGraphic(createImageView(textToImageMap.get(s.replace("\n", " ")), 1, 35));

        if (numberOfItems < 4) {
            button.setMaxHeight(75);
            button.setMaxWidth((primScreenBounds.getWidth() * 0.5) / (numberOfItems));
        }

        return button;
    }

    public ImageView createImageView(String image, int i, int size) throws FileNotFoundException {

        FileInputStream input = new FileInputStream("src/pictures/dashboard/dashboarditems/" + image + "_" + i + ".png");

        ImageView imageView = new ImageView(new Image(input));
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);
        return imageView;
    }

    private void addDashboardItem(String name, ImageView imageView, int x, int y, int i) {
        DashboardTile dashboardTile = new DashboardTile(imageView, name, i%2);
        dashboardTile.setOnMouseClicked(e -> {
            try {
                buttonMenusClicked(name);
            } catch (IOException ignored) {

            }
        });
        dashboardTiles.add(dashboardTile);
        gridContent.add(dashboardTile, x, y);
        setFillHeight(dashboardTile, true);
        setFillWidth(dashboardTile, true);
        gridContent.setHgap(35);
        gridContent.setVgap(35);
    }

    private void resetMenuButtons(){
        hboxMenu.getChildren().forEach(child -> {
            if (child.getStyleClass().contains("menuButton-active")){
                child.getStyleClass().remove("menuButton-active");
                try {
                    String text = ((Button) child).getText();
                    ((Button)child).setGraphic(createImageView(textToImageMap.get(text.replace("\n", " ")), 1, 35));
                } catch (FileNotFoundException ignored) {                }
            }
        });
    }

    private void buttonMenusClicked(String name) throws IOException {
        if (name.toLowerCase().contains("outstanding tickets"))
            name = "outstanding tickets";
        resetMenuButtons();
        String finalName = name;
        hboxMenu.getChildren().forEach(child -> {
            if (((Button) child).getText().replace("\n", " ").toLowerCase().contains(finalName.toLowerCase())) {
                child.getStyleClass().add("menuButton-active");
                try {
                    ((Button)child).setGraphic(createImageView(textToImageMap.get(finalName), 0, 35));
                } catch (FileNotFoundException ignored) {
                }

            }
        });

        if (name.toLowerCase().contains(LanguageResource.getString("manage").toLowerCase()) && name.toLowerCase().contains(LanguageResource.getString("employee").toLowerCase())) {
            tableViewPanelCompanion = new EmployeeTableViewPanelController<>(this, userViewModel, GUIEnum.EMPLOYEE, EmployeeRole.valueOf(userFacade.giveUserRole()));
            switchToManageScreen(name, tableViewPanelCompanion, userViewModel);
        } else if (name.toLowerCase().contains(LanguageResource.getString("manage").toLowerCase()) && name.toLowerCase().contains(LanguageResource.getString("customer").toLowerCase())) {
            tableViewPanelCompanion = new CustomerTableViewPanelController<>(this, userViewModel, GUIEnum.CUSTOMER, EmployeeRole.valueOf(userFacade.giveUserRole()));
            switchToManageScreen(name, tableViewPanelCompanion, userViewModel);
        } else if (name.toLowerCase().contains(LanguageResource.getString("ticket").toLowerCase()) && name.toLowerCase().contains(LanguageResource.getString("outstanding").toLowerCase())) {
            TicketStatus.setOutstanding(true);
            tableViewPanelCompanion = new TicketTableViewPanelController<>(this, ticketViewModel, GUIEnum.TICKET, EmployeeRole.valueOf(userFacade.giveUserRole()));
            switchToManageScreen(name, tableViewPanelCompanion, ticketViewModel);
        } else if (name.toLowerCase().contains(LanguageResource.getString("ticket").toLowerCase()) && name.toLowerCase().contains(LanguageResource.getString("resolved").toLowerCase())) {
            TicketStatus.setOutstanding(false);
            tableViewPanelCompanion = new TicketTableViewPanelController<>(this, ticketViewModel, GUIEnum.TICKET, EmployeeRole.valueOf(userFacade.giveUserRole()));
            switchToManageScreen(name, tableViewPanelCompanion, ticketViewModel);
        } else if (name.toLowerCase().contains(LanguageResource.getString("manage").toLowerCase()) && name.toLowerCase().contains(LanguageResource.getString("contract_type").toLowerCase())) {
            tableViewPanelCompanion = new ContractTypeTableViewPanelController<>(this, contractTypeViewModel, GUIEnum.CONTRACTTYPE, EmployeeRole.valueOf(userFacade.giveUserRole()));
            switchToManageScreen(name, tableViewPanelCompanion, contractTypeViewModel);
        } else if (name.toLowerCase().contains(LanguageResource.getString("manage").toLowerCase()) && name.toLowerCase().contains(LanguageResource.getString("contracts").toLowerCase())) {
        	tableViewPanelCompanion = new ContractTableViewPanelController<>(this, contractViewModel, GUIEnum.CONTRACT, EmployeeRole.valueOf(userFacade.giveUserRole()));
        	switchToManageScreen(name, tableViewPanelCompanion, contractViewModel);
        } else if ((name.toLowerCase().contains(LanguageResource.getString("manage").toLowerCase()) || name.toLowerCase().contains(LanguageResource.getString("consult").toLowerCase()) ) && name.toLowerCase().contains(LanguageResource.getString("knowledge").toLowerCase())) {
        	tableViewPanelCompanion = new KnowledgeBaseTableViewPanelController<>(this, knowledgeBaseViewModel, GUIEnum.KNOWLEDGEBASE, EmployeeRole.valueOf(userFacade.giveUserRole()));
        	switchToManageScreen(name, tableViewPanelCompanion, knowledgeBaseViewModel);
        } else if (name.toLowerCase().contains(LanguageResource.getString("statistics"))) {
            resetGridpane(gridContent);
            initializeGridPane(1, 1, 600, 600);
            gridContent.add(new ChartController(this, chartViewModel), 0, 0);
        } else {
            makePopUp(name);
        }
    }

	private void switchToManageScreen(String name, TableViewPanelController<T,E> tableViewPanelCompanion, ViewModel viewModel) {
    	// Necessary!!
    	// if listener is never removed when you press the home button
        // everytime you press an item in the tableView
        // it will run setDetailOnModifying() multiple times instead of once
    	// if you find a cleaner way to do this, please let me know xoxo
		if (detailsPanelController != null) {
			detailsPanelController.getViewModel().removeListener(detailsPanelController);
		}
		resetGridpane(gridContent);

		switch(viewModel.getClass().getSimpleName()) {
		case "UserViewModel" -> detailsPanelController = new UserDetailsPanelController(viewModel, gridContent);
		case "TicketViewModel" -> detailsPanelController = new TicketDetailsPanelController(viewModel, gridContent, userFacade.getEmployeeRole());
		case "ContractTypeViewModel" -> detailsPanelController = new ContractTypeDetailsPanelController(viewModel, gridContent);
		case "ContractViewModel" -> detailsPanelController = new ContractDetailsPanelController(viewModel, gridContent);
		case "KnowledgeBaseViewModel" -> detailsPanelController = new KnowledgeBaseDetailsPanelController(viewModel, gridContent);
		}
		gridContent.add(tableViewPanelCompanion, 0, 0);
		gridContent.add(detailsPanelController, 1, 0);
	}

    private void resetGridpane(GridPane gridPane){
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
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
		if (detailsPanelController != null) {
			detailsPanelController.getViewModel().removeListener(detailsPanelController);
		}
        makePopUp(LanguageResource.getString("logout_message"));   
        
        Scene scene = this.loginController.getScene();
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle(LanguageResource.getString("login"));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnNotificationAction(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Notifications");
        alert.setTitle("Notifications");

        if (((Employee) userViewModel.getSignedInUser()).getRole() == EmployeeRole.SUPPORT_MANAGER)
            alert.setContentText(String.format("Contracts in Request: %d%nTickets with unassigned technicians: %d%n",
                    (int) userViewModel.giveCustomers().stream().filter(c -> c.getStatus() == UserStatus.IN_REQUEST).count(),
                    (int) ticketViewModel.giveTicketsOutstanding().stream().filter(t -> t.giveTechnicians().size() == 0).count()
            ));
        else if (((Employee) userViewModel.getSignedInUser()).getRole() == EmployeeRole.TECHNICIAN)
            alert.setContentText(String.format("Outstanding tickets: %d%n",
                    ticketViewModel.giveTicketsOutstanding().size()
            ));

        alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        alert.showAndWait();
    }



    @FXML
    void btnProfileAction(MouseEvent event) {
    	resetGridpane(gridContent);
    	initializeGridPane(1, 1, 600, 600);    
        
    	gridContent.add(this.profilePanelController, 0, 0);
        hboxMenu.getChildren().forEach(child -> {
        		child.getStyleClass().remove("menuButton-active");
                String text = ((Button) child).getText();
                try {
					((Button)child).setGraphic(createImageView(textToImageMap.get(text.replace("\n", " ")), 1, 35));
				} catch (FileNotFoundException ignored) {

				}
        	});
    }

    @FXML
    void navigateToHome(MouseEvent event) throws FileNotFoundException {
    	// Necessary!!
    	// if listener is never removed when you press the home button
        // everytime you press an item in the tableView
        // it will run setDetailOnModifying() multiple times instead of once
    	if (detailsPanelController != null) {
    		detailsPanelController.getViewModel().removeListener(detailsPanelController);
    	}
        initializeDashboard();
    }

    private void makePopUp(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, text);

        alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
        alert.getDialogPane().getStyleClass().add("alert");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));
        alert.showAndWait();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
        
    @Override
    public void invalidated(Observable observable) {
        txtName.setText(String.format("%s %s" , userFacade.giveUserFirstName(), userFacade.giveUserLastName()));
    }

}
