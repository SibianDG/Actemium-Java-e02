package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import domain.enums.ContractStatus;
import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
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

	public ActemiumContract(ContractBuilder builder){
		this.contractType = builder.contractType;
		this.status.set(String.valueOf(builder.status));
		this.contractTypeName.set(String.valueOf(builder.contractType));
		this.customer = builder.customer;
		this.startDate.set(String.valueOf(builder.startDate));
		this.endDate.set(String.valueOf(builder.startDate));
	}

	//TODO
	// Customer should be able to request a contract in the future
	// For example:
	// Current contract will expire next week
	// Customer can already request a contract that will start next week
	// This is added to the UC Sign New Contract - More Info Needed
	/*public ActemiumContract(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate startDate, LocalDate endDate) {
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
	 */

	//TODO
	// Does anyone need to approve the contract after it has been signed by the customer?
	// In other words, can the contract be initialized with status CURRENT
	// or can it only be initialized with status IN_REQUEST ?
	/*public ActemiumContract(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate endDate) {
		this(contractType, customer, LocalDate.now(), endDate);
	}*/

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
		//if (startDate.isBefore(LocalDate.now())) {
		//	throw new IllegalArgumentException(LanguageResource.getString("startDate_invalid"));
		//}
		this.startDate.set(String.valueOf(startDate));
	}

	public LocalDate getEndDate() {
		return LocalDate.parse(endDate.get());
	}

	public StringProperty contractEndDateProperty() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		//LocalDate starDate = getStartDate();
		//if (endDate.isBefore(starDate)) {
		//	throw new IllegalArgumentException(LanguageResource.getString("endDate_invalid1"));
		//}
		//if (!(endDate.minusYears(1).equals(starDate)
		//		|| endDate.minusYears(2).equals(starDate)
		//		|| endDate.minusYears(3).equals(starDate))) {
		//	throw new IllegalArgumentException(LanguageResource.getString("endDate_invalid2"));
		//}
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

	public void checkAttributes() throws InformationRequiredException {
		// Ms. Malfait her idea
		new ContractBuilder()
				.contractType(this.getContractType())
				.customer(this.getCustomer())
				.status(this.getStatusAsEnum())
				.startDate(this.getStartDate())
				.endDate(this.getEndDate())
				.build();
	}

	public static class ContractBuilder {
		private ActemiumContractType contractType;
		private ActemiumCustomer customer;
		private ContractStatus status;
		private LocalDate startDate;
		private LocalDate endDate;

		private Set<RequiredElement> requiredElements;

		public ContractBuilder contractType(ActemiumContractType contractType) {
			this.contractType = contractType;
			return this;
		}

		public ContractBuilder customer(ActemiumCustomer customer) {
			this.customer = customer;
			return this;
		}

		public ContractBuilder status(ContractStatus status) {
			this.status = status;
			return this;
		}

		public ContractBuilder startDate(LocalDate startDate) {
			this.startDate = startDate;
			return this;
		}

		public ContractBuilder endDate(LocalDate endDate) {
			this.endDate = endDate;
			return this;
		}

		public ActemiumContract build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesEmployeeBuiler();
			return new ActemiumContract(this);
		}

		private void checkAttributesEmployeeBuiler() throws InformationRequiredException {
			if (contractType == null)
				requiredElements.add(RequiredElement.ContractTypeRequired);
			if (customer == null)
				requiredElements.add(RequiredElement.CustomerRequired);
			if (startDate == null)
				startDate = LocalDate.now();
			if (startDate.isBefore(LocalDate.now()))
				requiredElements.add(RequiredElement.ContractStartDateRequired);
			if (endDate == null || endDate.isBefore(startDate))
				requiredElements.add(RequiredElement.ContractEndDateRequired1);
			if (!(endDate.minusYears(1).equals(startDate)
					|| endDate.minusYears(2).equals(startDate)
					|| endDate.minusYears(3).equals(startDate))) {
				requiredElements.add(RequiredElement.ContractEndDateRequired2);
			}

			if (startDate == null)
				startDate = LocalDate.now();
			if (startDate.equals(LocalDate.now())) {
				status = ContractStatus.CURRENT;
			} else {
				status = ContractStatus.IN_REQUEST;
			}

			if (!requiredElements.isEmpty()) {
				throw new InformationRequiredException(requiredElements);
			}
		}

	}
}
