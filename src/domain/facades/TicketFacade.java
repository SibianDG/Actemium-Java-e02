package domain.facades;

import domain.*;
import domain.enums.TicketPriority;
import domain.enums.TicketType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

import java.util.List;

public class TicketFacade implements Facade {
	
	private GenericDao<ActemiumTicket> ticketRepo;

	private ObservableList<ActemiumTicket> tickets;
	
	public TicketFacade() {
		this.ticketRepo = new GenericDaoJpa<>(ActemiumTicket.class);
		fillTickets();
	}

	public void fillTickets() {
		List<ActemiumTicket> ticketList = ticketRepo.findAll();
		this.tickets = FXCollections.observableArrayList(ticketList);
	}

	public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
							   String remarks, String attachments, Customer customer) {
		ActemiumTicket ticket = new ActemiumTicket(priority, ticketType, title, description, customer, remarks, attachments);
		customer.addTicket(ticket);
		tickets.add(ticket);
	}

	public void modifyTicket(ActemiumTicket ticket, TicketPriority priority, String title, String description,
							 String remarks, String attachments, Customer customer, List<Employee> technicians) {
		int index = tickets.indexOf(ticket);

		ticket.setPriority(priority);
		ticket.setTitle(title);
		ticket.setDescription(description);
		ticket.setRemarks(remarks);
		ticket.setAttachments(attachments);
		ticket.setCustomer(customer);
		technicians.forEach(ticket::addTechnician);

		ticketRepo.startTransaction();
		ticketRepo.update(ticket);
		ticketRepo.commitTransaction();

		tickets.add(index, ticket);
		tickets.remove(index+1);
	}


	public ObservableList<ActemiumTicket> getTickets() {
		return FXCollections.unmodifiableObservableList(tickets);
	}
}
