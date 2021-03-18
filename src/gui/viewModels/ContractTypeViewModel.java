package gui.viewModels;

import java.util.*;

import domain.ActemiumContractType;
import domain.ContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import domain.facades.ContractTypeFacade;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import javafx.collections.ObservableList;

public class ContractTypeViewModel extends ViewModel {

    private GUIEnum currentState;
    private ContractType selectedContractType;
    private final ContractTypeFacade contractTypeFacade;

    public ContractTypeViewModel(ContractTypeFacade contractTypeFacade) {
        super();
        this.contractTypeFacade = contractTypeFacade;
        setCurrentState(GUIEnum.CONTRACTTYPE);
    }

    public ObservableList<ContractType> giveContractTypes() {
        return contractTypeFacade.giveActemiumContractTypes();
    }

    public ContractType getSelectedContractTypes() {
        return selectedContractType;
    }

    public void setSelectedContractType(ContractType contractType) {
        this.selectedContractType = contractType;
        if (contractType != null) {
            setCurrentState(GUIEnum.CONTRACTTYPE);
        }
        fireInvalidationEvent();
    }

	public ArrayList<String> getDetailsNewContractType() {
		return new ArrayList<String>(Arrays.asList("Name", "Status", "Email", "Phone", "Application",
				"Timestamp (support hours)", "Max handling time", "Min throughput time contract", "Price contract"));
	}

    public Map<String, Map<Boolean, Object>> getDetails() {
        ContractType contractType = selectedContractType;
        Map<String,Map<Boolean, Object>> details = new LinkedHashMap<>();
        details.put("Name", Collections.singletonMap(false, contractType.getName()));
        details.put("Status", Collections.singletonMap(true, contractType.getContractTypeStatus()));
        details.put("Email", Collections.singletonMap(true, (Boolean) contractType.isHasEmail()));
        details.put("Phone", Collections.singletonMap(true, (Boolean) contractType.isHasPhone()));
        details.put("Application", Collections.singletonMap(true, (Boolean) contractType.isHasApplication()));
        details.put("Timestamp (support hours)", Collections.singletonMap(true, contractType.getTimestamp()));
        details.put("Max handling time", Collections.singletonMap(true, String.format("%d", contractType.getMaxHandlingTime())));
        details.put("Min throughput time contract", Collections.singletonMap(true, String.format("%d", contractType.getMinThroughputTime())));
        details.put("Price contract", Collections.singletonMap(true, String.format("%.2f", contractType.getPrice())));
        //details.put("Amount open contracts", contractType);
        //details.put("Amount resolved tickets", contractType);
        //details.put("Percentage tickets resolved within agreed time", contractType);

        return details;
    }

    public void registerContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                     boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {
        contractTypeFacade.registerContractType(name, contractTypeStatus, hasEmail, hasPhone, hasApplication, timestamp, maxHandlingTime, minThroughputTime, price);
        setSelectedContractType(contractTypeFacade.getLastAddedContractType());
    }

    public void modifyContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                   boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {
        //ActemiumContractType contractType, String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
        //                                   boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) {
        contractTypeFacade.modifyContractType((ActemiumContractType) selectedContractType, name, contractTypeStatus, hasEmail, hasPhone, hasApplication, timestamp, maxHandlingTime, minThroughputTime, price );
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }

    public String getNameSelectedContractType() {
        return selectedContractType.getName();
    }

    @Override
    public void delete() {
        contractTypeFacade.delete((ActemiumContractType) selectedContractType);
    }
}
