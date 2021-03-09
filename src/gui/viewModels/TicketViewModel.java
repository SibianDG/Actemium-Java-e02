package gui.viewModels;

import java.util.*;

import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.Ticket;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.facades.TicketFacade;
import gui.GUIEnum;
import javafx.collections.ObservableList;

public class TicketViewModel extends ViewModel {

    private GUIEnum currentState;
    private Ticket selectedTicket;
    private final TicketFacade ticketFacade;

    public TicketViewModel(TicketFacade ticketFacade) {
        super();
        this.ticketFacade = ticketFacade;
        setCurrentState(GUIEnum.TICKET);
    }

	public ObservableList<Ticket> giveTickets() {
		return ticketFacade.giveActemiumTickets();
	}

	public ObservableList<Ticket> giveTicketsOutstanding() {
		return ticketFacade.giveActemiumTicketsOutstanding();
	}

	public ObservableList<Ticket> giveTicketsResolved() {
		return ticketFacade.giveActemiumTicketsResolved();
	}

    public Ticket getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(Ticket ticket) {
        this.selectedTicket = ticket;
        if (ticket != null){
        	// substring(8) to remove ACTEMIUM
            setCurrentState(GUIEnum.valueOf(ticket.getClass().getSimpleName().substring(8).toUpperCase()));
        }
        fireInvalidationEvent();
    }   
    
    public ArrayList<String> getDetailsNewTicket(){
        return new ArrayList<String>(Arrays.asList("Title", "Creation date", "Priority", "Type", "CustomerId", "Description", "Remarks", "Attachments"));
    }
    
    public Map<String, Map<Boolean, Object>> getDetails() {
        Ticket ticket = selectedTicket;
        Map<String, Map<Boolean, Object>> details = new LinkedHashMap<>();
        details.put("Title", Collections.singletonMap(true, ticket.getTitle()));
        details.put("Creation date", Collections.singletonMap(false, ticket.getDateOfCreation().toString()));
        details.put("Priority", Collections.singletonMap(true, ticket.getPriorityAsEnum()));
        details.put("Type", Collections.singletonMap(true, ticket.getTicketTypeAsEnum()));
        details.put("Status", Collections.singletonMap(true, ticket.getStatusAsEnum()));
        details.put("Description", Collections.singletonMap(true, ticket.getDescription()));
        details.put("Customer/Company", Collections.singletonMap(false, ticket.giveCustomer().getCompany().getName()));
        //details.put("Technician", ticket.getTechnicians().toString());
        details.put("Remarks", Collections.singletonMap(true, ticket.getRemarks()));
        details.put("Attachments", Collections.singletonMap(true, ticket.getAttachments()));
        
        return details;   
    }
    
    public String getIdOfSelectedTicket() {
        return selectedTicket.getTicketIdString();
    }

    public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
                               String remarks, String attachments, long customerId) {
        ticketFacade.registerTicket(priority, ticketType, title, description, remarks, attachments, customerId);
        setSelectedTicket(ticketFacade.getLastAddedTicket());
    }

    // TODO
    // Cannot modify customer of the ticket, needs to be unmodifiable field
    // should become Company instead of customer
    public void modifyTicket(TicketPriority priority, TicketType ticketType, TicketStatus status, String title, String description,
                             String remarks, String attachments, List<ActemiumEmployee> technicians) {
        ticketFacade.modifyTicket((ActemiumTicket) selectedTicket, priority, ticketType, status, title, description, remarks, attachments, technicians);
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }

    @Override
    public void delete() {
        ticketFacade.delete((ActemiumTicket) selectedTicket);
    }
}
