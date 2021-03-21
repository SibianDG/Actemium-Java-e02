package gui.detailPanels;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.TicketChange;
import gui.viewModels.TicketViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import languages.LanguageResource;


public class TicketHistoryPanelController extends GridPane implements InvalidationListener {

    private final TicketViewModel ticketViewModel;

    @FXML
    private Text txtTicketHistoryTitle;

    @FXML
    private GridPane gridDetails;
    
    @FXML
    private Button btnClose;    

    private GridPane gridContent; 

    public TicketHistoryPanelController(TicketViewModel ticketViewModel, GridPane gridContent) {
        this.ticketViewModel = ticketViewModel;
        this.gridContent = gridContent;
        ticketViewModel.addListener(this);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketHistoryPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gridDetails.setHgap(10);
        gridDetails.setVgap(3);
        btnClose.setText(LanguageResource.getString("close"));

        setTicketHistoryPane();
    }    

    @FXML
    void btnCloseOnAction(ActionEvent event) {
		gridContent.getChildren().remove(this);
    }

    @Override
    public void invalidated(Observable observable) {
		gridContent.getChildren().remove(this);
    }

    private void setTicketHistoryPane(){
        initGridDetails();

        addGridDetails();
            
        txtTicketHistoryTitle.setText(String.format("%s: %s", LanguageResource.getString("history_of_ticket"), ticketViewModel.getIdSelectedTicket()));
        btnClose.setVisible(true);
    }

    private void addGridDetails(){
        int i = 0;
        
        // Added extra empty field because of weird first Vgap
        TextField empty = new TextField("");
        empty.getStyleClass().clear();
        gridDetails.add(empty, 0, i++);
        
        List<TicketChange> ticketChanges = ticketViewModel.getSelectedTicket().giveTicketChanges();
        for (TicketChange change : ticketChanges) {
        	List<Node> dateTimeNodeList = createDateTimeNodeList(change);
			for (int j = 0; j < 2; j++) {
				gridDetails.add(dateTimeNodeList.get(j), 0, i + j);
			}
        	List<Node> changeInfoNodeList = createChangeInfoNodeList(change);
			for (int j = 0; j < changeInfoNodeList.size(); j++) {
				gridDetails.add(changeInfoNodeList.get(j), 1, i + j);
			}
			i += changeInfoNodeList.size();
			TextField empty2 = new TextField("");
	        empty2.getStyleClass().clear();        
            gridDetails.add(empty2, 1, i);
            i++;
        }
    }

    private List<Node> createChangeInfoNodeList(TicketChange change) {
    	List<String> changeInfo = new ArrayList<>();
    	
		changeInfo.add(String.format("%s: %s %s", change.getUserRole(), change.getUser().getFirstName(), change.getUser().getLastName()));
		changeInfo.add(String.format("%s", change.getChangeDescription()));
		change.getChangeContent().forEach(c -> changeInfo.add(c));
		
    	List<Node> ciNodeList = changeInfo.stream()
    			.map(dt -> createChangeInfoNode(dt))
    			.collect(Collectors.toList());
		return ciNodeList;
    }
    
    private List<Node> createDateTimeNodeList(TicketChange change) {
    	List<String> changeDateTime = new ArrayList<>();
    	
    	changeDateTime.add(String.format("%s", change.getDateTimeOfChange().format(DateTimeFormatter.ISO_DATE)));
    	String time = change.getDateTimeOfChange().format(DateTimeFormatter.ISO_TIME);
    	changeDateTime.add(String.format("%s", time.substring(0, time.lastIndexOf("."))));
    	
    	List<Node> dtNodeList = changeDateTime.stream()
    			.map(dt -> createDateTimeNode(dt))
    			.collect(Collectors.toList());
    	return dtNodeList;    	
    }
    
    private Node createDateTimeNode(String string) {
    	TextField detail = new TextField(string);	
	    detail.setEditable(false);	
	    detail.getStyleClass().clear();
	    detail.setAlignment(Pos.CENTER_RIGHT);
	    Node node = detail;
        return node;
    }
    
    private Node createChangeInfoNode(String string) {
    	TextField detail = new TextField(string);	
    	detail.setEditable(false);	
    	detail.getStyleClass().clear();
    	Node node = detail;
    	return node;
    }

    private void initGridDetails() {
        gridDetails.getChildren().clear();
        gridDetails.getColumnConstraints().clear();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(20);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(80);

        gridDetails.getColumnConstraints().addAll(col0,col1);
    }
    
}
