package domain.facades;

import domain.ActemiumContractType;
import domain.ContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.EmployeeRole;
import domain.enums.Timestamp;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.ObservableList;

public class ContractTypeFacade implements Facade{

    private Actemium actemium;

    public ContractTypeFacade(Actemium actemium) {
        this.actemium = actemium;
    }

    public void registerContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                     boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
    	ActemiumContractType contractType = new ActemiumContractType.ContractTypeBuilder()
                .contractTypeName(name)
                .contractTypeStatus(contractTypeStatus)
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

    public void modifyContractType(ActemiumContractType contractType, String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                   boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {

        try {
            ActemiumContractType contractTypeClone = contractType.clone();

            // check to see if signed in user is Support Manager
            actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);

            contractTypeClone.setName(name);
            contractTypeClone.setContractTypeStatus(contractTypeStatus);
            contractTypeClone.setHasEmail(hasEmail);
            contractTypeClone.setHasPhone(hasPhone);
            contractTypeClone.setHasApplication(hasApplication);
            contractTypeClone.setTimestamp(timestamp);
            contractTypeClone.setMaxHandlingTime(maxHandlingTime);
            contractTypeClone.setMinThroughputTime(minThroughputTime);
            contractTypeClone.setPrice(price);

            contractTypeClone.checkAttributes();

            contractType.setName(contractType.getName());
            contractType.setContractTypeStatus(contractType.getContractTypeStatusAsEnum());
            contractType.setHasEmail(contractType.isHasEmail());
            contractType.setHasPhone(contractType.isHasEmail());
            contractType.setHasApplication(contractType.isHasApplication());
            contractType.setTimestamp(contractType.getTimestampAsEnum());
            contractType.setMaxHandlingTime(contractType.getMaxHandlingTime());
            contractType.setMinThroughputTime(contractType.getMinThroughputTime());
            contractType.setPrice(contractType.getPrice());

            actemium.modifyContractType(contractType);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    public void delete(ActemiumContractType contractType) {
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
    	contractType.setContractTypeStatus(ContractTypeStatus.INACTIVE);
        actemium.modifyContractType(contractType);
    }

    public ObservableList<ContractType> giveActemiumContractTypes() {
        return actemium.giveActemiumContractTypes();
    }

    public ContractType getLastAddedContractType() {
        return actemium.getLastAddedContractType();
    }

}
