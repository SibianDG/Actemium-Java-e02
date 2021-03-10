package domain;

import domain.enums.ContractStatus;
import languages.LanguageResource;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ActemiumContract implements Contract, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int contractNr;
	@ManyToOne
	private ActemiumContractType contractType;
	@Enumerated(EnumType.STRING)
	private ContractStatus status;
	private LocalDate startDate;
	private LocalDate endDate;

	public ActemiumContract() {
		super();
	}
	
	//TODO
	// Customer should be able to request a contract in the future
	// For example:
	// Current contract will expire next week
	// Customer can already request a contract that will start next week
	// This is added to the UC Sign New Contract - More Info Needed
	public ActemiumContract(ActemiumContractType contractType, LocalDate startDate, LocalDate endDate) {
		setContractType(contractType);
		setStartDate(startDate);
		if (startDate.isEqual(LocalDate.now())) {
			setStatus(ContractStatus.CURRENT);
		} else {
			setStatus(ContractStatus.IN_REQUEST);
		}
		setEndDate(endDate);
	}

	//TODO
	// Does anyone need to approve the contract after it has been signed by the customer?
	// In other words, can the contract be initialized with status CURRENT
	// or can it only be initialized with status IN_REQUEST ?
	public ActemiumContract(ActemiumContractType contractType, LocalDate endDate) {
		this(contractType, LocalDate.now(), endDate);		
	}

	public ActemiumContractType getContractType() {
		return contractType;
	}

	public void setContractType(ActemiumContractType contractType) {
		this.contractType = contractType;
	}

	public ContractStatus getStatus() {
		return status;
	}

	public void setStatus(ContractStatus status) {
		this.status = status;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		if(startDate.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException(LanguageResource.getString("startDate_invalid"));
		}
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		if(endDate.isBefore(startDate)) {
			throw new IllegalArgumentException(LanguageResource.getString("endDate_invalid1"));
		}
		if(!(endDate.minusYears(1).equals(startDate) 
				|| endDate.minusYears(2).equals(startDate) 
				|| endDate.minusYears(3).equals(startDate))) {
			throw new IllegalArgumentException(LanguageResource.getString("endDate_invalid2"));
		}
		this.endDate = endDate;
	}

}
