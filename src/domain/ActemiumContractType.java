package domain;

import domain.enums.ContractTypeStatus;
import domain.enums.RequiredElement;
import domain.enums.Timestamp;
import exceptions.InformationRequiredException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import languages.LanguageResource;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * The type Actemium contract type.
 */
@Entity
@Access(AccessType.FIELD)
public class ActemiumContractType implements ContractType, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int contractTypeId;

	@Transient
	private StringProperty name = new SimpleStringProperty();

	@Transient
	private StringProperty status = new SimpleStringProperty();

	private boolean hasEmail;
	private boolean hasPhone;
	private boolean hasApplication;

	@Transient
	private StringProperty timestamp = new SimpleStringProperty();
	private int maxHandlingTime;
	private int minThroughputTime;
	// Price per year?
	private double price;

	/**
	 * Instantiates a new Actemium contract type.
	 */
	public ActemiumContractType() {
		super();
	}

	/**
	 * Instantiates a new Actemium contract type.
	 *
	 * @param builder the builder
	 */
	public ActemiumContractType(ContractTypeBuilder builder){
		this.name.set(builder.name);
		this.status.set(String.valueOf(builder.status));
		this.hasEmail = builder.hasEmail;
		this.hasPhone = builder.hasPhone;
		this.hasApplication = builder.hasApplication;
		this.timestamp.set(String.valueOf(builder.timestamp));
		this.maxHandlingTime = builder.maxHandlingTime;
		this.minThroughputTime = builder.minThroughputTime;
		this.price = builder.price;
	}

	/**
	 * Gets contract type ID
	 *
	 * @return contract type ID
	 */
	public int getContractTypeId() {
		return contractTypeId;
	}

	/**
	 * Gets the name.
	 *
	 * @return name
	 */
	@Access(AccessType.PROPERTY)
	public String getName() {
		return name.get();
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name.set(name);
	}

	/**
	 *	Gets the status as string.
	 *
	 * @return status
	 */
	public String getStatusAsString() {
		return status.get();
	}

	/**
	 * Gets Contract type status.
	 *
	 * @return Contract type status
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public ContractTypeStatus getStatus() {
		return ContractTypeStatus.valueOf(status.get());
	}

	/**
	 * Sets status.
	 *
	 * @param contractTypeStatus the contract type status
	 */
	public void setStatus(ContractTypeStatus contractTypeStatus) {
		this.status.set(contractTypeStatus.toString());
	}

	/**
	 * Gets the boolean of the contract type has a way to contact via email.
	 *
	 * @return has Email
	 */
	public boolean hasEmail() {
		return hasEmail;
	}

	/**
	 * Sets has email.
	 *
	 * @param hasEmail the has email
	 */
	public void setHasEmail(boolean hasEmail) {
		//verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasEmail = hasEmail;
	}

	/**
	 * Gets the boolean of the contract type has a way to contact via phone.
	 *
	 * @return has Phone
	 */
	public boolean hasPhone() {
		return hasPhone;
	}

	/**
	 * Sets has phone.
	 *
	 * @param hasPhone the has phone
	 */
	public void setHasPhone(boolean hasPhone) {
		//verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasPhone = hasPhone;
	}

	/**
	 * Gets the boolean of the contract type has a way to contact via application.
	 *
	 * @return has application
	 */
	public boolean hasApplication() {
		return hasApplication;
	}

	/**
	 * Sets has application.
	 *
	 * @param hasApplication the has application
	 */
	public void setHasApplication(boolean hasApplication) {
		//verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasApplication = hasApplication;
	}

	/**
	 * Gets the timestamp as string.
	 *
	 * @return timestamp as string
	 */
	public String getTimestampAsString() {
		return timestamp.get();
	}

	/**
	 * Gets the Timestamp.
	 *
	 * @return Timestamp
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public Timestamp getTimestamp() {
		return Timestamp.valueOf(timestamp.get());
	}

	/**
	 * Sets timestamp.
	 *
	 * @param timestamp the timestamp
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp.set(timestamp.toString());
	}

	/**
	 * Gets the max handling time.
	 *
	 * @return max handling time
	 */
	public int getMaxHandlingTime() {
		return maxHandlingTime;
	}

	/**
	 * Sets max handling time.
	 *
	 * @param maxHandlingTime the max handling time
	 */
	public void setMaxHandlingTime(int maxHandlingTime) {
		if(maxHandlingTime <= 0) {
			throw new IllegalArgumentException(LanguageResource.getString("maxHandlingTime_invalid"));
		}
		this.maxHandlingTime = maxHandlingTime;
	}

	/**
	 *	Gets the min throughput time.
	 *
	 * @return min throughput time
	 */
	public int getMinThroughputTime() {
		return minThroughputTime;
	}

	/**
	 * Sets min throughput time.
	 *
	 * @param minThroughputTime the min throughput time
	 */
	public void setMinThroughputTime(int minThroughputTime) {
		if(minThroughputTime <= 0) {
			throw new IllegalArgumentException(LanguageResource.getString("minThroughputTime_invalid"));
		}
		this.minThroughputTime = minThroughputTime;
	}

	/**
	 * Gets the price.
	 *
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets price.
	 *
	 * @param price the price
	 */
	public void setPrice(double price) {
		if(price <= 0) {
			throw new IllegalArgumentException(LanguageResource.getString("price_invalid"));
		}
		this.price = price;
	}
	
	/**
	 * Gets the name property.
	 *
	 * @return name property
	 */
	public StringProperty contractTypeNameProperty() {
		return name;
	}

	/**
	 * Gets the status property.
	 *
	 * @return status property
	 */
	public StringProperty contractTypeStatusProperty() {
		return status;
	}

	/**
	 * Gets the timestamp property.
	 *
	 * @return timestamp property
	 */
	public StringProperty contractTypestampProperty() {
		return timestamp;
	}

	/**
	 * Check attributes.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		// Ms. Malfait her idea
		new ContractTypeBuilder()
				.name(this.getName())
				.status(this.getStatus())
				.hasEmail(this.hasEmail)
				.hasPhone(this.hasPhone)
				.hasApplication(this.hasApplication)
				.timestamp(this.getTimestamp())
				.maxHandlingTime(this.getMaxHandlingTime())
				.minThroughputTime(this.minThroughputTime)
				.price(this.price)
				.build();
	}

	/**
	 * The type Contract type builder.
	 */
	public static class ContractTypeBuilder {
		private String name;
		private ContractTypeStatus status;
		private boolean hasEmail;
		private boolean hasPhone;
		private boolean hasApplication;
		private Timestamp timestamp;
		private int maxHandlingTime;
		private int minThroughputTime;
		private double price;

		private Set<RequiredElement> requiredElements;

		/**
		 * Name contract type builder.
		 *
		 * @param name the name
		 * @return the contract type builder
		 */
		public ContractTypeBuilder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Status contract type builder.
		 *
		 * @param status the status
		 * @return the contract type builder
		 */
		public ContractTypeBuilder status(ContractTypeStatus status) {
			this.status = status;
			return this;
		}

		/**
		 * Has email contract type builder.
		 *
		 * @param hasEmail the has email
		 * @return the contract type builder
		 */
		public ContractTypeBuilder hasEmail(boolean hasEmail) {
			this.hasEmail = hasEmail;
			return this;
		}

		/**
		 * Has phone contract type builder.
		 *
		 * @param hasPhone the has phone
		 * @return the contract type builder
		 */
		public ContractTypeBuilder hasPhone(boolean hasPhone) {
			this.hasPhone = hasPhone;
			return this;
		}

		/**
		 * Has application contract type builder.
		 *
		 * @param hasApplication the has application
		 * @return the contract type builder
		 */
		public ContractTypeBuilder hasApplication(boolean hasApplication) {
			this.hasApplication = hasApplication;
			return this;
		}

		/**
		 * Timestamp contract type builder.
		 *
		 * @param timestamp the timestamp
		 * @return the contract type builder
		 */
		public ContractTypeBuilder timestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		/**
		 * Max handling time contract type builder.
		 *
		 * @param maxHandlingTime the max handling time
		 * @return the contract type builder
		 */
		public ContractTypeBuilder maxHandlingTime(int maxHandlingTime) {
			this.maxHandlingTime = maxHandlingTime;
			return this;
		}

		/**
		 * Min throughput time contract type builder.
		 *
		 * @param minThroughputTime the min throughput time
		 * @return the contract type builder
		 */
		public ContractTypeBuilder minThroughputTime(int minThroughputTime) {
			this.minThroughputTime = minThroughputTime;
			return this;
		}

		/**
		 * Price contract type builder.
		 *
		 * @param price the price
		 * @return the contract type builder
		 */
		public ContractTypeBuilder price(double price) {
			this.price = price;
			return this;
		}

		/**
		 * Build actemium contract type.
		 *
		 * @return the actemium contract type
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumContractType build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesEmployeeBuiler();
			return new ActemiumContractType(this);
		}

		private void checkAttributesEmployeeBuiler() throws InformationRequiredException {
			if (name == null || name.isBlank())
				requiredElements.add(RequiredElement.ContractTypeNameRequired);
			if (status == null)
				requiredElements.add(RequiredElement.ContractTypeStatusRequired);
			if (timestamp == null)
				requiredElements.add(RequiredElement.ContractTypeStatusRequired);
			if (maxHandlingTime <= 0)
				requiredElements.add(RequiredElement.ContractTypeMaxHandlingTimeRequired);
			if (minThroughputTime <= 0)
				requiredElements.add(RequiredElement.ContractTypeMinTroughPutTimeRequired);
			if (price <= 0)
				requiredElements.add(RequiredElement.ContractTypePriceRequired);
			if (!(hasEmail || hasPhone || hasApplication))
				requiredElements.add(RequiredElement.ContractTypeWayRequired);

			if (!requiredElements.isEmpty()) {
				throw new InformationRequiredException(requiredElements);
			}

		}
	}

	/**
	 * This clones an actemium Ctontract type.
	 *
	 * @return Actemium Contract Type
	 * @throws CloneNotSupportedException
	 */
	@Override
	public ActemiumContractType clone() throws CloneNotSupportedException {

		ActemiumContractType cloned = null;
		try {
			cloned = new ContractTypeBuilder()
					.name(this.getName())
					.status(this.getStatus())
					.hasEmail(this.hasEmail())
					.hasPhone(this.hasPhone())
					.hasEmail(this.hasEmail())
					.timestamp(this.getTimestamp())
					.maxHandlingTime(this.getMaxHandlingTime())
					.minThroughputTime(this.getMinThroughputTime())
					.price(this.getPrice())
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
		return cloned;
	}
	
}
