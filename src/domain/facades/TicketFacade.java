package domain.facades;

import java.util.List;

import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.enums.TicketType;
import domain.manager.Actemium;

public class TicketFacade implements Facade {
	
	private Actemium actemium;
	
//	private GenericDao<ActemiumTicket> ticketRepo;

//	private ObservableList<ActemiumTicket> tickets;
	
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
							   String remarks, String attachments, ActemiumCustomer customer) {
		ActemiumTicket ticket = new ActemiumTicket(priority, ticketType, title, description, customer, remarks, attachments);
		actemium.registerTicket(ticket, customer);
	}

	public void modifyTicket(ActemiumTicket ticket, TicketPriority priority, TicketType ticketType, String title, String description,
							 String remarks, String attachments, ActemiumCustomer customer, List<ActemiumEmployee> technicians) {
//		int index = tickets.indexOf(ticket);

		ticket.setPriority(priority);
		ticket.setTicketType(ticketType);
		ticket.setTitle(title);
		ticket.setDescription(description);
		ticket.setRemarks(remarks);
		ticket.setAttachments(attachments);
		ticket.setCustomer(customer);
		technicians.forEach(ticket::addTechnician);

		actemium.modifyTicket(ticket);
		
//		ticketRepo.startTransaction();
//		ticketRepo.update(ticket);
//		ticketRepo.commitTransaction();
//
//		tickets.add(index, ticket);
//		tickets.remove(index+1);
	}


//	public ObservableList<ActemiumTicket> getTickets() {
//		return FXCollections.unmodifiableObservableList(tickets);
//	}
	
}
