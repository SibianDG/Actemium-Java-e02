package gui.viewModels;

import java.time.format.DateTimeFormatter;
import java.util.*;

import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.Employee;
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
    //private ObservableList<Employee> techniciansForTicket;
    private final TicketFacade ticketFacade;
    //private final UserFacade userFacade;

    public TicketViewModel(TicketFacade ticketFacade) {
        super();
        this.ticketFacade = ticketFacade;
        //his.userFacade = userFacade;
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
            //techniciansForTicket = ticket.giveTechnicians();
        }
        fireInvalidationEvent();
    }   
    
    public ArrayList<String> getDetailsNewTicket(){
        return new ArrayList<String>(Arrays.asList("Title", "Creation date", "Priority", "Type", "Customer ID", "Description", "Remarks", "Attachments"));
    }
    
    public Map<String, Map<Boolean, Object>> getDetails() {
        Ticket ticket = selectedTicket;
        Map<String, Map<Boolean, Object>> details = new LinkedHashMap<>();
        details.put("Title", Collections.singletonMap(true, ticket.getTitle()));
        details.put("Creation date", Collections.singletonMap(false, ticket.getDateOfCreation().format(DateTimeFormatter.ISO_DATE)));
        details.put("Creation time", Collections.singletonMap(false, ticket.getDateAndTimeOfCreation().format(DateTimeFormatter.ISO_TIME)));
        if (!TicketStatus.isOutstanding()) {
	        details.put("Completion date", Collections.singletonMap(false, ticket.getDateAndTimeOfCreation().format(DateTimeFormatter.ISO_DATE)));
	        details.put("Completion time", Collections.singletonMap(false, ticket.getDateAndTimeOfCreation().format(DateTimeFormatter.ISO_TIME)));
        }
        details.put("Priority", Collections.singletonMap(true, ticket.getPriorityAsEnum()));
        details.put("Type", Collections.singletonMap(true, ticket.getTicketTypeAsEnum()));
        details.put("Status", Collections.singletonMap(true, ticket.getStatusAsEnum()));
        details.put("Description", Collections.singletonMap(true, ticket.getDescription()));
        details.put("Customer/Company", Collections.singletonMap(false, ticket.giveCustomer().giveCompany().getName()));
        details.put("Technicians", Collections.singletonMap(true, ticket.giveTechnicians()));
        details.put("Remarks", Collections.singletonMap(true, ticket.getRemarks()));
        details.put("Attachments", Collections.singletonMap(true, ticket.getAttachments()));
        if (!TicketStatus.isOutstanding()) {
        	//TODO only the fields below should be editable in resloved tickets
        	// this means all the above fields should be set to false, ...
        	// How will we do this without using one big if/else block?
	        details.put("Solution", Collections.singletonMap(true, ticket.getSolution()));
	        details.put("Quality", Collections.singletonMap(true, ticket.getQuality()));
	        details.put("Support Needed", Collections.singletonMap(true, ticket.getSupportNeeded()));
        }
        
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
    public void modifyTicket(TicketPriority priority, TicketType ticketType, TicketStatus status, String title, String description,
                             String remarks, String attachments, List<ActemiumEmployee> technicians) {
        ticketFacade.modifyTicket((ActemiumTicket) selectedTicket, priority, ticketType, status, title, description, remarks, attachments, technicians);
    }

	public void modifyTicketOutstanding(String solution, String quality, String supportNeeded) {
        ticketFacade.modifyTicketOutstanding((ActemiumTicket) selectedTicket, solution, quality, supportNeeded);		
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

    public ObservableList<Employee> getAllTechnicians() {
        return ticketFacade.getAllTechnicians();
    }

}
