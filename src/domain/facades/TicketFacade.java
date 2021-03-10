package domain.facades;

import java.util.List;

import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.Ticket;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.manager.Actemium;
import javafx.collections.ObservableList;

public class TicketFacade implements Facade {
	
	private Actemium actemium;
	
//	private GenericDao<ActemiumTicket> ticketRepo;

//	private ObservableList<Ticket> actemiumTickets;
	
//	public TicketFacade() {
//		this.ticketRepo = new GenericDaoJpa<>(ActemiumTicket.class);
//		fillTickets();
//	}

	public TicketFacade(Actemium actemium) {
		this.actemium = actemium;
	}

//	public void fillTickets() {
//		List<ActemiumTicket> ticketList = ticketRepo.findAll();
//		this.tickets = FXCollections.observableArrayList(ticketList);
//	}

	public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
							   String remarks, String attachments, long customerId) {
		ActemiumCustomer customer = (ActemiumCustomer) actemium.findById(customerId);
		ActemiumTicket ticket = new ActemiumTicket(priority, ticketType, title, description, customer, remarks, attachments);
		actemium.registerTicket(ticket, customer);
	}

	public void modifyTicket(ActemiumTicket ticket, TicketPriority priority, TicketType ticketType, TicketStatus status, String title, String description,
							 String remarks, String attachments, List<ActemiumEmployee> technicians) {
//		int index = tickets.indexOf(ticket);

		ticket.setPriority(priority);
		ticket.setTicketType(ticketType);
		ticket.setStatus(status);
		ticket.setTitle(title);
		ticket.setDescription(description);
		ticket.setRemarks(remarks);
		ticket.setAttachments(attachments);
//		ticket.setCustomer(customer);
		technicians.forEach(ticket::addTechnician);

		actemium.modifyTicket(ticket);
		
//		ticketRepo.startTransaction();
//		ticketRepo.update(ticket);
//		ticketRepo.commitTransaction();
//
//		tickets.add(index, ticket);
//		tickets.remove(index+1);
	}

	public Ticket getLastAddedTicket() {
		return actemium.getLastAddedTicket();
	}

	public ObservableList<Ticket> giveActemiumTickets() {
		return actemium.giveActemiumTickets();
	}
	
	public ObservableList<Ticket> giveActemiumTicketsResolved() {
		return actemium.giveActemiumTicketsResolved();
	}

	public ObservableList<Ticket> giveActemiumTicketsOutstanding() {
		return actemium.giveActemiumTicketsOutstanding();
	}


    public void delete(ActemiumTicket ticket) {
		ticket.setStatus(TicketStatus.CANCELLED);
		actemium.modifyTicket(ticket);
    }
}
