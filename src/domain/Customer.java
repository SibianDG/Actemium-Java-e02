package domain;

import java.time.LocalDate;
import java.time.*;

public class Customer extends User implements Seniority {

	private Contract contracts;
	private Company company;
	private ContactPerson contactPersons;
	private int customerNr;
	private LocalDate registrationDate;

	public Customer(String username, String password, String firstName, String lastName) {
		super(username, password, firstName, lastName);
	}

	public Customer() {
	}

	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - registrationDate.getYear();
	}
}
