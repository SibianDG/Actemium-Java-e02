package gui.tableViewPanels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import domain.KbItem;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.enums.UserStatus;
import gui.DashboardFrameController;
import gui.GUIEnum;
import gui.viewModels.KnowledgeBaseViewModel;
import gui.viewModels.ViewModel;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import languages.LanguageResource;


public class KnowledgeBaseTableViewPanelController<T,E> extends TableViewPanelController {
	
//  @FXML
//  private TableView<T> tableView;
	
	private Map<String, Function<T, Property<E>>> propertyMap = new LinkedHashMap<>();

	private ObservableList<T> mainData;
//	private FilteredList<T> tableViewData;
//	private SortedList<T> tableViewDataSorted;
	
	public KnowledgeBaseTableViewPanelController(DashboardFrameController dashboardFrameController, ViewModel viewModel, GUIEnum currentState, EmployeeRole employeeRole) {
		super(dashboardFrameController, viewModel, currentState, employeeRole);				
		
		this.mainData = (ObservableList<T>) ((KnowledgeBaseViewModel) viewModel).giveKbItems();
		this.tableViewData = new FilteredList<>(mainData);
		propertyMap.put(LanguageResource.getString("title"), item -> (Property<E>)((KbItem) item).titleProperty());
		propertyMap.put(LanguageResource.getString("type"), item -> (Property<E>)((KbItem) item).typeProperty());
		btnAdd.setText(String.format("%s %s", LanguageResource.getString("add"), currentState.toString().toLowerCase()));
			
		initializeFilters();
		initializeTableViewSub();
	}
	
	@FXML
	void addOnMouseClicked(MouseEvent event) {
		if (alertChangesOnTabelView()) {		
			((KnowledgeBaseViewModel) viewModel).setCurrentState(GUIEnum.KNOWLEDGEBASE);
			((KnowledgeBaseViewModel) viewModel).setSelectedKbItem(null);		
		}
	}
		
	private void initializeFilters() {
		Map<GUIEnum, ArrayList<Object>> filterMap = new HashMap<>();
		filterMap.put(GUIEnum.KNOWLEDGEBASE, new ArrayList<>(Arrays.asList(LanguageResource.getString("title"), KbItemType.DATABASE, LanguageResource.getString("keywords"), LanguageResource.getString("FullSearch"))));

		filterMap.get(currentState).forEach(o -> hboxFilterSection.getChildren().add(createElementDetailGridpane(o)));
	}

	private Node createElementDetailGridpane(Object o) {
		if (o instanceof String) {
			String string = (String) o;
			TextField filter = new TextField();
			filter.setPromptText(string);
			filter.setPrefWidth(145);
			filter.setFont(Font.font("Arial", 14));
			filter.setOnKeyTyped(event -> {
				checkFilters();
			});
			return filter;
		} else if (o instanceof Enum) {
			return makeComboBox(o);
		}
		return null;
	}

	//TODO
	private ComboBox makeComboBox(Object o){
		String itemText;
		ArrayList<String> stringArrayList;

		ObservableList list;
		
	    switch(o.getClass().getSimpleName()) {	       
	        case "KbItemType" -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_type").toUpperCase()));
				Arrays.asList(KbItemType.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "KbItemType";
	        }      
	        default -> {
	        	stringArrayList = new ArrayList<>(Collections.singleton(LanguageResource.getString("select_status").toUpperCase()));
				Arrays.asList(UserStatus.values()).forEach(string -> stringArrayList.add(string.toString()));
				itemText = "Status";
	        }
	    } 	
	    
		list = FXCollections.observableList(stringArrayList);
		ComboBox c = new ComboBox(list);
		
		switch(itemText) {
	        case "KbItemType" -> c.getSelectionModel().select(LanguageResource.getString("select_type").toUpperCase());
	        default -> c.getSelectionModel().select(LanguageResource.getString("select").toUpperCase());
	    } 
		//TODO e.g. select technician and admin at the same time
//		c.getSelectionModel().select(SelectionMode.MULTIPLE);
		c.valueProperty().addListener(e -> {
			checkFilters();
		});
		return c;
	}

