package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.ActemiumContract;
import domain.Contract;
import domain.enums.ContractStatus;
import domain.facades.ContractFacade;
import gui.GUIEnum;
import javafx.collections.ObservableList;

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
        if (contract != null){
            // substring(8) to remove ACTEMIUM
            setCurrentState(GUIEnum.valueOf(contract.getClass().getSimpleName().substring(8).toUpperCase()));
        }
        fireInvalidationEvent();
    }

	public ArrayList<String> getDetailsNewContract() {
		return new ArrayList<String>(Arrays.asList("Contract Nr", "Type", "Status", "Start date", "End date"));
	}

    public Map<String, Map<Boolean, Object>> getDetails() {
        Contract contract = selectedContract;
        Map<String,Map<Boolean, Object>> details = new LinkedHashMap<>();
        details.put("Contract Nr", Collections.singletonMap(false, contract.getContractNrString()));
        details.put("Type", Collections.singletonMap(true, contract.giveContractType().getName()));
        details.put("Status", Collections.singletonMap(true, contract.getStatusAsEnum()));
        details.put("Start date", Collections.singletonMap(true, contract.getStartDate().toString()));
        details.put("End date", Collections.singletonMap(true, contract.getEndDate().toString()));
        //details.put("Amount open contracts", contractType);
        //details.put("Amount resolved tickets", contractType);
        //details.put("Percentage tickets resolved within agreed time", contractType);

        return details;
    }

    public void modifyContract(ActemiumContract selectedContract, ContractStatus status) {
        contractFacade.modifyContract((ActemiumContract) selectedContract, status);
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }

	@Override
	public void delete() {
		// Unnecessary for this class but has to be implemented
	}

}
