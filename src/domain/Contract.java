package domain;

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
	}
	
	public Contract(int contractNr, ContractType contractType, ContractStatus status, LocalDate startDate,
			LocalDate endDate) {
		this.contractNr = contractNr;
		this.contractType = contractType;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
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
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
			
}
