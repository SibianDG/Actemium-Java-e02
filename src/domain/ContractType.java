package domain;

import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import javafx.beans.property.StringProperty;

public interface ContractType {
	
	// getters 
	
	public String getName();

	public ContractTypeStatus getContractTypeStatus();

	public boolean isHasEmail();

	public boolean isHasPhone();

	public boolean isHasApplication();

	public Timestamp getTimestamp();

	public int getMaxHandlingTime();

	public int getMinThroughputTime();

	public double getPrice();

	public StringProperty getContractTypeStatusProperty();

	public StringProperty getContractTypeNameString();
}
