package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.FIELD)
public class Customer extends UserModel implements Seniority {
	@Serial
	private static final long serialVersionUID = 1L;

	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerNr;
//	@OneToMany
//	private List<Contract> contracts;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Company company;
//	@ManyToMany(mappedBy = "customers",
//				cascade = CascadeType.PERSIST)
//	private List<Employee> contactPersons = new ArrayList<>();

	private LocalDate registrationDate;

	public Customer() {
		super();
	}
	
	public Customer(String username, String password, String firstName, String lastName, Company company) {
		super(username, password, firstName, lastName);
		setCompany(company);
		setRegistrationDate(LocalDate.now());
//		this.contracts = new ArrayList<>();
//		this.contactPersons = new ArrayList<>();
	}


	public int getCustomerNr() {
		return customerNr;
	}

	public void setCustomerNr(int customerNr) {
		this.customerNr = customerNr;
	}

	
//	public List<Contract> getContracts() {
//		return contracts;
//	}
//
//	public void setContracts(List<Contract> contracts) {
//		this.contracts = contracts;
//	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

//	public List<Employee> getContactPersons() {
//		return contactPersons;
//	}
//
//	public void setContactPersons(List<Employee> contactPersons) {
//		this.contactPersons = contactPersons;
//	}
	
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
	
//	public void addContactPerson(Employee contactPerson) {
//		contactPersons.add(contactPerson);
//	}

	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - registrationDate.getYear();
	}	


}
