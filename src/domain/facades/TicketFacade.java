package domain.facades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.ActemiumTicketChange;
import domain.Employee;
import domain.Ticket;
import domain.enums.EmployeeRole;
import domain.enums.KbItemType;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import languages.LanguageResource;

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
		ActemiumTicket ticket = new ActemiumTicket.TicketBuilder()
							.title(title)
							.ticketPriority(priority)
							.ticketType(ticketType)
							.description(description)
							.customer(customer)
							.remarks(remarks)
							.attachments(attachments)
							.build();

		StringBuilder changeList = new StringBuilder();
		changeList.append(System.lineSeparator());
		changeList.append(String.format("Ticket was added for customer: \"%s\".", ticket.getCustomer().getCompany().getName()));
		changeList.append(System.lineSeparator());
		changeList.append(String.format("Ticket Priority was set to \"%s\".", priority));
		changeList.append(System.lineSeparator());
		changeList.append(String.format("Ticket Type was set to \"%s\".", ticketType));
		changeList.append(System.lineSeparator());
		changeList.append(String.format("Ticket Status was initialized as \"%s\".", ticket.getStatus()));
		changeList.append(System.lineSeparator());
		changeList.append(String.format("Ticket Title was set to \"%s\".", title));
		changeList.append(System.lineSeparator());
		changeList.append(String.format("Ticket Description was added."));
		if (!remarks.equals("(none)")) {
			changeList.append(System.lineSeparator());
			changeList.append(String.format("Ticket Remarks were added."));
		}
		if (!attachments.equals("(none)")) {
			changeList.append(System.lineSeparator());
			changeList.append(String.format("Ticket Attachments were added."));
		}				

		// This counter intuitive way of doing things
		// is necessary to get the right ticket ID
		actemium.registerTicket(ticket, customer);		
		
		ticket = (ActemiumTicket) actemium.getLastAddedTicket();
		ticket.addTicketChange(createTicketChange(ticket, "registerTicket", changeList.toString()));
		
		actemium.modifyTicket(ticket);
	}

	// old method before TicketHistory
	// I will remove this method if everyone is ok with the new one
