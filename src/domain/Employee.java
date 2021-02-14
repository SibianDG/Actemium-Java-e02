package domain;

public class Employee extends User {

	private int employeeNr;
	private String address;
	private int phoneNumber;
	private String emailAddress;
	private String role;
	private LocalDate registrationDate;

	public int giveEmployeeSeniority() {
		throw new UnsupportedOperationException();
	}

	public Employee(String username, String password, String firstName, String lastName, String address,
			int phoneNumber, String emailAddress, String role) {
		throw new UnsupportedOperationException();
	}
}
