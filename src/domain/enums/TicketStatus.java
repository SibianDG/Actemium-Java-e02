package domain.enums;

import languages.LanguageResource;

import java.util.Arrays;
import java.util.List;

/**
 * The enum Ticket status.
 */
public enum TicketStatus {

	/**
	 * The Created.
	 */
// Ticket has just been created
	CREATED

	// A Technician has been assigned to the ticket
	,
	/**
	 * In progress ticket status.
	 */
	IN_PROGRESS

	// The support engineer/technician has added information to the ticket but needs
	// input of the client before it can be completed
	,
	/**
	 * Waiting on user information ticket status.
	 */
	WAITING_ON_USER_INFORMATION

	// The client has provided the information needed and the technician can proceed
	,
	/**
	 * User information received ticket status.
	 */
	USER_INFORMATION_RECEIVED

	// The ticket requires a code change before it can be completed
	,
	/**
	 * In development ticket status.
	 */
	IN_DEVELOPMENT

	// A solution for the ticket has been found
	,
	/**
	 * Completed ticket status.
	 */
	COMPLETED

	// The customer did not need any further support for this ticket
	// Ticket was removed* by the customer/support engineer
	// *Data is still kept in the database
	,
	/**
	 * Cancelled ticket status.
	 */
	CANCELLED

	;

	private static boolean outstanding;

	/**
	 * Is outstanding boolean.
	 *
	 * @return the boolean
	 */
	public static boolean isOutstanding() {
		return outstanding;
	}

	/**
	 * Sets outstanding.
	 *
	 * @param outstanding the outstanding
	 */
	public static void setOutstanding(boolean outstanding) {
		TicketStatus.outstanding = outstanding;
	}

	/**
	 * Gets outstanding ticket statuses.
	 *
	 * @return the outstanding ticket statuses
	 */
	public static List<TicketStatus> getOutstandingTicketStatuses() {
		return Arrays.asList(CREATED, IN_PROGRESS, WAITING_ON_USER_INFORMATION, USER_INFORMATION_RECEIVED, IN_DEVELOPMENT);
	}

	/**
	 * Gets resolved ticket statuses.
	 *
	 * @return the resolved ticket statuses
	 */
	public static List<TicketStatus> getResolvedTicketStatuses() {
		return Arrays.asList(COMPLETED, CANCELLED);
	}
}
