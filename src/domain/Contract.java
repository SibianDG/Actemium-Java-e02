package domain;

import java.time.LocalDate;

import domain.enums.ContractStatus;

public interface Contract {
	
	public ActemiumContractType getContractType();

	public ContractStatus getStatus();

	public LocalDate getStartDate();

	public LocalDate getEndDate();

}
