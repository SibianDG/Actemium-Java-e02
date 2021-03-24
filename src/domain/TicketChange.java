package domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Ticket change.
 */
public interface TicketChange {

	/**
	 * Gets user.
	 *
	 * @return the user
	 */
	UserModel getUser();

	String getUserRole();

	/**
	 * Gets date time of change.
	 *
	 * @return the date time of change
	 */
	LocalDateTime getDateTimeOfChange();

	/**
	 * Gets change description.
	 *
	 * @return the change description
	 */
	String getChangeDescription();

	/**
	 * Gets change content.
	 *
	 * @return the change content
	 */
	List<String> getChangeContent();

}
