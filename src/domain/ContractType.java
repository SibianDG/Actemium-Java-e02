package domain;

import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import javafx.beans.property.StringProperty;

/**
 * The interface Contract type.
 */
public interface ContractType {
	
	// getters 

	/**
	 * Gets contract type id.
	 *
	 * @return the contract type id
	 */
	int getContractTypeId();

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Gets status.
	 *
	 * @return the status
	 */
	ContractTypeStatus getStatus();

	/**
	 * Gets status as string.
	 *
	 * @return the status as string
	 */
	String getStatusAsString();

	/**
	 * Has email boolean.
	 *
	 * @return the boolean
	 */
	boolean hasEmail();

	/**
	 * Has phone boolean.
	 *
	 * @return the boolean
	 */
	boolean hasPhone();

	/**
	 * Has application boolean.
	 *
	 * @return the boolean
	 */
	boolean hasApplication();

	/**
	 * Gets timestamp.
	 *
	 * @return the timestamp
	 */
	Timestamp getTimestamp();

	/**
	 * Gets timestamp as string.
	 *
	 * @return the timestamp as string
	 */
	String getTimestampAsString();

	/**
	 * Gets max handling time.
	 *
	 * @return the max handling time
	 */
	int getMaxHandlingTime();

	/**
	 * Gets min throughput time.
	 *
	 * @return the min throughput time
	 */
	int getMinThroughputTime();

	/**
	 * Gets price.
	 *
	 * @return the price
	 */
	double getPrice();

	/**
	 * Contract type name property string property.
	 *
	 * @return the string property
	 */
	StringProperty contractTypeNameProperty();

	/**
	 * Contract type status property string property.
	 *
	 * @return the string property
	 */
	StringProperty contractTypeStatusProperty();

	/**
	 * Contract typestamp property string property.
	 *
	 * @return the string property
	 */
	StringProperty contractTypestampProperty();
}
