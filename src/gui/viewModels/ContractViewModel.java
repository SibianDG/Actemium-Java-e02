package gui.viewModels;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.ActemiumContract;
import domain.Contract;
import domain.enums.ContractStatus;
import domain.facades.ContractFacade;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import javafx.collections.ObservableList;
import languages.LanguageResource;

public class ContractViewModel extends ViewModel {

    private GUIEnum currentState;
    private Contract selectedContract;
    private final ContractFacade contractFacade;

    public ContractViewModel(ContractFacade contractFacade) {
        super();
        this.contractFacade = contractFacade;
        setCurrentState(GUIEnum.CONTRACT);
    }

    public ObservableList<Contract> giveContracts() {
        return contractFacade.giveActemiumContracts();
    }

    public Contract getSelectedContracts() {
        return selectedContract;
    }

    public void setSelectedContract(Contract contract) {
        this.selectedContract = contract;
        if (contract != null) {
            setCurrentState(GUIEnum.CONTRACT);
        }
        fireInvalidationEvent();
    }

	public ArrayList<String> getDetailsNewContract() {
		return new ArrayList<String>(Arrays.asList(LanguageResource.getString("customer_ID"), LanguageResource.getString("contract_type_name"), LanguageResource.getString("start_date"), LanguageResource.getString("duration_iy")));
	}

    public Map<String, Map<Boolean, Object>> getDetails() {
        Contract contract = selectedContract;
        Map<String,Map<Boolean, Object>> details = new LinkedHashMap<>();
        details.put(LanguageResource.getString("contract_ID"), Collections.singletonMap(false, contract.getContractIdString()));
        details.put(LanguageResource.getString("company"), Collections.singletonMap(false, contract.giveCustomer().giveCompany().getName()));
        details.put(LanguageResource.getString("type"), Collections.singletonMap(false, contract.giveContractType().getName()));
        details.put(LanguageResource.getString("status"), Collections.singletonMap(true, contract.getStatus()));
        details.put(LanguageResource.getString("start_date"), Collections.singletonMap(false, contract.getStartDate().toString()));
        details.put(LanguageResource.getString("end_date"), Collections.singletonMap(false, contract.getEndDate().toString()));

        return details;
    }

    public void modifyContract(ContractStatus status) throws InformationRequiredException {
        contractFacade.modifyContract((ActemiumContract) selectedContract, status);
    }
    
    public void registerContract(Long customer, long contractTypeId, LocalDate startDate, int duration) throws InformationRequiredException {
    	contractFacade.registerContract(customer, contractTypeId, startDate, duration);
    	setSelectedContract(contractFacade.getLastAddedContract());
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }
    
    public String getIdSelectedContract() {
        return selectedContract.getContractIdString();
    }

	@Override
	public void delete() {
		// Unnecessary for this class but has to be implemented
	}

}
