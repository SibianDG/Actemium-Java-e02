package domain;

import domain.enums.ContractStatus;

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
public class Contract implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int contractNr;
	@ManyToOne
	private ContractType contractType;
	@Enumerated(EnumType.STRING)
	private ContractStatus status;
	private LocalDate startDate;
	private LocalDate endDate;

	public Contract() {
		super();
	}
	
	//TODO
	// Customer should be able to request a contract in the future
	// For example:
	// Current contract will expire next week
	// Customer can already request a contract that will start next week
	// This is added to the UC Sign New Contract - More Info Needed
	public Contract(ContractType contractType, LocalDate startDate, LocalDate endDate) {
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
	public Contract(ContractType contractType, LocalDate endDate) {
		this(contractType, LocalDate.now(), endDate);		
	}

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
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
			throw new IllegalArgumentException("StartDate must be today or in the future.");
		}
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		if(endDate.isBefore(startDate)) {
			throw new IllegalArgumentException("EndDate needs to be at least 1 year after startDate.");
		}
		if(!(endDate.minusYears(1).equals(startDate) 
				|| endDate.minusYears(2).equals(startDate) 
				|| endDate.minusYears(3).equals(startDate))) {
			throw new IllegalArgumentException("EndDate needs to be exactly 1, 2 or 3 years after startDate.");
		}
		this.endDate = endDate;
	}

}
