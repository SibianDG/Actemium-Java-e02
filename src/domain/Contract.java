package domain;

import java.time.LocalDate;

import domain.enums.ContractStatus;

public interface Contract {
	
	public ContractType giveContractType();

	public ContractStatus getStatus();

	public LocalDate getStartDate();

	public LocalDate getEndDate();

}
