package domain;

import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import javafx.beans.property.StringProperty;

public interface ContractType {
	
	// getters 
	
	public String getName();

	public ContractTypeStatus getContractTypeStatusAsEnum();
	
	public String getContractTypeStatus();

	public boolean isHasEmail();

	public boolean isHasPhone();

	public boolean isHasApplication();

	public Timestamp getTimestampAsEnum();
	
	public String getTimestamp();

	public int getMaxHandlingTime();

	public int getMinThroughputTime();

	public double getPrice();

	public StringProperty contractTypeNameProperty();
	
	public StringProperty contractTypeStatusProperty();

	public StringProperty contractTypestampProperty();
}
