package domain;

import java.time.LocalDate;

import javafx.collections.ObservableList;

/**
 * The interface Customer.
 */
public interface Customer extends User {
	
	// getters

	/**
	 * Gets customer nr.
	 *
	 * @return the customer nr
	 */
	int getCustomerNr();	

	/**
	 * Gets email address.
	 *
	 * @return the email address
	 */
	String getEmailAddress();

	/**
	 * Give company company.
	 *
	 * @return the company
	 */
	Company giveCompany();

	/**
	 * Give contracts observable list.
	 *
	 * @return the observable list
	 */
	ObservableList<Contract> giveContracts();

	/**
	 * Gets registration date.
	 *
	 * @return the registration date
	 */
	LocalDate getRegistrationDate();

	/**
	 * Give seniority int.
	 *
	 * @return the int
	 */
	int giveSeniority();
	
}
