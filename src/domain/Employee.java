package domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import domain.enums.EmployeeRole;
import domain.enums.TicketType;
import javafx.beans.property.StringProperty;

/**
 * The interface Employee.
 */
public interface Employee extends User {

	// getters

	/**
	 * Give seniority int.
	 *
	 * @return the int
	 */
	int giveSeniority();

	/**
	 * Gets employee nr.
	 *
	 * @return the employee nr
	 */
	int getEmployeeNr();

	/**
	 * Gets address.
	 *
	 * @return the address
	 */
	String getAddress();

	/**
	 * Gets email address.
	 *
	 * @return the email address
	 */
	String getEmailAddress();

	/**
	 * Gets phone number.
	 *
	 * @return the phone number
	 */
	String getPhoneNumber();

	/**
	 * Gets registration date.
	 *
	 * @return the registration date
	 */
	LocalDate getRegistrationDate();

	/**
	 * Gets role as string.
	 *
	 * @return the role as string
	 */
	String getRoleAsString();

	/**
	 * Gets role.
	 *
	 * @return the role
	 */
	EmployeeRole getRole();

//	List<Ticket> giveTickets();

	/**
	 * Role property string property.
	 *
	 * @return the string property
	 */
	StringProperty roleProperty();

	/**
	 * Gets specialties.
	 *
	 * @return the specialties
	 */
	Set<TicketType> getSpecialties();

}
