package domain;

import languages.LanguageResource;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Employee extends UserModel implements Seniority, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long employeeNr;
	
	private String address;
	private String phoneNumber;
	private String emailAddress;
	@Enumerated(EnumType.STRING)
	private EmployeeRole role;
	private LocalDate registrationDate;

	public Employee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role) {
		super(username, password, firstName, lastName);
		setAddress(address);
		setPhoneNumber(phoneNumber);
		setEmailAddress(emailAddress);
		setRole(role);
		setRegistrationDate(LocalDate.now());
	}

	public Employee() {

	}

	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - registrationDate.getYear();
	}

	public long getEmployeeNr() {
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
			throw new IllegalArgumentException(LanguageResource.getString("address_invalid"));
		}
		this.address = address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		//String usernameRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		String emailregex = "^(.+)@(.+)$";
		if(emailAddress == null || emailAddress.isBlank() || !emailAddress.matches(emailregex)) {
			throw new IllegalArgumentException(LanguageResource.getString("email_invalid"));
		}
		this.emailAddress = emailAddress;
	}

	public EmployeeRole getRole() {
		return role;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		String usernameRegex = "[0-9 /-]+";
		if(phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches(usernameRegex)) {
			throw new IllegalArgumentException(LanguageResource.getString("phonenumber_invalid"));
		}
		this.phoneNumber = phoneNumber;
	}

	public void setRole(EmployeeRole role) {
		//String usernameRegex = "[A-Za-z0-9_/-]+";
		//if(role == null || role.isBlank() || !role.matches(usernameRegex)) {
		//	throw new IllegalArgumentException(LanguageResource.getString("role_invalid"));
		//}
		this.role = role;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
}
