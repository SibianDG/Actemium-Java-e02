package domain.enums;

public enum TicketStatus {

	// Ticket has just been created
	CREATED,
	// A Technician has been assigned to the ticket
	IN_PROGRESS,
	// The support engineer/technician has added information to the ticket but needs
	// input of the client before it can be completed
	WAITING_ON_USER_INFORMATION,
	// The client has provided the information needed and the technician can proceed
	USER_INFORMATION_RECEIVED,
	// The ticket requires a code change before it can be completed
	IN_DEVELOPMENT,
	// A solution for the ticket has been found
	COMPLETED,
	// The customer did not need any further support for this ticket
	// Ticket was removed* by the customer/support engineer
	// *Data is still kept in the database
	CANCELLED

}
