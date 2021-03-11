package domain.facades;

import domain.ActemiumContract;
import domain.Contract;
import domain.enums.ContractStatus;
import domain.manager.Actemium;
import javafx.collections.ObservableList;

public class ContractFacade implements Facade {

    private Actemium actemium;

    public ContractFacade(Actemium actemium) {
        this.actemium = actemium;
    }

    public void modifyContract(ActemiumContract contract, ContractStatus status) {    	
    	contract.setStatus(status);
        actemium.modifyContract(contract);
    }

//    public void delete(ActemiumContractType contractType) {
//        contractType.setContractTypeStatus(ContractTypeStatus.INACTIVE);
//        actemium.modifyContractType(contractType);
//    }

    public ObservableList<Contract> giveActemiumContracts() {
        return actemium.giveActemiumContracts();
    }

//    public ContractType getLastAddedContractType() {
//        return actemium.getLastAddedContractType();
//    }

}