//	public void modifyTicketOutstanding(ActemiumTicket ticket, TicketPriority priority, TicketType ticketType,
//			TicketStatus status, String title, String description, String remarks, String attachments,
//			List<ActemiumEmployee> technicians) throws InformationRequiredException {
//		try {
//			ActemiumTicket ticketClone = ticket.clone();
//			// check to see if signed in user is Support Manger
//			actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
//
//			ticketClone.setPriority(priority);
//			ticketClone.setTicketType(ticketType);
//			ticketClone.setStatus(status);
//			ticketClone.setTitle(title);
//			ticketClone.setDescription(description);
//			ticketClone.setRemarks(remarks);
//			ticketClone.setAttachments(attachments);
//
//			ticketClone.checkAttributes();
//
//			StringBuilder changeList = new StringBuilder();
//			
//			ticket.setPriority(priority);
//			ticket.setTicketType(ticketType);
//			ticket.setStatus(status);
//			ticket.setTitle(title);
//			ticket.setDescription(description);
//			ticket.setRemarks(remarks);
//			ticket.setAttachments(attachments);
//			ticket.setTechnicians(new ArrayList<>());
//			technicians.forEach(ticket::addTechnician);			
//			
//			ticket.addTicketChange(createTicketChange(ticket, "modifyTicketOutstanding", changeList.toString()));
//
//			actemium.modifyTicket(ticket);
//
//		} catch (CloneNotSupportedException e) {
//			System.out.println(LanguageResource.getString("cannot_clone"));
//		}
//
//	}
	
	public void modifyTicketOutstanding(ActemiumTicket ticket, TicketPriority priority, TicketType ticketType,
			TicketStatus status, String title, String description, String remarks, String attachments,
			List<ActemiumEmployee> technicians) throws InformationRequiredException {
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
			
			StringBuilder changeList = new StringBuilder();
			
			if (!ticket.getPriority().equals(priority)) {
				changeList.append(System.lineSeparator());
				changeList.append(String.format("Ticket Priority changed from \"%s\" to \"%s\".", ticket.getPriority(), priority));
				ticket.setPriority(priority);
			}
			if (!ticket.getTicketType().equals(ticketType)) {
				changeList.append(System.lineSeparator());
				changeList.append(String.format("Ticket Type changed from \"%s\" to \"%s\".", ticket.getTicketType(), ticketType));
				ticket.setTicketType(ticketType);
			}
			if (!ticket.getStatus().equals(status)) {
				changeList.append(System.lineSeparator());
				changeList.append(String.format("Ticket Status changed from \"%s\" to \"%s\".", ticket.getStatus(), status));
				ticket.setStatus(status);
			}
			if (!ticket.getTitle().equals(title)) {
				changeList.append(System.lineSeparator());
				changeList.append(String.format("Ticket Title changed from \"%s\" to \"%s\".", ticket.getTitle(), title));
				ticket.setTitle(title);
			}
			if (!ticket.getDescription().equals(description)) {
				changeList.append(System.lineSeparator());
//				changeList.append(String.format("Ticket Description changed from \"%s\" to \"%s\".", ticket.getDescription(), description));
				changeList.append(String.format("Ticket Description changed."));
				ticket.setDescription(description);
			}
			if (!ticket.getRemarks().equals(remarks)) {
				changeList.append(System.lineSeparator());
//				changeList.append(String.format("Ticket Remarks changed from \"%s\" to \"%s\".", ticket.getRemarks(), remarks));
				changeList.append(String.format("Ticket Remarks changed."));
				ticket.setRemarks(remarks);
			}
			if (!ticket.getAttachments().equals(attachments)) {
				changeList.append(System.lineSeparator());
//				changeList.append(String.format("Ticket Attachments changed from \"%s\" to \"%s\".", ticket.getAttachments(), attachments));
				changeList.append(String.format("Ticket Attachments changed."));
				ticket.setAttachments(attachments);
			}
			// Had to stream the list in order for the removeAll to work properly
			if (!ticket.getTechnicians().equals(technicians)) {
				List<ActemiumEmployee> originalTechnicians = ticket.getTechnicians().stream().collect(Collectors.toList());
				System.out.println(originalTechnicians);
				originalTechnicians.removeAll(technicians);
				System.out.println(originalTechnicians);
				if (originalTechnicians.size() != 0) {
					for (ActemiumEmployee technician : originalTechnicians) {
						changeList.append(System.lineSeparator());
						changeList.append(String.format("Technician \"%s %s\" with ID: %d got removed from the ticket.", 
								technician.getFirstName(), technician.getLastName(), technician.getUserId()));
					}
				}
				originalTechnicians = ticket.getTechnicians().stream().collect(Collectors.toList());;
				System.out.println(originalTechnicians);
				List<ActemiumEmployee> newTechnicians = technicians.stream().collect(Collectors.toList());;
				System.out.println(newTechnicians);
				newTechnicians.removeAll(originalTechnicians);
				System.out.println(newTechnicians);
				if (newTechnicians.size() != 0) {
					for (ActemiumEmployee technician : newTechnicians) {
						changeList.append(System.lineSeparator());
						changeList.append(String.format("Technician \"%s %s\" with ID: %d got added to the ticket.", 
								technician.getFirstName(), technician.getLastName(), technician.getUserId()));
					}
				}
				ticket.setTechnicians(new ArrayList<>());
				technicians.forEach(ticket::addTechnician);	
			}		
			
			ticket.addTicketChange(createTicketChange(ticket, "modifyTicketOutstanding", changeList.toString()));
			
			actemium.modifyTicket(ticket);
			
		} catch (CloneNotSupportedException e) {
			System.out.println(LanguageResource.getString("cannot_clone"));
		}
		
	}

	// old method before TicketHistory
	// I will remove this method if everyone is ok with the new one
