package gui.tableViewPanels;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import domain.enums.EmployeeRole;
import gui.DashboardFrameController;
import gui.GUIEnum;
import gui.viewModels.ViewModel;
import javafx.beans.property.Property;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import languages.LanguageResource;


public abstract class TableViewPanelController<T, E> extends GridPane {

	//---------ALL THE CASES FOR THE FILTERS WITH LanguageResource ---------------//
	//enum employee { firstname(LanguageResource.getString("firstname")}
	//----------------------------------------------------------------------------//


	//private UserFacade userFacade;
	protected final DashboardFrameController dashboardFrameController;
	protected final ViewModel viewModel;
	protected GUIEnum currentState;
	protected EmployeeRole employeeRole;

	@FXML
	protected Button btnAdd;
	
	@FXML
	protected Button btnResetFilters;
	
	@FXML
	protected Button btnP1;

	@FXML
	protected Button btnP2;

	@FXML
	protected Button btnP3;

    @FXML
    protected Text txtFilter;

	@FXML
	protected HBox hboxFilterSection;
	
    @FXML
    protected TableView<T> tableView;
    
//	protected Map<String, Function<T, Property<E>>> propertyMap = new LinkedHashMap<>();

//	private ObservableList<T> mainData;
	protected FilteredList<T> tableViewData;
	protected SortedList<T> tableViewDataSorted;
	
	public TableViewPanelController(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState, EmployeeRole employeeRole) {
		this.dashboardFrameController = dashboardFrameController;
		this.viewModel = viewModel;
		this.currentState = currentState;
		this.employeeRole = employeeRole;
		
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableViewPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		btnP1.setVisible(false);
		btnP2.setVisible(false);
		btnP3.setVisible(false);
		btnResetFilters.setText(LanguageResource.getString("reset_filters"));

		btnAdd.setVisible(!employeeRole.equals(EmployeeRole.TECHNICIAN));		
	}	

	@FXML
	void showFilterOnBtnP1(MouseEvent event) {
		//
	}
	@FXML
	void showFilterOnBtnP2(MouseEvent event) {
		//
	}
	@FXML
	void showFilterOnBtnP3(MouseEvent event) {
		//
	}
	
	@FXML
	void resetFiltersOnMouseClicked(MouseEvent event) {
		tableViewData.setPredicate(p -> true);
		hboxFilterSection.getChildren().forEach(f -> {
			if (f instanceof ComboBox)
				((ComboBox<?>) f).getSelectionModel().select(0);
			else if (f instanceof TextField)
				((TextField) f).setText("");
		});
	}
	
	@FXML
	void addOnMouseClicked(MouseEvent event) {
		//
	}
	
	//TODO
	protected <T> TableColumn<T, E> createColumn(String title, Function<T, Property<E>> prop) {
		
		TableColumn<T, E> column = new TableColumn<>(title);
		column.setCellValueFactory(cellData -> prop.apply(cellData.getValue()));
		// Resize policy for Title column in Tickets TableView
		if(title.equalsIgnoreCase(LanguageResource.getString("title"))) {
			//TODO
			column.setPrefWidth(320.00);
		}
		return column;	
	}
	
	//TODO
	protected TextField createTextFieldFilter(String o) {
		String string = o;
		TextField filter = new TextField();
		filter.setPromptText(string);
		filter.setPrefWidth(145);
		filter.setFont(Font.font("Arial", 14));
		return filter;		
	}

	protected void initializeTableViewSuper() {
		// For column header sort
		tableViewDataSorted = tableViewData.sorted();
		tableViewDataSorted.comparatorProperty().bind(tableView.comparatorProperty());		
		tableView.setItems(tableViewDataSorted);
	}
	
	public boolean alertChangesOnTabelView() {
		boolean showNewObject = true;
		if(viewModel.isFieldModified()) {
			//popup
			Alert alert = new Alert(AlertType.CONFIRMATION);

			alert.setTitle(LanguageResource.getString("modifiedWithoutSaving"));
			alert.setHeaderText(LanguageResource.getString("unsavedChanges"));
			alert.setContentText(LanguageResource.getString("chooseOption"));

			alert.getDialogPane().getStylesheets().add("file:src/start/styles.css");
			alert.getDialogPane().getStyleClass().add("alert");
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/pictures/icon.png")));

			ButtonType discardChanges = new ButtonType(LanguageResource.getString("discardChanges"));
			ButtonType buttonTypeCancel = new ButtonType(LanguageResource.getString("keepEditing"), ButtonData.CANCEL_CLOSE);
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
				viewModel.setFieldModified(false);
			} else {
				// ... user chose CANCEL or closed the dialog
				// nothing happens -> back to same detail panel
				dashboardFrameController.setEnabled(false);
				showNewObject = false;
			}
		}		
		return showNewObject;		
	}
		
	protected void setPredicateForFilteredList(Predicate newPredicate){
		Predicate currentPredicate = tableViewData.getPredicate();
		Predicate resultPredicate;
		if(currentPredicate != null){
			resultPredicate = currentPredicate.and(newPredicate);
		} else {
			resultPredicate = newPredicate;
		}
		tableViewData.setPredicate((Predicate<? super T>) resultPredicate);
	}
	
	
	protected abstract void initializeFilters();
	
	protected abstract Node createFilterNode(Object o);
	
	protected abstract ComboBox makeComboBox(Object o);
	
	protected abstract void checkFilters();
	
	protected abstract Predicate giveFilterPredicate(String fieldName, String filterText);
	
	protected abstract void initializeTableViewSub();
	
}
