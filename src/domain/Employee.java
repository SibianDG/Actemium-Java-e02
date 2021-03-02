package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import languages.LanguageResource;

@Entity
@Access(AccessType.FIELD)
public class Employee extends UserModel implements Seniority, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	//ToDo: when on SQL SERVER Turn on
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeNr;

	private String address;
	private String phoneNumber;
	private String emailAddress;
	@Transient
	private StringProperty role = new SimpleStringProperty();

	private LocalDate registrationDate;
	
	@ManyToMany(mappedBy = "technicians", cascade = CascadeType.PERSIST)
	private List<ActemiumTicket> tickets = new ArrayList<>();
	
//	@ManyToMany
//	private List<Customer> customers;

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
		super();
	}

	@Override
	public int giveSeniority() {
		return LocalDate.now().getYear() - registrationDate.getYear();
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
		if (address == null || address.isBlank() || !address.matches(usernameRegex)) {
			throw new IllegalArgumentException(LanguageResource.getString("address_invalid"));
		}
		this.address = address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		// String usernameRegex =
		// "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		String emailregex = "^(.+)@(.+)$";
		if (emailAddress == null || emailAddress.isBlank() || !emailAddress.matches(emailregex)) {
			throw new IllegalArgumentException(LanguageResource.getString("email_invalid"));
		}
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		String usernameRegex = "[0-9 /-]+";
		if (phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches(usernameRegex)) {
			throw new IllegalArgumentException(LanguageResource.getString("phonenumber_invalid"));
		}
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getRole() {
		return role.get();
	}

	public EmployeeRole getRoleAsEnum() {
		return EmployeeRole.valueOf(role.get());
	}

	public void setRole(EmployeeRole role) {
		System.out.println(role);
		if (role == null) {
			throw new IllegalArgumentException(LanguageResource.getString("role_invalid"));
		}
		this.roleProperty().set(role.toString());
	}

	public List<ActemiumTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<ActemiumTicket> tickets) {
		this.tickets = tickets;
	}

	public StringProperty roleProperty() {
		return role;
	}
	
}
