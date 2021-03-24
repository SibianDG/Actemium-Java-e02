package domain;

import java.time.LocalDateTime;

/**
 * The interface Ticket comment.
 */
public interface TicketComment {

	/**
	 * Gets user.
	 *
	 * @return the user
	 */
	UserModel getUser();

	String getUserRole();

	/**
	 * Gets date time of comment.
	 *
	 * @return the date time of comment
	 */
	LocalDateTime getDateTimeOfComment();

	/**
	 * Gets comment text.
	 *
	 * @return the comment text
	 */
	String getCommentText();

}
