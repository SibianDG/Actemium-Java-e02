package domain.enums;

/**
 * The enum Ticket priority.
 */
public enum TicketPriority {
	/**
	 * The P 1.
	 */
// Standard notation for all companies
	// Priority and maximum handling time of a ticket is based on its type
	// P1: production impacted: solution needed within 2 hours
	P1,
	/**
	 * The P 2.
	 */
// P2: production will halt within due time: solution needed within 4 hours
	P2,
	/**
	 * The P 3.
	 */
// P3: no impact on production: solution needed within 3 days
	P3

}
