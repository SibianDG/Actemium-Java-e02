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

	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerNr;
	
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
	
	public ActemiumCustomer(String username, String password, String firstName, String lastName, ActemiumCompany company) {
		super(username, password, firstName, lastName);
		setCompany(company);
		setRegistrationDate(LocalDate.now());
	}


	public int getCustomerNr() {
		return customerNr;
	}

	public void setCustomerNr(int customerNr) {
		this.customerNr = customerNr;
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


}
