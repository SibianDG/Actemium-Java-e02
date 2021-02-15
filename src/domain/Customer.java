package domain;

import java.time.LocalDate;

public class Customer extends User {

	private Contract contracts;
	private Company company;
	private ContactPerson contactPersons;
	private int customerNr;
	private LocalDate registrationDate;

	public Customer(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
	}

	public int giveCustomerSeniority() {
		throw new UnsupportedOperationException();
	}
}
