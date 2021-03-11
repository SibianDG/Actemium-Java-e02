package domain;

import java.time.LocalDate;

import domain.enums.ContractStatus;
import javafx.beans.property.StringProperty;

public interface Contract {
	
	public String getContractNrString();
	
	public ContractType giveContractType();
	
	public Customer giveCustomer();
	
	public String getStatus();
	
	public ContractStatus getStatusAsEnum();

	public LocalDate getStartDate();

	public LocalDate getEndDate();
	
	public StringProperty contractNrProperty();
	
	public StringProperty contractTypeNameProperty();
	
	public StringProperty contractStatusProperty();
	
}
