package domain;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

// Customer is the contactPerson of a company
// Company is the "real" Customer, but for ease of naming
// we will use the name Customer for contactPerson
// Could be changed if we realy have to

@Entity
@Access(AccessType.FIELD)
public class ActemiumCustomer extends UserModel implements Customer, Seniority {
	@Serial
	private static final long serialVersionUID = 1L;

	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerNr;
//	@OneToMany
//	private List<Contract> contracts;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private ActemiumCompany company;
//	@ManyToMany(mappedBy = "customers",
//				cascade = CascadeType.PERSIST)
//	private List<Employee> contactPersons = new ArrayList<>();
	
	@OneToMany(mappedBy = "customer",
			   cascade = CascadeType.PERSIST)
	private List<ActemiumTicket> tickets = new ArrayList<>();

	private LocalDate registrationDate;

	public ActemiumCustomer() {
		super();
	}
	
	public ActemiumCustomer(String username, String password, String firstName, String lastName, ActemiumCompany company) {
		super(username, password, firstName, lastName);
		setCompany(company);
		setRegistrationDate(LocalDate.now());
//		this.contracts = new ArrayList<>();
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

	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - registrationDate.getYear();
	}	


}
