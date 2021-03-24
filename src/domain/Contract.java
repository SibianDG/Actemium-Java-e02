package domain;

import java.time.LocalDate;

import domain.enums.ContractStatus;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * The interface Contract.
 */
public interface Contract {

	/**
	 * Gets contract id string.
	 *
	 * @return the contract id string
	 */
	String getContractIdString();

	/**
	 * Give contract type contract type.
	 *
	 * @return the contract type
	 */
	ContractType giveContractType();

	/**
	 * Give customer customer.
	 *
	 * @return the customer
	 */
	Customer giveCustomer();

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
	ContractStatus getStatus();

	LocalDate getStartDate();

	/**
	 * Gets end date.
	 *
	 * @return the end date
	 */
	LocalDate getEndDate();

	/**
	 * Contract id property integer property.
	 *
	 * @return the integer property
	 */
	IntegerProperty contractIdProperty();

	/**
	 * Contract type name property string property.
	 *
	 * @return the string property
	 */
	StringProperty contractTypeNameProperty();

	/**
	 * Contract status property string property.
	 *
	 * @return the string property
	 */
	StringProperty contractStatusProperty();

	/**
	 * Contract start date property string property.
	 *
	 * @return the string property
	 */
	StringProperty contractStartDateProperty();

	/**
	 * Contract end date property string property.
	 *
	 * @return the string property
	 */
	StringProperty contractEndDateProperty();

}
