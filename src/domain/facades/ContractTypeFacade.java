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
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
		
        contractType.setName(name);
        contractType.setContractTypeStatus(contractTypeStatus);
        contractType.setHasEmail(hasEmail);
        contractType.setHasPhone(hasPhone);
        contractType.setHasApplication(hasApplication);
        contractType.setTimestamp(timestamp);
        contractType.setMaxHandlingTime(maxHandlingTime);
        contractType.setMinThroughputTime(minThroughputTime);
        contractType.setPrice(price);

        contractType.checkAttributes();

        actemium.modifyContractType(contractType);
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
