package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * The interface Ticket.
 */
public interface Ticket {

	/**
	 * Gets ticket id string.
	 *
	 * @return the ticket id string
	 */
	String getTicketIdString();

	/**
	 * Gets status as string.
	 *
	 * @return the status as string
	 */
	String getStatusAsString();

	/**
	 * Gets status.
	 *
	 * @return the status
	 */
	TicketStatus getStatus();

	/**
	 * Gets priority as string.
	 *
	 * @return the priority as string
	 */
	String getPriorityAsString();

	/**
	 * Gets priority.
	 *
	 * @return the priority
	 */
	TicketPriority getPriority();

	/**
	 * Gets ticket type as string.
	 *
	 * @return the ticket type as string
	 */
	String getTicketTypeAsString();

	/**
	 * Gets ticket type.
	 *
	 * @return the ticket type
	 */
	TicketType getTicketType();

	/**
	 * Gets date of creation.
	 *
	 * @return the date of creation
	 */
	LocalDate getDateOfCreation();

	/**
	 * Gets date and time of creation.
	 *
	 * @return the date and time of creation
	 */
	LocalDateTime getDateAndTimeOfCreation();

	/**
	 * Gets date and time of completion.
	 *
	 * @return the date and time of completion
	 */
	LocalDateTime getDateAndTimeOfCompletion();

	/**
	 * Gets title.
	 *
	 * @return the title
	 */
	String getTitle();

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	String getDescription();

	/**
	 * Give customer customer.
	 *
	 * @return the customer
	 */
	Customer giveCustomer();

	/**
	 * Give comments list.
	 *
	 * @return the list
	 */
	List<TicketComment> giveComments();

	/**
	 * Gets attachments.
	 *
	 * @return the attachments
	 */
	String getAttachments();

	/**
	 * Give technicians observable list.
	 *
	 * @return the observable list
	 */
	ObservableList<Employee> giveTechnicians();

	/**
	 * Gets solution.
	 *
	 * @return the solution
	 */
	String getSolution();

	/**
	 * Gets quality.
	 *
	 * @return the quality
	 */
	String getQuality();

	/**
	 * Gets support needed.
	 *
	 * @return the support needed
	 */
	String getSupportNeeded();

	/**
	 * Give ticket changes list.
	 *
	 * @return the list
	 */
	List<TicketChange> giveTicketChanges();

	/**
	 * Title property string property.
	 *
	 * @return the string property
	 */
	StringProperty titleProperty();

	/**
	 * Priority property string property.
	 *
	 * @return the string property
	 */
	StringProperty priorityProperty();

	/**
	 * Status property string property.
	 *
	 * @return the string property
	 */
	StringProperty statusProperty();

	/**
	 * Ticket id property integer property.
	 *
	 * @return the integer property
	 */
	IntegerProperty ticketIdProperty();

	/**
	 * Ticket type property string property.
	 *
	 * @return the string property
	 */
	StringProperty ticketTypeProperty();

	/**
	 * Completion date property string property.
	 *
	 * @return the string property
	 */
	StringProperty completionDateProperty();
	
}
