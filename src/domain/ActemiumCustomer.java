package domain;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Customer is the contactPerson of a company
// Company is the "real" Customer, but for ease of naming
// we will use the name Customer for contactPerson
// Could be changed if we realy have to

/**
 * The type Actemium customer.
 */
@Entity
@Access(AccessType.FIELD)
public class ActemiumCustomer extends UserModel implements Customer, Seniority {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "customer",
			   cascade = CascadeType.PERSIST)
	private List<ActemiumContract> contracts = new ArrayList<>();;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private ActemiumCompany company;
	
	@OneToMany(mappedBy = "customer",
			   cascade = CascadeType.PERSIST)
	private List<ActemiumTicket> tickets = new ArrayList<>();

	/**
	 * Instantiates a new Actemium customer.
	 */
	public ActemiumCustomer() {
		super();
	}
	

	/**
	 * Instantiates a new Actemium customer.
	 *
	 * @param builder the builder
	 */
	public ActemiumCustomer(CustomerBuilder builder){
		super(builder.username, builder.password, builder.firstName, builder.lastName);
		this.company = builder.company;
	}

	/**
	 *	Gets the customer nr. In fact the userid.
	 *
	 * @return customer nr
	 */
	public int getCustomerNr() {
		return (int) super.getUserId();
	}

	/**
	 * Gets contracts.
	 *
	 * @return the contracts
	 */
	public List<ActemiumContract> getContracts() {
		return contracts;
	}

	/**
	 * Gives the contracts in a observable list.
	 *
	 * @return ObservableList of contracts
	 */
	public ObservableList<Contract> giveContracts() {
		return (ObservableList<Contract>) (Object) FXCollections.observableList(contracts);
	}

	/**
	 * Sets contracts.
	 *
	 * @param contracts the contracts
	 */
	public void setContracts(List<ActemiumContract> contracts) {
		this.contracts = contracts;
	}

	/**
	 * Gets company.
	 *
	 * @return the company
	 */
	public ActemiumCompany getCompany() {
		return company;
	}

	/**
	 * Give the company of a customer.
	 *
	 * @return company
	 */
	public Company giveCompany() {
		return (Company) company;
	}

	/**
	 * Sets company.
	 *
	 * @param company the company
	 */
	public void setCompany(ActemiumCompany company) {
		this.company = company;
	}

	/**
	 * Add ticket.
	 *
	 * @param ticket the ticket
	 */
	public void addTicket(ActemiumTicket ticket) {
		tickets.add(ticket);
	}

	/**
	 * Add contract.
	 *
	 * @param contract the contract
	 */
	public void addContract(ActemiumContract contract) {
		contracts.add(contract);
	}

	/**
	 * Give seniority int for the customer.
	 *
	 * @return the int
	 */
	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - super.getRegistrationDate().getYear();
	}

	/**
	 * Check attributes.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		new CustomerBuilder()
				.username(super.usernameProperty().get())
				.password(super.getPassword())
				.firstName(super.firstNameProperty().get())
				.lastName(super.lastNameProperty().get())
				.company(this.company)
				.registrationDate(super.getRegistrationDate())
				.build();
	}

	/**
	 * The type Customer builder.
	 */
	public static class CustomerBuilder {
		private String username;
		private String password;
		private String firstName;
		private String lastName;

		private ActemiumCompany company;
		private LocalDate registrationDate;

		private Set<RequiredElement> requiredElements;


		/**
		 * Username customer builder.
		 *
		 * @param username the username
		 * @return the customer builder
		 */
		public CustomerBuilder username(String username) {
			this.username = username;
			return this;
		}

		/**
		 * Password customer builder.
		 *
		 * @param password the password
		 * @return the customer builder
		 */
		public CustomerBuilder password(String password) {
			this.password = password;
			return this;
		}

		/**
		 * First name customer builder.
		 *
		 * @param firstName the first name
		 * @return the customer builder
		 */
		public CustomerBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		/**
		 * Last name customer builder.
		 *
		 * @param lastName the last name
		 * @return the customer builder
		 */
		public CustomerBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		/**
		 * Company customer builder.
		 *
		 * @param company the company
		 * @return the customer builder
		 */
		public CustomerBuilder company(ActemiumCompany company) {
			this.company = company;
			return this;
		}

		/**
		 * Registration date customer builder.
		 *
		 * @param registrationDate the registration date
		 * @return the customer builder
		 */
		public CustomerBuilder registrationDate(LocalDate registrationDate) {
			this.registrationDate = registrationDate;
			return this;
		}

		/**
		 * Build actemium customer.
		 *
		 * @return the actemium customer
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumCustomer build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesEmployeeBuiler();
			return new ActemiumCustomer(this);
		}

		private void checkAttributesEmployeeBuiler() throws InformationRequiredException {
			if (username == null || username.isBlank() || !username.matches("[A-Za-z0-9]+"))
				requiredElements.add(RequiredElement.UsernameRequired);
			if(password == null || password.isBlank() || !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()â€“{}:;',.?/*~$^+=<>]).{8,}$"))
				requiredElements.add(RequiredElement.PasswordRequired);
			if(firstName == null || firstName.isBlank() || !firstName.matches("[^0-9]+"))
				requiredElements.add(RequiredElement.FirstnameRequired);
			if(lastName == null || lastName.isBlank() || !lastName.matches("[^0-9]+"))
				requiredElements.add(RequiredElement.LastnameRequired);
			if (company == null)
				requiredElements.add(RequiredElement.CompanyNameRequired);

			if (!requiredElements.isEmpty())
				throw new InformationRequiredException(requiredElements);

		}
	}

	/**
	 * This clones an actemium customer.
	 *
	 * @return Actemium Customer
	 * @throws CloneNotSupportedException throws a clone not supported exception
	 */
	@Override
	public ActemiumCustomer clone() throws CloneNotSupportedException {

		ActemiumCustomer cloned = null;
		try {
			cloned = new ActemiumCustomer.CustomerBuilder()
					.username(this.usernameProperty().get())
					.password(this.getPassword())
					.firstName(this.firstNameProperty().get())
					.lastName(this.lastNameProperty().get())
					.company(this.getCompany())
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
		return cloned;
	}



}
