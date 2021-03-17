package domain.facades;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.Employee;
import domain.Ticket;
import domain.enums.EmployeeRole;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketFacade implements Facade {
	
	private Actemium actemium;

	public TicketFacade(Actemium actemium) {
		this.actemium = actemium;
	}

	public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
							   String remarks, String attachments, long customerId) throws InformationRequiredException {
		// check to see if signed in user is Support Manger
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
		ActemiumCustomer customer = (ActemiumCustomer) actemium.findUserById(customerId);
//		ActemiumTicket ticket = new ActemiumTicket(priority, ticketType, title, description, customer, remarks, attachments);
		ActemiumTicket ticket = new ActemiumTicket.TicketBuiler()
							.ticketPriority(priority)
							.ticketType(ticketType)
							.description(description)
							.customer(customer)
							.remarks(remarks)
							.attachments(attachments)
							.build();
		actemium.registerTicket(ticket, customer);
	}

	public void modifyTicket(ActemiumTicket ticket, TicketPriority priority, TicketType ticketType, TicketStatus status, String title, String description,
							 String remarks, String attachments, List<ActemiumEmployee> technicians) throws InformationRequiredException {
		try {
			ActemiumTicket ticketClone = ticket.clone();
			// check to see if signed in user is Support Manger
			actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);

			ticketClone.setPriority(priority);
			ticketClone.setTicketType(ticketType);
			ticketClone.setStatus(status);
			ticketClone.setTitle(title);
			ticketClone.setDescription(description);
			ticketClone.setRemarks(remarks);
			ticketClone.setAttachments(attachments);

			ticketClone.checkAttributes();

			ticket.setPriority(priority);
			ticket.setTicketType(ticketType);
			ticket.setStatus(status);
			ticket.setTitle(title);
			ticket.setDescription(description);
			ticket.setRemarks(remarks);
			ticket.setAttachments(attachments);
			ticket.setTechnicians(new ArrayList<>());
			technicians.forEach(ticket::addTechnician);

			actemium.modifyTicket(ticket);

		} catch (CloneNotSupportedException e) {
			System.out.println("Can't clone object");
		}

	}
	
	public void modifyTicketOutstanding(ActemiumTicket ticket, String solution, String quality, String supportNeeded) throws InformationRequiredException {
		try {
			ActemiumTicket ticketClone = ticket.clone();
			// check to see if signed in user is Support Manger
			actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
			ticketClone.setSolution(solution);
			ticketClone.setQuality(quality);
			ticketClone.setSupportNeeded(supportNeeded);
			ticketClone.checkAttributes();

			ticket.setSolution(solution);
			ticket.setQuality(quality);
			ticket.setSupportNeeded(supportNeeded);

			actemium.modifyTicket(ticket);

		} catch (CloneNotSupportedException e) {
			System.out.println("Can't clone object");
		}
	}
	
    public void delete(ActemiumTicket ticket) throws InformationRequiredException {
		// check to see if signed in user is Support Manger
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
		ticket.setStatus(TicketStatus.CANCELLED);
		actemium.modifyTicket(ticket);
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

	public ObservableList<Employee> getAllTechnicians() {
		return FXCollections.observableArrayList(actemium.giveActemiumEmployees()
				.stream()
				.filter(t -> t.getRoleAsEnum() == EmployeeRole.TECHNICIAN)
				.collect(Collectors.toList()));
	}
}
