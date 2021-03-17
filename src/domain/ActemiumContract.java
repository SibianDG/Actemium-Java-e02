package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import domain.enums.ContractStatus;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import languages.LanguageResource;

@Entity
public class ActemiumContract implements Contract, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long contractId;

	@Transient
	private IntegerProperty contractIdInt = new SimpleIntegerProperty();
//	@Transient
//	private StringProperty contractIdString = new SimpleStringProperty();

	@ManyToOne(cascade = CascadeType.PERSIST)
	private ActemiumContractType contractType;
	@Transient
	private StringProperty contractTypeName = new SimpleStringProperty();

	@ManyToOne
	ActemiumCustomer customer;

	@Transient
	private StringProperty status = new SimpleStringProperty();

	@Transient
	private StringProperty startDate = new SimpleStringProperty();
	@Transient
	private StringProperty endDate = new SimpleStringProperty();
	//private LocalDate startDate;
	//private LocalDate endDate;

	public ActemiumContract() {
		super();
	}

	//TODO
	// Customer should be able to request a contract in the future
	// For example:
	// Current contract will expire next week
	// Customer can already request a contract that will start next week
	// This is added to the UC Sign New Contract - More Info Needed
	public ActemiumContract(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate startDate, LocalDate endDate) {
		setContractType(contractType);
		setCustomer(customer);
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
	public ActemiumContract(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate endDate) {
		this(contractType, customer, LocalDate.now(), endDate);
	}

	public String getContractIdString() {
		return String.valueOf(contractId);
	}

	public int getContractIdInt() {
		return (int) contractId;
	}

	public void setContractIdInt() {
		this.contractIdInt.set((int) contractId);
	}

	public ActemiumContractType getContractType() {
		return contractType;
	}

	public ContractType giveContractType() {
		return (ContractType) contractType;
	}

	public void setContractType(ActemiumContractType contractType) {
		this.contractType = contractType;
	}

	public void setContractTypeName() {
		this.contractTypeName.set(String.valueOf(contractType.getName()));
	}

	public ActemiumCustomer getCustomer() {
		return customer;
	}

	public Customer giveCustomer() {
		return (Customer) customer;
	}

	public void setCustomer(ActemiumCustomer customer) {
		this.customer = customer;
	}

	public String getStatus() {
		return status.get();
	}

	public ContractStatus getStatusAsEnum() {
		return ContractStatus.valueOf(status.get());
	}

	public void setStatus(ContractStatus status) {
		this.status.set(String.valueOf(status));
	}

	public LocalDate getStartDate() {
		return LocalDate.parse(startDate.get());
	}

	public StringProperty contractStartDateProperty() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		if (startDate.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException(LanguageResource.getString("startDate_invalid"));
		}
		this.startDate.set(String.valueOf(startDate));
	}

	public LocalDate getEndDate() {
		return LocalDate.parse(endDate.get());
	}

	public StringProperty contractEndDateProperty() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		LocalDate starDate = getStartDate();

		if (endDate.isBefore(starDate)) {
			throw new IllegalArgumentException(LanguageResource.getString("endDate_invalid1"));
		}
		if (!(endDate.minusYears(1).equals(starDate)
				|| endDate.minusYears(2).equals(starDate)
				|| endDate.minusYears(3).equals(starDate))) {
			throw new IllegalArgumentException(LanguageResource.getString("endDate_invalid2"));
		}
		this.endDate.set(String.valueOf(endDate));
	}

	@Override
	public IntegerProperty contractIdProperty() {
		setContractIdInt();
		return contractIdInt;
	}

	@Override
	public StringProperty contractTypeNameProperty() {
		setContractTypeName();
		return contractTypeName;
	}

	@Override
	public StringProperty contractStatusProperty() {
		return status;
	}

	@Override
	public String toString() {
		return String.format("%s: %s %s %s until %s", this.getContractIdString(), this.contractType.getName(), this.getStatus(), this.getStartDate().toString(), this.getEndDate().toString());
	}
}

//	public static class ContractBuilder {
//
//	}
