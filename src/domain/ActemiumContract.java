package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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


/**
 * The type Actemium contract.
 */
@Entity
@Access(AccessType.FIELD)
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

	/**
	 * The Customer with the contract.
	 */
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

	/**
	 * Instantiates a new Actemium contract.
	 */
	public ActemiumContract() {
		super();
	}

	/**
	 * Instantiates a new Actemium contract.
	 *
	 * @param builder the builder
	 */
	public ActemiumContract(ContractBuilder builder){
		this.contractType = builder.contractType;
		this.status.set(String.valueOf(builder.status));
		this.contractTypeName.set(String.valueOf(builder.contractType));
		this.customer = builder.customer;
		this.startDate.set(String.valueOf(builder.startDate));
		this.endDate.set(String.valueOf(builder.endDate));
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

	/**
	 * Gets contract Id.
	 *
	 * @return contract Id
	 */
	public String getContractIdString() {
		return String.valueOf(contractId);
	}

	/**
	 * Gets contract id int.
	 *
	 * @return the contract id int
	 */
	public int getContractIdInt() {
		return (int) contractId;
	}

	/**
	 * Sets contract id int.
	 */
	public void setContractIdInt() {
		this.contractIdInt.set((int) contractId);
	}

	/**
	 * Gets contract type.
	 *
	 * @return the contract type
	 */
	public ActemiumContractType getContractType() {
		return contractType;
	}

	/**
	 * gets contract type.
	 *
	 * @return contract type
	 */
	public ContractType giveContractType() {
		return (ContractType) contractType;
	}

	/**
	 * Sets contract type.
	 *
	 * @param contractType the contract type
	 */
	public void setContractType(ActemiumContractType contractType) {
		this.contractType = contractType;
	}

	/**
	 * Sets contract type name.
	 */
	public void setContractTypeName() {
		this.contractTypeName.set(String.valueOf(contractType.getName()));
	}

	/**
	 * Gets customer.
	 *
	 * @return the customer
	 */
	public ActemiumCustomer getCustomer() {
		return customer;
	}

	/**
	 * Gives customer.
	 *
	 * @return customer
	 */
	public Customer giveCustomer() {
		return (Customer) customer;
	}

	/**
	 * Sets customer.
	 *
	 * @param customer the customer
	 */
	public void setCustomer(ActemiumCustomer customer) {
		this.customer = customer;
	}

	/**
	 * Returns the status as string.
	 *
	 * @return status as string
	 */
	public String getStatusAsString() {
		return status.get();
	}

	/**
	 * Gets contract status.
	 *
	 * @return contract status
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public ContractStatus getStatus() {
		return ContractStatus.valueOf(status.get());
	}

	/**
	 * Sets status.
	 *
	 * @param status the status
	 */
	public void setStatus(ContractStatus status) {
		this.status.set(String.valueOf(status));
	}

	/**
	 * Gets start date.
	 *
	 * @return start date
	 */
	@Access(AccessType.PROPERTY)
	public LocalDate getStartDate() {
		return LocalDate.parse(startDate.get());
	}

	/**
	 * Property contract start date
	 *
	 * @return start date
	 */
	public StringProperty contractStartDateProperty() {
		return startDate;
	}

	/**
	 * Sets start date.
	 *
	 * @param startDate the start date
	 */
	public void setStartDate(LocalDate startDate) {
		//if (startDate.isBefore(LocalDate.now())) {
		//	throw new IllegalArgumentException(LanguageResource.getString("startDate_invalid"));
		//}
		this.startDate.set(String.valueOf(startDate));
	}

	/**
	 * Gets end date.
	 *
	 * @return end date
	 */
	@Access(AccessType.PROPERTY)
	public LocalDate getEndDate() {
		return LocalDate.parse(endDate.get());
	}

	/**
	 * Gets property end date
	 *
	 * @return end date property
	 */
	public StringProperty contractEndDateProperty() {
		return endDate;
	}

	/**
	 * Sets end date.
	 *
	 * @param endDate the end date
	 */
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

	/**
	 * Gets property contract ID.
	 *
	 * @return contract ID property
	 */
	@Override
	public IntegerProperty contractIdProperty() {
		setContractIdInt();
		return contractIdInt;
	}

	/**
	 * Gets property contract type name.
	 *
	 * @return contract type name property
	 */
	@Override
	public StringProperty contractTypeNameProperty() {
		setContractTypeName();
		return contractTypeName;
	}

	/**
	 * Gets property status.
	 *
	 * @return status
	 */
	@Override
	public StringProperty contractStatusProperty() {
		return status;
	}

	@Override
	public String toString() {
		return String.format("%s: %s %s %s %s %s", this.getContractIdString(), this.contractType.getName(), this.getStatusAsString(), this.getStartDate().toString(), LanguageResource.getString("until") ,this.getEndDate().toString());
	}

	/**
	 * Check attributes.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		// Ms. Malfait her idea
		new ContractBuilder()
				.contractType(this.getContractType())
				.customer(this.getCustomer())
				.status(this.getStatus())
				.startDate(this.getStartDate())
				.endDate(this.getEndDate())
				.build();
	}

	/**
	 * The type Contract builder.
	 */
	public static class ContractBuilder {
		private ActemiumContractType contractType;
		private ActemiumCustomer customer;
		private ContractStatus status;
		private LocalDate startDate;
		private LocalDate endDate;

		private Set<RequiredElement> requiredElements;

		/**
		 * Contract type contract builder.
		 *
		 * @param contractType the contract type
		 * @return the contract builder
		 */
		public ContractBuilder contractType(ActemiumContractType contractType) {
			this.contractType = contractType;
			return this;
		}

		/**
		 * Customer contract builder.
		 *
		 * @param customer the customer
		 * @return the contract builder
		 */
		public ContractBuilder customer(ActemiumCustomer customer) {
			this.customer = customer;
			return this;
		}

		/**
		 * Status contract builder.
		 *
		 * @param status the status
		 * @return the contract builder
		 */
		public ContractBuilder status(ContractStatus status) {
			this.status = status;
			return this;
		}

		/**
		 * Start date contract builder.
		 *
		 * @param startDate the start date
		 * @return the contract builder
		 */
		public ContractBuilder startDate(LocalDate startDate) {
			this.startDate = startDate;
			return this;
		}

		/**
		 * End date contract builder.
		 *
		 * @param endDate the end date
		 * @return the contract builder
		 */
		public ContractBuilder endDate(LocalDate endDate) {
			this.endDate = endDate;
			return this;
		}

		/**
		 * Build actemium contract.
		 *
		 * @return the actemium contract
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumContract build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesEmployeeBuilder();
			return new ActemiumContract(this);
		}

		private void checkAttributesEmployeeBuilder() throws InformationRequiredException {
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
			if (status == null) {
				if (startDate.equals(LocalDate.now())) {
					status = ContractStatus.CURRENT;
				} else {
					status = ContractStatus.IN_REQUEST;
				}
			}

			requiredElements.forEach(System.out::println);

			if (!requiredElements.isEmpty()) {
				throw new InformationRequiredException(requiredElements);
			}
		}


	}

	/**
	 * This clones an actemium contract.
	 *
	 * @return actmium contract
	 * @throws CloneNotSupportedException throws a clone not supported exception
	 */
	@Override
	public ActemiumContract clone() throws CloneNotSupportedException {

		ActemiumContract cloned = null;
		try {
			cloned = new ContractBuilder()
					.contractType(this.getContractType())
					.customer(this.getCustomer())
					.status(this.getStatus())
					.startDate(this.getStartDate())
					.endDate(this.getEndDate())
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
		return cloned;
	}
}
