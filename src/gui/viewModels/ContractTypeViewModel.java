package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.ActemiumContractType;
import domain.ContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import domain.facades.ContractTypeFacade;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import javafx.collections.ObservableList;
import languages.LanguageResource;

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

    public void setSelectedContractType(ContractType contractType) {
        this.selectedContractType = contractType;
        if (contractType != null) {
            setCurrentState(GUIEnum.CONTRACTTYPE);
        }
        fireInvalidationEvent();
    }

	public ArrayList<String> getDetailsNewContractType() {
		return new ArrayList<String>(Arrays.asList(LanguageResource.getString("name"), LanguageResource.getString("status"), LanguageResource.getString("email"), LanguageResource.getString("phone"), LanguageResource.getString("application"),
				LanguageResource.getString("timestamp_sh"), LanguageResource.getString("max_handling_time"), LanguageResource.getString("min_throughput_time"), LanguageResource.getString("price_contract")));
	}

    public Map<String, Map<Boolean, Object>> getDetails() {
        ContractType contractType = selectedContractType;
        Map<String,Map<Boolean, Object>> details = new LinkedHashMap<>();
        details.put(LanguageResource.getString("ID"), Collections.singletonMap(true, String.valueOf(contractType.getContractTypeId())));
        details.put(LanguageResource.getString("name"), Collections.singletonMap(true, contractType.getName()));
        details.put(LanguageResource.getString("status"), Collections.singletonMap(true, contractType.getStatus()));
        details.put(LanguageResource.getString("email"), Collections.singletonMap(true, (Boolean) contractType.hasEmail()));
        details.put(LanguageResource.getString("phone"), Collections.singletonMap(true, (Boolean) contractType.hasPhone()));
        details.put(LanguageResource.getString("application"), Collections.singletonMap(true, (Boolean) contractType.hasApplication()));
        details.put(LanguageResource.getString("timestamp_sh"), Collections.singletonMap(true, contractType.getTimestamp()));
        details.put(LanguageResource.getString("max_handling_time"), Collections.singletonMap(true, String.format("%d", contractType.getMaxHandlingTime())));
        details.put(LanguageResource.getString("min_throughput_time"), Collections.singletonMap(true, String.format("%d", contractType.getMinThroughputTime())));
        details.put(LanguageResource.getString("price_contract"), Collections.singletonMap(true, String.format("%.2f", contractType.getPrice())));

        return details;
    }

    public void registerContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                     boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {
        contractTypeFacade.registerContractType(name, contractTypeStatus, hasEmail, hasPhone, hasApplication, timestamp, maxHandlingTime, minThroughputTime, price);
        setSelectedContractType(contractTypeFacade.getLastAddedContractType());
    }

    public void modifyContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
                                   boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) throws InformationRequiredException {
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

	public void refreshContractTypeData() {
		contractTypeFacade.refreshContractTypeData();
	}
}
