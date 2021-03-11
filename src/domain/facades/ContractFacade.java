package domain.facades;

import java.time.LocalDate;

import domain.ActemiumContract;
import domain.ActemiumContractType;
import domain.ActemiumCustomer;
import domain.Contract;
import domain.enums.ContractStatus;
import domain.enums.EmployeeRole;
import domain.manager.Actemium;
import javafx.collections.ObservableList;

public class ContractFacade implements Facade {

    private Actemium actemium;

    public ContractFacade(Actemium actemium) {
        this.actemium = actemium;
    }

    public void modifyContract(ActemiumContract contract, ContractStatus status) {    	
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
    	contract.setStatus(status);
        actemium.modifyContract(contract);
    }
    
	public void registerContract(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate startDate,
			LocalDate endDate) {
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
		ActemiumContract contract = new ActemiumContract(contractType, customer, startDate, endDate);
		actemium.registerContract(contract);
	}

    public ObservableList<Contract> giveActemiumContracts() {
        return actemium.giveActemiumContracts();
    }

	public Contract getLastAddedContract() {
		return actemium.getLastAddedContract();
	}

}