//	public void modifyTicketResolved(ActemiumTicket ticket, String solution, String quality, String supportNeeded)
//			throws InformationRequiredException {
//		try {
//			ActemiumTicket ticketClone = ticket.clone();
//			// check to see if signed in user is Support Manger
//			actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
//			ticketClone.setSolution(solution);
//			ticketClone.setQuality(quality);
//			ticketClone.setSupportNeeded(supportNeeded);
//			ticketClone.checkAttributes();
//
//			ticket.setSolution(solution);
//			ticket.setQuality(quality);
//			ticket.setSupportNeeded(supportNeeded);
//
//			ticket.addTicketChange(createTicketChange(ticket, "modifyTicketResolved"));
//			
//			actemium.modifyTicket(ticket);
//
//		} catch (CloneNotSupportedException e) {
//			System.out.println(LanguageResource.getString("cannot_clone"));
//		}
//	}
	public void modifyTicketResolved(ActemiumTicket ticket, String solution, String quality, String supportNeeded)
			throws InformationRequiredException {
		try {
			ActemiumTicket ticketClone = ticket.clone();
			// check to see if signed in user is Support Manger
			actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
			ticketClone.setSolution(solution);
			ticketClone.setQuality(quality);
			ticketClone.setSupportNeeded(supportNeeded);
			ticketClone.checkAttributes();
			
			StringBuilder changeList = new StringBuilder();
			
			if (!ticket.getSolution().equals(solution)) {
				changeList.append(System.lineSeparator());
				changeList.append(String.format("Ticket Solution changed from \"%s\" to \"%s\".", ticket.getSolution(), solution));
				ticket.setSolution(solution);
			}
			if (!ticket.getQuality().equals(quality)) {
				changeList.append(System.lineSeparator());
				changeList.append(String.format("Ticket Quality changed from \"%s\" to \"%s\".", ticket.getQuality(), quality));
				ticket.setQuality(quality);
			}
			if (!ticket.getSupportNeeded().equals(supportNeeded)) {
				changeList.append(System.lineSeparator());
				changeList.append(String.format("Ticket Support Needed changed from \"%s\" to \"%s\".", ticket.getSupportNeeded(), supportNeeded));
				ticket.setSupportNeeded(supportNeeded);
			}
			
			ticket.addTicketChange(createTicketChange(ticket, "modifyTicketResolved", changeList.toString()));
			
			actemium.modifyTicket(ticket);
			
		} catch (CloneNotSupportedException e) {
			System.out.println(LanguageResource.getString("cannot_clone"));
		}
	}
	
	private ActemiumTicketChange createTicketChange(ActemiumTicket ticket, String methodName, String changeList) throws InformationRequiredException {
		StringBuilder changeDescription = new StringBuilder();
		switch(methodName) {
		case "registerTicket" -> changeDescription.append("Created ticket with ID: ");
		case "modifyTicketOutstanding" -> changeDescription.append("Modified outstanding ticket with ID: ");
		case "modifyTicketResolved" -> changeDescription.append("Modified resolved ticket with ID: ");
		}
		changeDescription.append(ticket.getTicketIdString());
		
		return new ActemiumTicketChange.TicketChangeBuilder()
				.ticket(ticket)
				.user(actemium.getSignedInUser())
				.userRole(actemium.giveUserRole())
				.dateTimeOfChange(LocalDateTime.now())
				.changeDescription(changeDescription.toString())
				.changeContent(changeList)
				.build();
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
	
	public ObservableList<Ticket> giveActemiumTicketsOutstanding() {
		return actemium.giveActemiumTicketsOutstanding();
	}
	
	public ObservableList<Ticket> giveActemiumTicketsResolved() {
		return actemium.giveActemiumTicketsResolved();
	}
	
	public ObservableList<Ticket> giveActemiumTicketsCompleted() {
		return FXCollections.observableArrayList(
				actemium.giveActemiumTicketsResolved()
					.stream()
					.filter(t -> t.getStatus().equals(TicketStatus.COMPLETED))
					.collect(Collectors.toList()));
	}

	public ObservableList<Employee> getAllTechnicians() {
		return FXCollections.observableArrayList(actemium.giveActemiumEmployees()
				.stream()
				.filter(t -> t.getRole() == EmployeeRole.TECHNICIAN)
				.collect(Collectors.toList()));
	}

	public ObservableList<Ticket> giveTicketsOfSameType(KbItemType type) {
		ObservableList<Ticket> ticketsOfSameType;
		switch(type) {
		case HARDWARE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType() == TicketType.HARDWARE)
					.collect(Collectors.toList()));
		}
		case SOFTWARE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType() == TicketType.SOFTWARE)
					.collect(Collectors.toList()));
		}
		case INFRASTRUCTURE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType() == TicketType.INFRASTRUCTURE)
					.collect(Collectors.toList()));
		}
		case DATABASE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType() == TicketType.DATABASE)
					.collect(Collectors.toList()));
		}
		case NETWORK -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType() == TicketType.NETWORK)
					.collect(Collectors.toList()));
		}
		default -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType() == TicketType.OTHER)
					.collect(Collectors.toList()));
		}
		}
		return ticketsOfSameType;
	}
}
