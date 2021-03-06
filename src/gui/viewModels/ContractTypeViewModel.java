package gui.viewModels;

import domain.*;
import domain.enums.*;
import domain.facades.ContractTypeFacade;
import domain.facades.TicketFacade;
import gui.GUIEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class ContractTypeViewModel extends ViewModel {

    private GUIEnum currentState;
    private ContractType selectedActemiumContractType;
    private final ContractTypeFacade contractTypeFacade;
    private ObservableList<ContractType> actemiumContractTypes;

    public ContractTypeViewModel(ContractTypeFacade contractTypeFacade) {
        super();
        this.contractTypeFacade = contractTypeFacade;
        this.actemiumContractTypes = FXCollections.observableArrayList();
        setCurrentState(GUIEnum.CONTRACTTYPE);
    }

    public ObservableList<ContractType> getActemiumContractTypes() {
        return FXCollections.unmodifiableObservableList(actemiumContractTypes);
    }

    public void setActemiumContractTypes(ObservableList<ContractType> observableList) {
        this.actemiumContractTypes = observableList;
    }

    public ContractType getSelectedActemiumContractTypes() {
        return selectedActemiumContractType;
    }

    public void setSelectedActemiumContractType(ContractType contractType) {
        this.selectedActemiumContractType = contractType;
        if (contractType != null){
            // substring(8) to remove ACTEMIUM
            //TODO obsolete
            setCurrentState(GUIEnum.valueOf(contractType.getClass().getSimpleName().substring(8).toUpperCase()));
        }
        fireInvalidationEvent();
    }

    public ArrayList<String> getDetailsNewContractType(){
        return new ArrayList<String>(Arrays.asList("Name", "Email", "Phone", "Application", "Timestamp ticket creation", "Max handling time", "Min throughput time contract", "Price contract"));
    }

    public Map<String, Object> getDetails() {
        ContractType contractType = selectedActemiumContractType;
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("Name", contractType.getName());
        details.put("Status", contractType.getContractTypeStatus());
        details.put("Email", contractType.isHasEmail());
        details.put("Phone", contractType.isHasPhone());
        details.put("Application", contractType.isHasApplication());
        details.put("Timestamp ticket creation", contractType.getTimestamp());
        details.put("Max handling time", contractType.getMaxHandlingTime());
        details.put("Min throughput time contract", contractType.getMinThroughputTime());
        details.put("Price contract", contractType.getPrice());
        //details.put("Amount open contracts", contractType);
        //details.put("Amount resolved tickets", contractType);
        //details.put("Percentage tickets resolved within agreed time", contractType);

        return details;
    }

    public void registerContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                     boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) {
        contractTypeFacade.registerContractType(name, contractTypeStatus, hasEmail, hasPhone, hasApplication, timestamp, maxHandlingTime, minThroughputTime, price);
        setSelectedActemiumContractType(contractTypeFacade.getLastAddedContractType());
    }

    public void modifyContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                   boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) {
        contractTypeFacade.modifyContractType((ActemiumContractType) selectedActemiumContractType,  name, contractTypeStatus, hasEmail, hasPhone, hasApplication, timestamp, maxHandlingTime, minThroughputTime,price );
    }

    public GUIEnum getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GUIEnum currentState) {
        this.currentState = currentState;
    }

    public String getNameSelectedActemiumContractType() {
        return selectedActemiumContractType.getName();
    }

}
