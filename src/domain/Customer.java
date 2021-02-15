package domain;

import java.time.LocalDate;

public class Customer extends User {

	private Contract contracts;
	private Company company;
	private ContactPerson contactPersons;
	private int customerNr;
	private LocalDate registrationDate;

	public Customer() {
		throw new UnsupportedOperationException();
	}

	public Customer(LocalDate registrationDate) {
		throw new UnsupportedOperationException();
	}

	public int giveCustomerSeniority() {
		throw new UnsupportedOperationException();
	}
}
