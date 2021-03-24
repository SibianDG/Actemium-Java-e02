package domain;

import domain.enums.UserStatus;
import javafx.beans.property.StringProperty;

/**
 * The interface User.
 */
public interface User {

	// getters
//	public List<LoginAttempt> getLoginAttempts();

	/**
	 * Gets user id.
	 *
	 * @return the user id
	 */
	long getUserId();

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	String getUsername();

	/**
	 * Gets password.
	 *
	 * @return the password
	 */
	String getPassword();

	/**
	 * Gets first name.
	 *
	 * @return the first name
	 */
	String getFirstName();

	/**
	 * Gets last name.
	 *
	 * @return the last name
	 */
	String getLastName();

	/**
	 * Gets failed login attempts.
	 *
	 * @return the failed login attempts
	 */
	int getFailedLoginAttempts();

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
	UserStatus getStatus();

	/**
	 * Username property string property.
	 *
	 * @return the string property
	 */
	StringProperty usernameProperty();

	/**
	 * Status property string property.
	 *
	 * @return the string property
	 */
	StringProperty statusProperty();

	/**
	 * First name property string property.
	 *
	 * @return the string property
	 */
	StringProperty firstNameProperty();

	/**
	 * Last name property string property.
	 *
	 * @return the string property
	 */
	StringProperty lastNameProperty();

}
