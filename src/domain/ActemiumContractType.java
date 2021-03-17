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

@Entity
@Access(AccessType.FIELD)
public class ActemiumContractType implements ContractType, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	//TODO
	// Name is primary key so it cannot be changed
	// This could be a good thing
	// this means if you want to change a contract type name
	// it's better to set the old contract type to inactive
	// and create a new contractype with a new name
	@Id
	private String name;

	@Transient
	private StringProperty contractTypeName = new SimpleStringProperty();

	@Transient
	private StringProperty contractTypeStatus = new SimpleStringProperty();

	private boolean hasEmail;
	private boolean hasPhone;
	private boolean hasApplication;

	@Transient
	private StringProperty timestamp = new SimpleStringProperty();
	//TODO
	// Does the integer represent:
	// Days, Hours, Minutes?
	private int maxHandlingTime;
	private int minThroughputTime;
	// Price per year?
	private double price;
	
	public ActemiumContractType() {
		super();
	}

	public ActemiumContractType(ContractTypeBuilder builder){
		this.contractTypeName.set(builder.contractTypeName);
		this.name = (builder.contractTypeName);
		this.contractTypeStatus.set(String.valueOf(builder.contractTypeStatus));
		this.hasEmail = builder.hasEmail;
		this.hasPhone = builder.hasPhone;
		this.hasApplication = builder.hasApplication;
		this.timestamp.set(String.valueOf(builder.timestamp));
		this.maxHandlingTime = builder.maxHandlingTime;
		this.minThroughputTime = builder.minThroughputTime;
		this.price = builder.price;
	}

	/*public ActemiumContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
			boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) {
		super();
		verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		setName(name);
		setContractTypeStatus(contractTypeStatus);
		// Not using setters for has... because then verifyTicketCreationMethods()
		// would be called 3 extra times, which isn't necessary
		this.hasEmail = hasEmail;
		this.hasPhone = hasPhone;
		this.hasApplication = hasApplication;
		setTimestamp(timestamp);
		setMaxHandlingTime(maxHandlingTime);
		setMinThroughputTime(minThroughputTime);
		setPrice(price);
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		////TODO no naming rules yet
		//if (name == null || name.isBlank()) {
		//	throw new IllegalArgumentException(LanguageResource.getString("name_invalid"));
		//}
		////TODO check if contractTypeName is not already taken,
		//// two contractTypes can't have the same name
		setContractTypeName();
		this.name = name;
	}
	
	private void setContractTypeName() {
		this.contractTypeName.set(name);
	}

	public String getContractTypeStatus() {
		return contractTypeStatus.get();
	}

	public ContractTypeStatus getContractTypeStatusAsEnum() {
		return ContractTypeStatus.valueOf(contractTypeStatus.get());
	}

	public void setContractTypeStatus(ContractTypeStatus contractTypeStatus) {
		this.contractTypeStatus.set(contractTypeStatus.toString());
	}

	public boolean isHasEmail() {
		return hasEmail;
	}

	public void setHasEmail(boolean hasEmail) {
		//verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasEmail = hasEmail;
	}

	public boolean isHasPhone() {
		return hasPhone;
	}

	public void setHasPhone(boolean hasPhone) {
		//verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasPhone = hasPhone;
	}

	public boolean isHasApplication() {
		return hasApplication;
	}

	public void setHasApplication(boolean hasApplication) {
		//verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasApplication = hasApplication;
	}

	public String getTimestamp() {
		return timestamp.get();
	}
	
	public Timestamp getTimestampAsEnum() {
		return Timestamp.valueOf(timestamp.get());
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp.set(timestamp.toString());
	}

	public int getMaxHandlingTime() {
		return maxHandlingTime;
	}

	public void setMaxHandlingTime(int maxHandlingTime) {
		if(maxHandlingTime <= 0) {
			throw new IllegalArgumentException(LanguageResource.getString("maxHandlingTime_invalid"));
		}
		this.maxHandlingTime = maxHandlingTime;
	}

	public int getMinThroughputTime() {
		return minThroughputTime;
	}

	public void setMinThroughputTime(int minThroughputTime) {
		if(minThroughputTime <= 0) {
			throw new IllegalArgumentException(LanguageResource.getString("minThroughputTime_invalid"));
		}
		this.minThroughputTime = minThroughputTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if(price <= 0) {
			throw new IllegalArgumentException(LanguageResource.getString("price_invalid"));
		}
		this.price = price;
	}
		
	//TODO this method should be private but than you can't test it
	// or shouldn't we test this method seperately?
	/**
	 * Ensures that at least 1 ticket creation method is selected.
	 * @param hasEmail
	 * @param hasPhone
	 * @param hasApplication
	 * @throws IllegalArgumentException
	 */
	public void verifyTicketCreationMethods(boolean hasEmail, boolean hasPhone,
			boolean hasApplication) {
		if (!(hasEmail || hasPhone || hasApplication)) {
			throw new IllegalArgumentException(LanguageResource.getString("ticketCreation_invalid"));
		}
	}
	
	public StringProperty contractTypeNameProperty() {
		setContractTypeName();
		return contractTypeName;
	}
	
	public StringProperty contractTypeStatusProperty() {
		return contractTypeStatus;
	}
	
	public StringProperty contractTypestampProperty() {
		return timestamp;
	}

	public void checkAttributes() throws InformationRequiredException {
		// Ms. Malfait her idea
		new ContractTypeBuilder()
				.contractTypeName(this.contractTypeNameProperty().get())
				.contractTypeStatus(this.getContractTypeStatusAsEnum())
				.hasEmail(this.hasEmail)
				.hasPhone(this.hasPhone)
				.hasApplication(this.isHasApplication())
				.timestamp(this.getTimestampAsEnum())
				.maxHandlingTime(this.getMaxHandlingTime())
				.minThroughputTime(this.minThroughputTime)
				.price(this.price)
				.build();
	}

	public static class ContractTypeBuilder {
		private String contractTypeName;
		private ContractTypeStatus contractTypeStatus;
		private boolean hasEmail;
		private boolean hasPhone;
		private boolean hasApplication;
		private Timestamp timestamp;
		private int maxHandlingTime;
		private int minThroughputTime;
		private double price;

		private Set<RequiredElement> requiredElements;

		public ContractTypeBuilder contractTypeName(String contractTypeName) {
			this.contractTypeName = contractTypeName;
			return this;
		}

		public ContractTypeBuilder contractTypeStatus(ContractTypeStatus contractTypeStatus) {
			this.contractTypeStatus = contractTypeStatus;
			return this;
		}

		public ContractTypeBuilder hasEmail(boolean hasEmail) {
			this.hasEmail = hasEmail;
			return this;
		}

		public ContractTypeBuilder hasPhone(boolean hasPhone) {
			this.hasPhone = hasPhone;
			return this;
		}

		public ContractTypeBuilder hasApplication(boolean hasApplication) {
			this.hasApplication = hasApplication;
			return this;
		}

		public ContractTypeBuilder timestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public ContractTypeBuilder maxHandlingTime(int maxHandlingTime) {
			this.maxHandlingTime = maxHandlingTime;
			return this;
		}

		public ContractTypeBuilder minThroughputTime(int minThroughputTime) {
			this.minThroughputTime = minThroughputTime;
			return this;
		}

		public ContractTypeBuilder price(double price) {
			this.price = price;
			return this;
		}

		public ActemiumContractType build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesEmployeeBuiler();
			return new ActemiumContractType(this);
		}

		private void checkAttributesEmployeeBuiler() throws InformationRequiredException {
			if (contractTypeName == null || contractTypeName.isBlank())
				requiredElements.add(RequiredElement.ContractTypeNameRequired);
			if (contractTypeStatus == null)
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

	public ActemiumContractType clone() throws CloneNotSupportedException {

		ActemiumContractType cloned = null;
		try {
			cloned = new ContractTypeBuilder()
					.contractTypeName(this.contractTypeNameProperty().get())
					.contractTypeStatus(this.getContractTypeStatusAsEnum())
					.hasEmail(this.isHasEmail())
					.hasPhone(this.isHasPhone())
					.hasPhone(this.isHasPhone())
					.timestamp(this.getTimestampAsEnum())
					.maxHandlingTime(this.getMaxHandlingTime())
					.minThroughputTime(this.getMinThroughputTime())
					.price(this.getPrice())
					.build();
		} catch (InformationRequiredException e) {
			//this should be a good Employee
			e.printStackTrace();
		}
		return cloned;
	}
	
}
