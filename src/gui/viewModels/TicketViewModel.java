package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.Ticket;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.facades.TicketFacade;
import gui.GUIEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketViewModel extends ViewModel {

    private GUIEnum currentState;
    private Ticket selectedActemiumTicket;
    private final TicketFacade ticketFacade;
    private ObservableList<Ticket> actemiumTickets;

//    private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

    public TicketViewModel(TicketFacade ticketFacade) {
        super();
        this.ticketFacade = ticketFacade;
        this.actemiumTickets = FXCollections.observableArrayList();
        setCurrentState(GUIEnum.TICKET);
    }

    public ObservableList<Ticket> getActemiumTickets() {
        return FXCollections.unmodifiableObservableList(actemiumTickets);
    }
    
    public void setActemiumTickets(ObservableList<Ticket> observableList) {
        this.actemiumTickets = observableList;
    }

    public Ticket getSelectedActemiumTicket() {
        return selectedActemiumTicket;
    }

    public void setSelectedActemiumTicket(Ticket ticket) {
        this.selectedActemiumTicket = ticket;
        if (ticket != null){
        	// substring(8) to remove ACTEMIUM
        	//TODO obsolete
            setCurrentState(GUIEnum.valueOf(ticket.getClass().getSimpleName().substring(8).toUpperCase()));
        }
        fireInvalidationEvent();
    }   
    
    public ArrayList<String> getDetailsNewTicket(){
        return new ArrayList<String>(Arrays.asList("Title", "Creation date", "Priority", "Type", "CustomerId", "Description", "Remarks", "Attachments"));
    }
    
    public Map<String, Object> getDetails() {
        Ticket ticket = selectedActemiumTicket;
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("Title", ticket.getTitle());
        details.put("Creation date", ticket.getDateOfCreation().toString());
        details.put("Priority", ticket.getPriorityAsEnum());
        details.put("Type", ticket.getTicketTypeAsEnum());
        details.put("Status", ticket.getStatusAsEnum());
        details.put("Description", ticket.getDescription());
        details.put("Customer/Company", ticket.giveCustomer().getCompany().getName());
        //details.put("Technician", ticket.getTechnicians().toString());
        details.put("Remarks", ticket.getRemarks());
        details.put("Attachments", ticket.getAttachments());
        
        return details;   
    }
    
    public String getIdOfSelectedTicket() {
        return selectedActemiumTicket.getTicketIdString();
    }

    public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
                               String remarks, String attachments, long customerId) {
        ticketFacade.registerTicket(priority, ticketType, title, description, remarks, attachments, customerId);
        setSelectedActemiumTicket(ticketFacade.getLastAddedTicket());
    }

    // TODO
    // Cannot modify customer of the ticket, needs to be unmodifiable field
    // should become Company instead of customer
    public void modifyTicket(TicketPriority priority, TicketType ticketType, TicketStatus status, String title, String description,
                             String remarks, String attachments, List<ActemiumEmployee> technicians) {
        ticketFacade.modifyTicket((ActemiumTicket) selectedActemiumTicket, priority, ticketType, status, title, description, remarks, attachments, technicians);
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }
 
}
