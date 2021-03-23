package domain;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Customer is the contactPerson of a company
// Company is the "real" Customer, but for ease of naming
// we will use the name Customer for contactPerson
// Could be changed if we realy have to

@Entity
@Access(AccessType.FIELD)
public class ActemiumCustomer extends UserModel implements Customer, Seniority {
	@Serial
	private static final long serialVersionUID = 1L;

	//@GeneratedValue(strategy = GenerationType.AUTO)
	//private int customerNr;
	
	@OneToMany(mappedBy = "customer",
			   cascade = CascadeType.PERSIST)
	private List<ActemiumContract> contracts = new ArrayList<>();;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private ActemiumCompany company;
	
	@OneToMany(mappedBy = "customer",
			   cascade = CascadeType.PERSIST)
	private List<ActemiumTicket> tickets = new ArrayList<>();

	private LocalDate registrationDate;

	public ActemiumCustomer() {
		super();
	}
	
	/*public ActemiumCustomer(String username, String password, String firstName, String lastName, ActemiumCompany company) {
		super(username, password, firstName, lastName);
		setCompany(company);
		setRegistrationDate(LocalDate.now());
	}

	 */

	public ActemiumCustomer(CustomerBuilder builder){
		super(builder.username, builder.password, builder.firstName, builder.lastName);
		this.company = builder.company;
		this.registrationDate = builder.registrationDate;
	}



	public int getCustomerNr() {
		return (int) super.getUserId();
	}

	public List<ActemiumContract> getContracts() {
		return contracts;
	}

	public ObservableList<Contract> giveContracts() {
		return (ObservableList<Contract>) (Object) FXCollections.observableList(contracts);
	}

	public void setContracts(List<ActemiumContract> contracts) {
		this.contracts = contracts;
	}

	public ActemiumCompany getCompany() {
		return company;
	}
	
	public Company giveCompany() {
		return (Company) company;
	}

	public void setCompany(ActemiumCompany company) {
		this.company = company;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	public void addTicket(ActemiumTicket ticket) {
		tickets.add(ticket);
	}

	public void addContract(ActemiumContract contract) {
		contracts.add(contract);
	}

	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - registrationDate.getYear();
	}

	public void checkAttributes() throws InformationRequiredException {
		// Ms. Malfait her idea
		new CustomerBuilder()
				.username(super.usernameProperty().get())
				.password(super.getPassword())
				.firstName(super.firstNameProperty().get())
				.lastName(super.lastNameProperty().get())
				.company(this.company)
				.registrationDate(this.registrationDate)
				.build();
	}

	public static class CustomerBuilder {
		private String username;
		private String password;
		private String firstName;
		private String lastName;

		private ActemiumCompany company;
		private LocalDate registrationDate;

		private Set<RequiredElement> requiredElements;


		public CustomerBuilder username(String username) {
			this.username = username;
			return this;
		}
		public CustomerBuilder password(String password) {
			this.password = password;
			return this;
		}
		public CustomerBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		public CustomerBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		public CustomerBuilder company(ActemiumCompany company) {
			this.company = company;
			return this;
		}
		public CustomerBuilder registrationDate(LocalDate registrationDate) {
			this.registrationDate = registrationDate;
			return this;
		}

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
			if (registrationDate == null)
				registrationDate = LocalDate.now();

			if (!requiredElements.isEmpty())
				throw new InformationRequiredException(requiredElements);

		}
	}

	public ActemiumCustomer clone() throws CloneNotSupportedException {

		ActemiumCustomer cloned = null;
		try {
			cloned = new ActemiumCustomer.CustomerBuilder()
					.username(this.usernameProperty().get())
					.password(this.getPassword())
					.firstName(this.firstNameProperty().get())
					.lastName(this.lastNameProperty().get())
					//TODO: clone company as well
					.company(this.getCompany())
					.build();
		} catch (InformationRequiredException e) {
			//this should be a good Employee
			e.printStackTrace();
		}
		return cloned;
	}



}
