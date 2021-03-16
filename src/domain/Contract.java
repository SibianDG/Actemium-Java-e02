package domain;

import java.time.LocalDate;

import domain.enums.ContractStatus;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface Contract {
	
	public String getContractIdString();
//	public int getContractIdInt();
	
	public ContractType giveContractType();
	
	public Customer giveCustomer();
	
	public String getStatus();
	
	public ContractStatus getStatusAsEnum();

	public LocalDate getStartDate();

	public LocalDate getEndDate();
	
//	public StringProperty contractIdProperty();
	public IntegerProperty contractIdProperty();
	
	public StringProperty contractTypeNameProperty();
	
	public StringProperty contractStatusProperty();
	
}
