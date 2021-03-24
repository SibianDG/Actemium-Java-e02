package domain.facades;

import domain.ActemiumContractType;
import domain.ContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.EmployeeRole;
import domain.enums.Timestamp;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.ObservableList;

/**
 * The type Contract type facade.
 */
public class ContractTypeFacade implements Facade{

    private final Actemium actemium;

    /**
     * Instantiates a new Contract type facade.
     *
     * @param actemium the actemium
     */
    public ContractTypeFacade(Actemium actemium) {
        this.actemium = actemium;
    }

    /**
     * Register contract type.
     *
     * @param name               the name
     * @param contractTypeStatus the contract type status
     * @param hasEmail           the has email
     * @param hasPhone           the has phone
     * @param hasApplication     the has application
     * @param timestamp          the timestamp
     * @param maxHandlingTime    the max handling time
     * @param minThroughputTime  the min throughput time
     * @param price              the price
     * @throws InformationRequiredException the information required exception
     */
    public void registerContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                     boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {
		// check to see if signed in user is Support Manager
		actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
    	ActemiumContractType contractType = new ActemiumContractType.ContractTypeBuilder()
    			.name(name)
                .status(contractTypeStatus)
                .hasEmail(hasEmail)
                .hasPhone(hasPhone)
                .hasApplication(hasApplication)
                .timestamp(timestamp)
                .maxHandlingTime(maxHandlingTime)
                .minThroughputTime(minThroughputTime)
                .price(price)
                .build();
        actemium.registerContractType(contractType);
    }

    /**
     * Modify contract type.
     *
     * @param contractType      the contract type
     * @param name              the name
     * @param status            the status
     * @param hasEmail          the has email
     * @param hasPhone          the has phone
     * @param hasApplication    the has application
     * @param timestamp         the timestamp
     * @param maxHandlingTime   the max handling time
     * @param minThroughputTime the min throughput time
     * @param price             the price
     * @throws InformationRequiredException the information required exception
     */
    public void modifyContractType(ActemiumContractType contractType, String name, ContractTypeStatus status, boolean hasEmail, boolean hasPhone,
                                   boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {

        try {
            ActemiumContractType contractTypeClone = contractType.clone();

            // check to see if signed in user is Support Manager
            actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);

            contractTypeClone.setName(name);
            contractTypeClone.setStatus(status);
            contractTypeClone.setHasEmail(hasEmail);
            contractTypeClone.setHasPhone(hasPhone);
            contractTypeClone.setHasApplication(hasApplication);
            contractTypeClone.setTimestamp(timestamp);
            contractTypeClone.setMaxHandlingTime(maxHandlingTime);
            contractTypeClone.setMinThroughputTime(minThroughputTime);
            contractTypeClone.setPrice(price);

            contractTypeClone.checkAttributes();

            contractType.setName(name);
            contractType.setStatus(status);
            contractType.setHasEmail(hasEmail);
            contractType.setHasPhone(hasPhone);
            contractType.setHasApplication(hasApplication);
            contractType.setTimestamp(timestamp);
            contractType.setMaxHandlingTime(maxHandlingTime);
            contractType.setMinThroughputTime(minThroughputTime);
            contractType.setPrice(price);

            actemium.modifyContractType(contractType);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete.
     *
     * @param contractType the contract type
     */
    public void delete(ActemiumContractType contractType) {
		// check to see if signed in user is Support Manager
		actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
    	contractType.setStatus(ContractTypeStatus.INACTIVE);
        actemium.modifyContractType(contractType);
    }

    /**
     * Give actemium contract types observable list.
     *
     * @return the observable list
     */
    public ObservableList<ContractType> giveActemiumContractTypes() {
        return actemium.giveActemiumContractTypes();
    }

    /**
     * Gets last added contract type.
     *
     * @return the last added contract type
     */
    public ContractType getLastAddedContractType() {
        return actemium.getLastAddedContractType();
    }

}
