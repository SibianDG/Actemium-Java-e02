package domain;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Customer extends UserModel implements Seniority {
	@Serial
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerNr;
	@OneToMany
	private List<Contract> contracts;
	@ManyToOne
	private Company company;
	@ManyToMany(mappedBy = "customerList")
	private List<ContactPerson> contactPersons;

	private LocalDate registrationDate;

	public Customer() {
	}
	
	public Customer(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
		//this.contracts = new ArrayList<>();
		//this.contactPersons = new ArrayList<>();
	}


	public int getCustomerNr() {
		return customerNr;
	}

	private void setCustomerNr(int customerNr) {
		this.customerNr = customerNr;
	}

	/*
	public List<Contract> getContracts() {
		return contracts;
	}

	private void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public Company getCompany() {
		return company;
	}

	private void setCompany(Company company) {
		this.company = company;
	}

	public List<ContactPerson> getContactPersons() {
		return contactPersons;
	}

	private void setContactPersons(List<ContactPerson> contactPersons) {
		this.contactPersons = contactPersons;
	}*/

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	private void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - registrationDate.getYear();
	}
}
