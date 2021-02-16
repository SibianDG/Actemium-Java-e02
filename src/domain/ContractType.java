package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class ContractType implements Serializable {
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
	private int maxHandlingTime;
	private int minThroughputTime;
	private double price;

	
	public ContractType() {
		super();
	}
	
	public ContractType(String name, ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone,
			boolean hasApplication, Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) {
		super();
		this.name = name;
		this.contractTypeStatus = contractTypeStatus;
		this.hasEmail = hasEmail;
		this.hasPhone = hasPhone;
		this.hasApplication = hasApplication;
		this.timestamp = timestamp;
		this.maxHandlingTime = maxHandlingTime;
		this.minThroughputTime = minThroughputTime;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
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
		this.hasEmail = hasEmail;
	}

	public boolean isHasPhone() {
		return hasPhone;
	}

	public void setHasPhone(boolean hasPhone) {
		this.hasPhone = hasPhone;
	}

	public boolean isHasApplication() {
		return hasApplication;
	}

	public void setHasApplication(boolean hasApplication) {
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
		this.maxHandlingTime = maxHandlingTime;
	}

	public int getMinThroughputTime() {
		return minThroughputTime;
	}

	public void setMinThroughputTime(int minThroughputTime) {
		this.minThroughputTime = minThroughputTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
}
