package domain.enums;

import languages.LanguageResource;

import java.util.Arrays;
import java.util.List;

public enum TicketStatus {

	// Ticket has just been created
	CREATED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("CREATED");
		}
	}*/
	// A Technician has been assigned to the ticket
	,IN_PROGRESS /*{
		@Override
		public String toString() {
			return LanguageResource.getString("IN_PROGRESS");
		}
	}*/
	// The support engineer/technician has added information to the ticket but needs
	// input of the client before it can be completed
	,WAITING_ON_USER_INFORMATION /*{
		@Override
		public String toString() {
			return LanguageResource.getString("WAITING_ON_USER_INFORMATION");
		}
	}*/
	// The client has provided the information needed and the technician can proceed
	,USER_INFORMATION_RECEIVED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("USER_INFORMATION_RECEIVED");
		}
	}*/
	// The ticket requires a code change before it can be completed
	,IN_DEVELOPMENT /*{
		@Override
		public String toString() {
			return LanguageResource.getString("IN_DEVELOPMENT");
		}
	}*/
	// A solution for the ticket has been found
	,COMPLETED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("COMPLETED");
		}
	}*/
	// The customer did not need any further support for this ticket
	// Ticket was removed* by the customer/support engineer
	// *Data is still kept in the database
	,CANCELLED /*{
		@Override
		public String toString() {
			return LanguageResource.getString("CANCELLED");
		}
	}*/;

	private static boolean outstanding;

	public static boolean isOutstanding() {
		return outstanding;
	}

	public static void setOutstanding(boolean outstanding) {
		TicketStatus.outstanding = outstanding;
	}

	public static List<TicketStatus> getOutstandingTicketStatuses() {
		return Arrays.asList(CREATED, IN_PROGRESS, WAITING_ON_USER_INFORMATION, USER_INFORMATION_RECEIVED, IN_DEVELOPMENT);
	}

	public static List<TicketStatus> getResolvedTicketStatuses() {
		return Arrays.asList(COMPLETED, CANCELLED);
	}
}
