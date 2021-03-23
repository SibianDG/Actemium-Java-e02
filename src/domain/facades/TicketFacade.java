package domain.facades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.ActemiumCustomer;
import domain.ActemiumEmployee;
import domain.ActemiumTicket;
import domain.ActemiumTicketChange;
import domain.ActemiumTicketComment;
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
	
	private final Actemium actemium;

	public TicketFacade(Actemium actemium) {
		this.actemium = actemium;
	}

	public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
							String commentText, String attachments, long customerId, List<ActemiumEmployee> techniciansAsignedToTicket) throws InformationRequiredException {
		// check to see if signed in user is Support Manger
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
		ActemiumCustomer customer = (ActemiumCustomer) actemium.findUserById(customerId);
		ActemiumTicket ticket = new ActemiumTicket.TicketBuilder()
							.title(title)
							.ticketPriority(priority)
							.ticketType(ticketType)
							.description(description)
							.customer(customer)
//							.comments(comments)
							.attachments(attachments)
							.build();
		ticket.setTechnicians(techniciansAsignedToTicket);

		ticket.addTicketComment(createTicketComment(ticket, commentText));
		
		List<String> changeList = new ArrayList<>();
		changeList.add(String.format("Ticket was added for customer: \"%s\".", ticket.getCustomer().getCompany().getName()));
		changeList.add(String.format("Ticket Priority was set to \"%s\".", priority));
		changeList.add(String.format("Ticket Type was set to \"%s\".", ticketType));
		changeList.add(String.format("Ticket Status was initialized as \"%s\".", ticket.getStatus()));
		changeList.add(String.format("Ticket Title was set to \"%s\".", title));
		changeList.add(String.format("Ticket Description was added."));
		if (!commentText.equals("(none)")) {
			changeList.add(String.format("Ticket Remark/Comment was added."));
		}
		if (!attachments.equals("(none)")) {
			changeList.add(String.format("Ticket Attachments were added."));
		}				

		// This counter intuitive way of doing things
		// is necessary to get the right ticket ID
		actemium.registerTicket(ticket, customer);		
		
		ticket = (ActemiumTicket) actemium.getLastAddedTicket();
		ticket.addTicketChange(createTicketChange(ticket, "registerTicket", changeList));
		
		actemium.modifyTicket(ticket);
	}

	public void modifyTicketOutstanding(ActemiumTicket ticket, TicketPriority priority, TicketType ticketType,
			TicketStatus status, String title, String description, String commentText, String attachments,
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
//			ticketClone.setComments(commentText);
			ticketClone.setAttachments(attachments);
			
			ticketClone.checkAttributes();

			List<String> changeList = new ArrayList<>();
			
			if (!ticket.getPriority().equals(priority)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from"), ticket.getPriority(), LanguageResource.getString("to"), priority));
				ticket.setPriority(priority);
			}
			if (!ticket.getTicketType().equals(ticketType)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from") , ticket.getTicketType(), LanguageResource.getString("to"), ticketType));
				ticket.setTicketType(ticketType);
			}
			if (!ticket.getStatus().equals(status)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from") , ticket.getStatus(), LanguageResource.getString("to"), status));
				ticket.setStatus(status);
			}
			if (!ticket.getTitle().equals(title)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from") , ticket.getTitle(), LanguageResource.getString("to"), title));
				ticket.setTitle(title);
			}
			if (!ticket.getDescription().equals(description)) {
				changeList.add(String.format("%s.", LanguageResource.getString("ticket_description_changed")));
				ticket.setDescription(description);
			}
			if (commentText == null || !(commentText.equals("(none)") || commentText.isBlank())) {
				changeList.add(String.format("%s.", LanguageResource.getString("ticket_comment_added")));
				ticket.addTicketComment(createTicketComment(ticket, commentText));
			}
			if (!ticket.getAttachments().equals(attachments)) {
				changeList.add(String.format("%s.", LanguageResource.getString("ticket_attachments_changed")));
				ticket.setAttachments(attachments);
			}
			// Had to stream the list in order for the removeAll to work properly
			if (!ticket.getTechnicians().equals(technicians)) {
				List<ActemiumEmployee> originalTechnicians = new ArrayList<>(ticket.getTechnicians());
				originalTechnicians.removeAll(technicians);
				if (originalTechnicians.size() != 0) {
					for (ActemiumEmployee technician : originalTechnicians) {
						changeList.add(String.format("%s \"%s %s\" %s: %d %s.", "Technician"/*(LanguageResource.getString("TECHNICIAN").substring(0,1).toUpperCase() + LanguageResource.getString("TECHNICIAN").substring(1).toLowerCase())*/,
								technician.getFirstName(), technician.getLastName(), LanguageResource.getString("with_id") ,technician.getUserId(), LanguageResource.getString("got_removed_from_the_ticket")));
					}
				}
				originalTechnicians = new ArrayList<>(ticket.getTechnicians());
				List<ActemiumEmployee> newTechnicians = new ArrayList<>(technicians);
				newTechnicians.removeAll(originalTechnicians);
				if (newTechnicians.size() != 0) {
					for (ActemiumEmployee technician : newTechnicians) {
						changeList.add(String.format("%s \"%s %s\" %s: %d %s.", "Technician"/*(LanguageResource.getString("TECHNICIAN").substring(0,1).toUpperCase() + LanguageResource.getString("TECHNICIAN").substring(1).toLowerCase())*/,
								technician.getFirstName(), technician.getLastName(), LanguageResource.getString("with_id") ,technician.getUserId(), LanguageResource.getString("got_added_to_the_ticket")));
					}
				}
				ticket.setTechnicians(new ArrayList<>());
				technicians.forEach(ticket::addTechnician);	
			}		
			
			ticket.addTicketChange(createTicketChange(ticket, "modifyTicketOutstanding", changeList));
			
			actemium.modifyTicket(ticket);
			
		} catch (CloneNotSupportedException e) {
			System.out.println(LanguageResource.getString("cannot_clone"));
		}
		
	}
	
	//TODO duplicate code
	public void modifyTicketOutstandingAsTechnician(ActemiumTicket ticket, TicketStatus status, 
					String commentText, String attachments) throws InformationRequiredException {
		try {
			ActemiumTicket ticketClone = ticket.clone();
			// check to see if signed in user is Support Manger
			actemium.checkPermision(EmployeeRole.TECHNICIAN);			
			
			ticketClone.setStatus(status);
			ticketClone.setAttachments(attachments);
			
			ticketClone.checkAttributes();

			List<String> changeList = new ArrayList<>();			
			
			if (!ticket.getStatus().equals(status)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from") , ticket.getStatus(), LanguageResource.getString("to"), status));
				ticket.setStatus(status);
			}			
			if (commentText == null || !(commentText.equals("(none)") || commentText.isBlank())) {
				changeList.add(String.format("%s.", LanguageResource.getString("ticket_comment_added")));
				ticket.addTicketComment(createTicketComment(ticket, commentText));
			}
			if (!ticket.getAttachments().equals(attachments)) {
				changeList.add(String.format("%s.", LanguageResource.getString("ticket_attachments_changed")));
				ticket.setAttachments(attachments);
			}
			
			ticket.addTicketChange(createTicketChange(ticket, "modifyTicketOutstanding", changeList));
			
			actemium.modifyTicket(ticket);
			
		} catch (CloneNotSupportedException e) {
			System.out.println(LanguageResource.getString("cannot_clone"));
		}
		
	}
	
	public void modifyTicketResolved(ActemiumTicket ticket, String solution, String quality, String supportNeeded)
			throws InformationRequiredException {
		try {
			ActemiumTicket ticketClone = ticket.clone();
			// check to see if signed in user is Support Manger
			actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER, EmployeeRole.TECHNICIAN);
			ticketClone.setSolution(solution);
			ticketClone.setQuality(quality);
			ticketClone.setSupportNeeded(supportNeeded);
			ticketClone.checkAttributes();
			
			List<String> changeList = new ArrayList<>();
			
			if (!ticket.getSolution().equals(solution)) {
				changeList.add(String.format("%s %s \"%s\" %s \"%s\".", LanguageResource.getString("ticket_solution"), LanguageResource.getString("changed_from"), ticket.getSolution(), LanguageResource.getString("to"), solution));
				ticket.setSolution(solution);
			}
			if (!ticket.getQuality().equals(quality)) {
				changeList.add(String.format("%s %s \"%s\" %s \"%s\".", LanguageResource.getString("ticket_quality"), LanguageResource.getString("changed_from"), ticket.getQuality(), LanguageResource.getString("to"), quality));
				ticket.setQuality(quality);
			}
			if (!ticket.getSupportNeeded().equals(supportNeeded)) {
				changeList.add(String.format("%s %s \"%s\" %s \"%s\".", LanguageResource.getString("ticket_support_needed"), LanguageResource.getString("changed_from"), ticket.getSupportNeeded(), LanguageResource.getString("to"), supportNeeded));
				ticket.setSupportNeeded(supportNeeded);
			}
			
			ticket.addTicketChange(createTicketChange(ticket, "modifyTicketResolved", changeList));
			
			actemium.modifyTicket(ticket);
			
		} catch (CloneNotSupportedException e) {
			System.out.println(LanguageResource.getString("cannot_clone"));
		}
	}
	
	private ActemiumTicketChange createTicketChange(ActemiumTicket ticket, String methodName, List<String> changeList) throws InformationRequiredException {
		StringBuilder changeDescription = new StringBuilder();
		switch(methodName) {
			case "registerTicket" -> changeDescription.append(String.format("%s: ", LanguageResource.getString("created_ticket_with_ID")));
			case "modifyTicketOutstanding" -> changeDescription.append(String.format("%s: ", LanguageResource.getString("modified_outstanding_ticket_with_ID")));
			case "modifyTicketResolved" -> changeDescription.append(String.format("%s: ", LanguageResource.getString("modified_resolved_ticket_with_ID")));
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
	
	private ActemiumTicketComment createTicketComment(ActemiumTicket ticket, String commentText) throws InformationRequiredException {				
		return new ActemiumTicketComment.TicketCommentBuilder()
				.ticket(ticket)
				.user(actemium.getSignedInUser())
				.userRole(actemium.giveUserRole())
				.dateTimeOfComment(LocalDateTime.now())
				.commentText(commentText)
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
	
	public ObservableList<Ticket> giveActemiumTicketsOutstandingAssignedToTechnician() {
		return actemium.giveActemiumTicketsOutstandingAssignedToTechnician();
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
				.filter(t -> t.getRole().equals(EmployeeRole.TECHNICIAN))
				.collect(Collectors.toList()));
	}

	public ObservableList<Ticket> giveTicketsOfSameType(KbItemType type) {
		ObservableList<Ticket> ticketsOfSameType;
		switch(type) {
		case HARDWARE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType().equals(TicketType.HARDWARE))
					.collect(Collectors.toList()));
		}
		case SOFTWARE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType().equals(TicketType.SOFTWARE))
					.collect(Collectors.toList()));
		}
		case INFRASTRUCTURE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType().equals(TicketType.INFRASTRUCTURE))
					.collect(Collectors.toList()));
		}
		case DATABASE -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType().equals(TicketType.DATABASE))
					.collect(Collectors.toList()));
		}
		case NETWORK -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType().equals(TicketType.NETWORK))
					.collect(Collectors.toList()));
		}
		default -> {
			ticketsOfSameType = FXCollections.observableArrayList(
					giveActemiumTicketsCompleted()
					.stream()
					.filter(t -> t.getTicketType().equals(TicketType.OTHER))
					.collect(Collectors.toList()));
		}
		}
		return ticketsOfSameType;
	}
}
