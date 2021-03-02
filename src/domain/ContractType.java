package domain;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class ContractType implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	private String name;
	@Enumerated(EnumType.STRING)
	private ContractTypeStatus contractTypeStatus;
	private boolean hasEmail;
	private boolean hasPhone;
	private boolean hasApplication;
	@Enumerated(EnumType.STRING)
	private Timestamp timestamp;
	// Does the integer represent:
	// Days, Hours, Minutes?
	private int maxHandlingTime;
	private int minThroughputTime;
	// Price per year?
	private double price;
	
	public ContractType() {
		super();
	}
	
	public ContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
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
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		//TODO no naming rules yet
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name can't be left blank.");
		}
		//TODO check if contractTypeName is not already taken,
		// two contractTypes can't have the same name
		this.name = name;
	}

	public ContractTypeStatus getContractTypeStatus() {
		return contractTypeStatus;
	}

	public void setContractTypeStatus(ContractTypeStatus contractTypeStatus) {
		this.contractTypeStatus = contractTypeStatus;
	}

	public boolean isHasEmail() {
		return hasEmail;
	}

	public void setHasEmail(boolean hasEmail) {
		verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasEmail = hasEmail;
	}

	public boolean isHasPhone() {
		return hasPhone;
	}

	public void setHasPhone(boolean hasPhone) {
		verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasPhone = hasPhone;
	}

	public boolean isHasApplication() {
		return hasApplication;
	}

	public void setHasApplication(boolean hasApplication) {
		verifyTicketCreationMethods(hasEmail, hasPhone, hasApplication);
		this.hasApplication = hasApplication;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getMaxHandlingTime() {
		return maxHandlingTime;
	}

	public void setMaxHandlingTime(int maxHandlingTime) {
		if(maxHandlingTime <= 0) {
			throw new IllegalArgumentException("maxHandlingTime must be greater than 0.");
		}
		this.maxHandlingTime = maxHandlingTime;
	}

	public int getMinThroughputTime() {
		return minThroughputTime;
	}

	public void setMinThroughputTime(int minThroughputTime) {
		if(minThroughputTime <= 0) {
			throw new IllegalArgumentException("minThroughputTime must be greater than 0.");
		}
		this.minThroughputTime = minThroughputTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if(price <= 0) {
			throw new IllegalArgumentException("Price must be greater than 0.");
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
			throw new IllegalArgumentException("You must have at least 1 way to create the ticket.");
		}
	}
	
}
