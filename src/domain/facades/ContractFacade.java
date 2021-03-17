package domain.facades;

import java.time.LocalDate;

import domain.ActemiumContract;
import domain.ActemiumContractType;
import domain.ActemiumCustomer;
import domain.Contract;
import domain.enums.ContractStatus;
import domain.enums.EmployeeRole;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import javafx.collections.ObservableList;

public class ContractFacade implements Facade {

    private Actemium actemium;

    public ContractFacade(Actemium actemium) {
        this.actemium = actemium;
    }

    public void modifyContract(ActemiumContract contract, ContractStatus status) throws InformationRequiredException {
		try {
			System.out.println("STATUS: "+status);
			System.out.println("Start clone");
			ActemiumContract contractClone = contract.clone();
			System.out.println("End clone");
			System.out.println(contractClone);
			// check to see if signed in user is Support Manager
			actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
			contractClone.setStatus(status);

			System.out.println("----------------"+status);
			contractClone.checkAttributes();
			contract.setStatus(status);
			actemium.modifyContract(contract);
		} catch (CloneNotSupportedException e) {
			System.out.println("Can't clone");
		}

    }
    
	public void registerContract(Long customerId, String contractTypeName, LocalDate startDate,
			int duration) throws InformationRequiredException {
		// check to see if signed in user is Support Manager
		actemium.checkPermision(EmployeeRole.SUPPORT_MANAGER);
		ActemiumContractType contractType = (ActemiumContractType) actemium.findContractTypeById(contractTypeName);
		if (contractType == null) {
			throw new IllegalArgumentException("You must provide the name of an existing contractType");
		}
		ActemiumCustomer customer = (ActemiumCustomer) actemium.findUserById(customerId);
		if (customer == null) {
			throw new IllegalArgumentException("You must provide the customer ID of an existing customer");
		}
		LocalDate endDate = startDate.plusYears(duration);
		ActemiumContract contract = new ActemiumContract.ContractBuilder()
				.contractType(contractType)
				.customer(customer)
				.startDate(startDate)
				.endDate(endDate)
				.build();

		actemium.registerContract(contract);
	}

    public ObservableList<Contract> giveActemiumContracts() {
        return actemium.giveActemiumContracts();
    }

	public Contract getLastAddedContract() {
		return actemium.getLastAddedContract();
	}

}
