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
		setAddress(address);
		setPhoneNumber(phoneNumber);
		setEmailAddress(emailAddress);
		setRole(role);
		setRegistrationDate(LocalDate.now());
	}

	public int giveEmployeeSeniority() {
		throw new UnsupportedOperationException();
	}

	public int getEmployeeNr() {
		return employeeNr;
	}

	public void setEmployeeNr(int employeeNr) {
		this.employeeNr = employeeNr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		String usernameRegex = "[A-Za-z0-9 _-]+";
		if(address == null || address.isBlank() || !address.matches(usernameRegex)) {
			throw new IllegalArgumentException("Invalid address");
		}
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		String usernameRegex = "[0-9 /-]+";
		if(phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches(usernameRegex)) {
			throw new IllegalArgumentException("Invalid phone number");
		}
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		String usernameRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		if(emailAddress == null || emailAddress.isBlank() || !emailAddress.matches(usernameRegex)) {
			throw new IllegalArgumentException("Invalid email address");
		}
		this.emailAddress = emailAddress;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		String usernameRegex = "[A-Za-z0-9_/-]+";
		if(role == null || role.isBlank() || !role.matches(usernameRegex)) {
			throw new IllegalArgumentException("Invalid role");
		}
		this.role = role;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
}
