package domain.facades;

import java.time.LocalDate;

import domain.ActemiumContract;
import domain.ActemiumContractType;
import domain.ActemiumCustomer;
import domain.enums.ContractStatus;
import domain.enums.EmployeeRole;
import domain.manager.Actemium;
import exceptions.InformationRequiredException;
import languages.LanguageResource;

/**
 * The type Contract facade.
 */
public class ContractFacade extends Facade {

	/**
	 * Instantiates a new Contract facade.
	 *
	 * @param actemium the actemium
	 */
	public ContractFacade(Actemium actemium) {
        super(actemium);
    }

	/**
	 * Modify contract.
	 *
	 * @param contract the contract
	 * @param status   the status
	 * @throws InformationRequiredException the information required exception
	 */
	public void modifyContract(ActemiumContract contract, ContractStatus status) throws InformationRequiredException {
		try {
			ActemiumContract contractClone = contract.clone();
			// check to see if signed in user is Support Manager
			actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
			contractClone.setStatus(status);

			contractClone.checkAttributes();
			contract.setStatus(status);
			actemium.modifyContract(contract);
		} catch (CloneNotSupportedException e) {
			System.out.println(LanguageResource.getString("cannot_clone"));
		}

    }

	/**
	 * Register contract.
	 *
	 * @param customerId     the customer id
	 * @param contractTypeId the contract type id
	 * @param startDate      the start date
	 * @param duration       the duration
	 * @throws InformationRequiredException the information required exception
	 */
	public void registerContract(int customerId, int contractTypeId, LocalDate startDate,
			int duration) throws InformationRequiredException {
		// check to see if signed in user is Support Manager
		actemium.checkPermission(EmployeeRole.SUPPORT_MANAGER);
		ActemiumContractType contractType = (ActemiumContractType) actemium.findContractTypeById(contractTypeId);
		ActemiumCustomer customer = (ActemiumCustomer) actemium.findUserById(customerId);
		LocalDate endDate = startDate.plusYears(duration);
		ActemiumContract contract = new ActemiumContract.ContractBuilder()
				.contractType(contractType)
				.company(customer.getCompany())
				.startDate(startDate)
				.endDate(endDate)
				.build();

		actemium.registerContract(contract);
	}

	public void refreshContractData() {
		actemium.refreshContractData();
	}

}
