package domain;

import java.time.LocalDate;

public class Employee extends User {

	private int employeeNr;
	private String address;
	private String phoneNumber;
	private String emailAddress;
	private String role;
	private LocalDate registrationDate;

	public Employee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, String role) {
		super(username, password, firstName, lastName);
	}

	//"^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"


	public int giveEmployeeSeniority() {
		throw new UnsupportedOperationException();
	}
}
