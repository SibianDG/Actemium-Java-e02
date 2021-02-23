package gui;

import java.io.IOException;

import domain.controllers.DomainController;
import domain.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class TableViewPanelController extends GridPane{

	private DomainController domainController;
	
    @FXML
    private Button btnAdd;

    @FXML
    private Text txtFilter;
	
	@FXML
	private TableView<Employee> tvUsers;
	
	@FXML
	private TableColumn<Employee, String> usernameCol;
	
	@FXML
	private TableColumn<Employee, String> fistnameCol;
	
	@FXML
	private TableColumn<Employee, String> lastnameCol;
	
	@FXML
	private TableColumn<Employee, String> statusCol;
	
	public TableViewPanelController(DomainController domainController) {
		this.domainController = domainController;
		
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableViewPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		/*usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
		
		tvUsers.setItems(domainController.giveEmployeeList());*/
		
	}
	
	
	
	private void initializeTableView() {
		
	}
}