	private void checkFilters(){		
		List<Predicate> predicates = new ArrayList<>();

		hboxFilterSection.getChildren().forEach(object -> {
			if (object instanceof TextField) {
				TextField textField = (TextField) object;
				if (textField.getText().trim().length() > 0){
					predicates.add(giveFilterPredicate(textField.getPromptText(), textField.getText().trim().toLowerCase()));
				}
			} else if (object instanceof ComboBox) {
				ComboBox comboBox = (ComboBox) object;

				if (comboBox.getSelectionModel().getSelectedItem() != null &&  !comboBox.getSelectionModel().getSelectedItem().toString().contains("SELECT")) {
					
					ArrayList<KbItemType> kbItemTypeArrayList = new ArrayList<>(Arrays.asList(KbItemType.values()));
					List<String> kbItemTypeStringArray = kbItemTypeArrayList.stream().map(KbItemType::toString).collect(Collectors.toList());

					String selectedItem = comboBox.getSelectionModel().getSelectedItem().toString();
					
					if (kbItemTypeStringArray.contains(selectedItem)) {
						predicates.add(giveFilterPredicate("KbItemType", selectedItem.toLowerCase()));
					}
				}
			}
		});
		// Reset all filters
		tableViewData.setPredicate(p -> true);
		// Create one combined predicate by iterating over the list
		predicates.forEach(p -> setPredicateForFilteredList(p));
	}

	private void initializeTableViewSub() {
		propertyMap.forEach((key, prop) -> {
			TableColumn<T, E> c = createColumn(key, prop);
			tableView.getColumns().add(c);
		});

		tableViewDataSorted = tableViewData.sorted();
		tableViewDataSorted.comparatorProperty().bind(tableView.comparatorProperty());
		
		tableView.setItems(tableViewDataSorted);
		
		tableView.setOnMouseClicked((MouseEvent m) -> {
			if (alertChangesOnTabelView()) {
				T data = (T) tableView.getSelectionModel().selectedItemProperty().get();				
				((KnowledgeBaseViewModel) viewModel).setSelectedKbItem((KbItem) data);				
			}
		});

		/*tableView.setOnKeyPressed( keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				viewModel.delete();
			}
		});*/
	}

	private Predicate giveFilterPredicate(String fieldName, String filterText){
		fieldName = fieldName.toLowerCase();		

		if (fieldName.length() > 0 && !filterText.toLowerCase().contains(LanguageResource.getString("select"))){

			Predicate<KbItem> newPredicate;

			if (fieldName.equalsIgnoreCase(LanguageResource.getString("title")) || fieldName.equalsIgnoreCase("title")){
				newPredicate = e -> e.getTitle().toLowerCase().contains(filterText);
			} else if (fieldName.equalsIgnoreCase("kbitemtype")){
				newPredicate = e -> e.getTypeAsString().toLowerCase().contains(filterText);
			} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("keywords")) || fieldName.equalsIgnoreCase("keywords")){
				newPredicate = e -> e.getKeywords().toLowerCase().contains(filterText);
			} else if (fieldName.equalsIgnoreCase(LanguageResource.getString("FullSearch")) || fieldName.equalsIgnoreCase("fullsearch")){
				newPredicate = e -> e.getText()
						.concat(" " + e.getTitle() + " " + e.getTypeAsString() + " " + e.getKeywords())
						.toLowerCase()
						.contains(filterText);
			} else {
				throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
			}
			
			//switch (fieldName) {
			//	case "title" -> newPredicate = e -> e.getTitle().toLowerCase().contains(filterText);
			//	case "kbitemtype" -> newPredicate = e -> e.getTypeAsString().toLowerCase().contains(filterText);
			//	case "keywords" -> newPredicate = e -> e.getKeywords().toLowerCase().contains(filterText);
			//	case "fullsearch" -> {
			//		newPredicate = e -> e.getText()
			//								.concat(" " + e.getTitle() + " " + e.getTypeAsString() + " " + e.getKeywords())
			//								.toLowerCase()
			//								.contains(filterText);
			//	}
			//	default -> throw new IllegalStateException(LanguageResource.getString("unexpectedValue") + " " + fieldName);
			//}
			System.out.println(newPredicate.toString());
			return newPredicate;				
		}
		return null;
	}
		
}
