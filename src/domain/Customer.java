package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Customer extends UserModel implements Seniority {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerNr;
	//@OneToMany(mappedBy = "customer")
	//private List<Contract> contracts;
	//@OneToOne(mappedBy = "customer")
	//private Company company;
	//@ManyToMany(mappedBy = "customer")
	//private List<ContactPerson> contactPersons;
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
