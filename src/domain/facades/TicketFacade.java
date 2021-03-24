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

/**
 * The type Ticket facade.
 */
public class TicketFacade implements Facade {
	
	private final Actemium actemium;

	/**
	 * Instantiates a new Ticket facade.
	 *
	 * @param actemium the actemium
	 */
	public TicketFacade(Actemium actemium) {
		this.actemium = actemium;
	}

	/**
	 * Register ticket.
	 *
	 * @param priority                   the priority
	 * @param ticketType                 the ticket type
	 * @param title                      the title
	 * @param description                the description
	 * @param commentText                the comment text
	 * @param attachments                the attachments
	 * @param customerId                 the customer id
	 * @param techniciansAsignedToTicket the technicians asigned to ticket
	 * @throws InformationRequiredException the information required exception
	 */
	public void registerTicket(TicketPriority priority, TicketType ticketType, String title, String description,
							String commentText, String attachments, long customerId, List<ActemiumEmployee> techniciansAsignedToTicket) throws InformationRequiredException {
		// check to see if signed in user is Support Manger
		actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
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
		for (ActemiumEmployee technician : techniciansAsignedToTicket) {
			changeList.add(String.format("%s \"%s %s\" %s: %d %s.", "Technician"/*(LanguageResource.getString("TECHNICIAN").substring(0,1).toUpperCase() + LanguageResource.getString("TECHNICIAN").substring(1).toLowerCase())*/,
					technician.getFirstName(), technician.getLastName(), LanguageResource.getString("with_id") ,technician.getUserId(), LanguageResource.getString("got_removed_from_the_ticket")));
		}
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

	/**
	 * Modify ticket outstanding.
	 *
	 * @param ticket      the ticket
	 * @param priority    the priority
	 * @param ticketType  the ticket type
	 * @param status      the status
	 * @param title       the title
	 * @param description the description
	 * @param commentText the comment text
	 * @param attachments the attachments
	 * @param technicians the technicians
	 * @throws InformationRequiredException the information required exception
	 */
	public void modifyTicketOutstanding(ActemiumTicket ticket, TicketPriority priority, TicketType ticketType,
			TicketStatus status, String title, String description, String commentText, String attachments,
			List<ActemiumEmployee> technicians) throws InformationRequiredException {		
		EmployeeRole signedInEmployeeRole = ((Employee) actemium.getSignedInUser()).getRole();
		if(signedInEmployeeRole.equals(EmployeeRole.TECHNICIAN) || signedInEmployeeRole.equals(EmployeeRole.SUPPORT_MANAGER)) {		
		try {
			ActemiumTicket ticketClone = ticket.clone();			

			List<String> changeList = new ArrayList<>();

			// part only for support manager
			if(signedInEmployeeRole.equals(EmployeeRole.SUPPORT_MANAGER)) {
			
			ticketClone.setPriority(priority);
			ticketClone.setTicketType(ticketType);
			ticketClone.setStatus(status);
			ticketClone.setTitle(title);
			ticketClone.setDescription(description);
//			ticketClone.setComments(commentText);
			ticketClone.setAttachments(attachments);
			
			ticketClone.checkAttributes();
				
			if (!ticket.getPriority().equals(priority)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from"), ticket.getPriority(), LanguageResource.getString("to"), priority));
				ticket.setPriority(priority);
			}
			if (!ticket.getTicketType().equals(ticketType)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from") , ticket.getTicketType(), LanguageResource.getString("to"), ticketType));
				ticket.setTicketType(ticketType);
			}
			if (!ticket.getTitle().equals(title)) {
				changeList.add(String.format("%s \"%s\" %s \"%s\".",LanguageResource.getString("ticket_priority_changed_from") , ticket.getTitle(), LanguageResource.getString("to"), title));
				ticket.setTitle(title);
			}
			if (!ticket.getDescription().equals(description)) {
				changeList.add(String.format("%s.", LanguageResource.getString("ticket_description_changed")));
				ticket.setDescription(description);
			}
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
			}
			// common part for technician and support manager
			if(signedInEmployeeRole.equals(EmployeeRole.SUPPORT_MANAGER) || signedInEmployeeRole.equals(EmployeeRole.TECHNICIAN)) {	
			
			ticketClone.setStatus(status);
			ticketClone.setAttachments(attachments);			
			ticketClone.checkAttributes();			
			
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
			}
		} catch (CloneNotSupportedException e) {
			System.out.println(LanguageResource.getString("cannot_clone"));
		}
		}
	}

	/**
	 * Modify ticket resolved.
	 *
	 * @param ticket        the ticket
	 * @param solution      the solution
	 * @param quality       the quality
	 * @param supportNeeded the support needed
	 * @throws InformationRequiredException the information required exception
	 */
	public void modifyTicketResolved(ActemiumTicket ticket, String solution, String quality, String supportNeeded)
			throws InformationRequiredException {
		EmployeeRole signedInEmployeeRole = ((Employee) actemium.getSignedInUser()).getRole();
		if(signedInEmployeeRole.equals(EmployeeRole.TECHNICIAN) || signedInEmployeeRole.equals(EmployeeRole.SUPPORT_MANAGER)) {	
		try {
			ActemiumTicket ticketClone = ticket.clone();
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

	/**
	 * Delete.
	 *
	 * @param ticket the ticket
	 * @throws InformationRequiredException the information required exception
	 */
	public void delete(ActemiumTicket ticket) throws InformationRequiredException {
		// check to see if signed in user is Support Manger
		actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
		ticket.setStatus(TicketStatus.CANCELLED);
		actemium.modifyTicket(ticket);
    }

	/**
	 * Gets last added ticket.
	 *
	 * @return the last added ticket
	 */
	public Ticket getLastAddedTicket() {
		return actemium.getLastAddedTicket();
	}

	/**
	 * Give actemium tickets observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Ticket> giveActemiumTickets() {
		return actemium.giveActemiumTickets();
	}

	/**
	 * Give actemium tickets outstanding observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Ticket> giveActemiumTicketsOutstanding() {
		return actemium.giveActemiumTicketsOutstanding();
	}

	/**
	 * Give actemium tickets resolved observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Ticket> giveActemiumTicketsResolved() {
		return actemium.giveActemiumTicketsResolved();
	}

	/**
	 * Give actemium tickets completed observable list.
	 *
	 * @return the observable list
	 */
	public ObservableList<Ticket> giveActemiumTicketsCompleted() {
		return FXCollections.observableArrayList(
				actemium.giveActemiumTicketsResolved()
					.stream()
					.filter(t -> t.getStatus().equals(TicketStatus.COMPLETED))
					.collect(Collectors.toList()));
	}

	/**
	 * Gets all technicians.
	 *
	 * @return the all technicians
	 */
	public ObservableList<Employee> getAllTechnicians() {
		return FXCollections.observableArrayList(actemium.giveActemiumEmployees()
				.stream()
				.filter(t -> t.getRole().equals(EmployeeRole.TECHNICIAN))
				.collect(Collectors.toList()));
	}

	/**
	 * Give tickets of same type observable list.
	 *
	 * @param type the type
	 * @return the observable list
	 */
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
