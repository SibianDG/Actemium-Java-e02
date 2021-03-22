package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import domain.enums.EmployeeRole;
import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Access(AccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = "Employee.findByEmail",
				query = "SELECT e FROM ActemiumEmployee e WHERE e.emailAddress = :emailAdress"),
})
public class ActemiumEmployee extends UserModel implements Employee, Seniority, Serializable, Comparable {

	@Serial
	private static final long serialVersionUID = 1L;

	//ToDo: when on SQL SERVER Turn on
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int employeeNr;

	private String address;
	private String phoneNumber;
	private String emailAddress;
	@Transient
	private StringProperty role = new SimpleStringProperty();

	private LocalDate registrationDate;
	
//	@ManyToMany(mappedBy = "technicians", cascade = CascadeType.PERSIST)
//	private List<ActemiumTicket> tickets = new ArrayList<>();
	
//	@ManyToMany
//	private List<Customer> customers;

	/*public ActemiumEmployee(String username, String password, String firstName, String lastName, String address,
			String phoneNumber, String emailAddress, EmployeeRole role) {
		super(username, password, firstName, lastName);
		setAddress(address);
		setPhoneNumber(phoneNumber);
		setEmailAddress(emailAddress);
		setRole(role);
		setRegistrationDate(LocalDate.now());
	}*/

	public ActemiumEmployee(EmployeeBuilder builder){
		super(builder.username, builder.password, builder.firstName, builder.lastName);
		this.address = builder.address;
		this.phoneNumber = builder.phoneNumber;
		this.emailAddress = builder.emailAddress;
		this.roleProperty().set(String.valueOf(builder.role));
		this.registrationDate = builder.registrationDate;
	}

	public ActemiumEmployee() {
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
		//String usernameRegex = "[A-Za-z0-9 _-]+";
		//if (address == null || address.isBlank() || !address.matches(usernameRegex)) {
		//	throw new IllegalArgumentException(LanguageResource.getString("address_invalid"));
		//}
		this.address = address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		//String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+[.][a-zA-Z0-9-]{2,4}$";
		////TODO the regex below doesn't work because we create employees with a false email according to this regex in PopulateDB
		//// "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		//if (emailAddress == null || emailAddress.isBlank() || !emailAddress.matches(emailRegex)) {
		//	throw new IllegalArgumentException(LanguageResource.getString("email_invalid"));
		//}
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		//String usernameRegex = "[0-9 /-]+";
		//if (phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches(usernameRegex)) {
		//	throw new IllegalArgumentException(LanguageResource.getString("phonenumber_invalid"));
		//}
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getRoleAsString() {
		return role.get();
	}

	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public EmployeeRole getRole() {
		return EmployeeRole.valueOf(role.get());
	}

	public void setRole(EmployeeRole role) {
		//if (role == null) {
		//	throw new IllegalArgumentException(LanguageResource.getString("role_invalid"));
		//}
		this.roleProperty().set(role.toString());
	}

//	public List<ActemiumTicket> getTickets() {
//		return tickets;
//	}
//	
//	public List<Ticket> giveTickets() {
//		return (List<Ticket>) (Object) tickets;
//	}
//
//	public void setTickets(List<ActemiumTicket> tickets) {
//		this.tickets = tickets;
//	}

	public StringProperty roleProperty() {
		return role;
	}

	public void checkAttributes() throws InformationRequiredException {
		// Ms. Malfait her idea
		new EmployeeBuilder()
				.username(super.usernameProperty().get())
				.password(super.getPassword())
				.firstName(super.firstNameProperty().get())
				.lastName(super.lastNameProperty().get())
				.address(this.getAddress())
				.phoneNumber(this.getPhoneNumber())
				.emailAddress(this.getEmailAddress())
				.role(this.getRole())
				.registrationDate(this.getRegistrationDate())
				.build();
	}

	public static class EmployeeBuilder {
		private String username;
		private String password;
		private String firstName;
		private String lastName;

		private String address;
		private String phoneNumber;
		private String emailAddress;

		private EmployeeRole role;
		private LocalDate registrationDate;

		private Set<RequiredElement> requiredElements;

		public EmployeeBuilder username(String username) {
			this.username = username;
			return this;
		}
		public EmployeeBuilder password(String password) {
			this.password = password;
			return this;
		}
		public EmployeeBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		public EmployeeBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		public EmployeeBuilder address(String address) {
			this.address = address;
			return this;
		}
		public EmployeeBuilder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}
		public EmployeeBuilder emailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}
		public EmployeeBuilder role(EmployeeRole role) {
			this.role = role;
			return this;
		}
		public EmployeeBuilder registrationDate(LocalDate registrationDate) {
			this.registrationDate = registrationDate;
			return this;
		}

		public ActemiumEmployee build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesEmployeeBuiler();
			return new ActemiumEmployee(this);
		}

		private void checkAttributesEmployeeBuiler() throws InformationRequiredException {
			if (username == null || username.isBlank() || !username.matches("[A-Za-z0-9]+"))
				requiredElements.add(RequiredElement.UsernameRequired);
			if(password == null || password.isBlank() || !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()â€“{}:;',.?/*~$^+=<>]).{8,}$"))
				requiredElements.add(RequiredElement.PasswordRequired);
			if(firstName == null || firstName.isBlank() || !firstName.matches("[^0-9]+"))
				requiredElements.add(RequiredElement.FirstnameRequired);
			if(lastName == null || lastName.isBlank() || !lastName.matches("[^0-9]+"))
				requiredElements.add(RequiredElement.LastnameRequired);
			if (address == null || address.isBlank() || !address.matches("[A-Za-z0-9 _-]+"))
				requiredElements.add(RequiredElement.AddressRequired);
			if (phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches("[0-9 /-]+"))
				requiredElements.add(RequiredElement.PhoneRequired);
			if (emailAddress == null || emailAddress.isBlank() || !emailAddress.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+[.][a-zA-Z0-9-]{2,4}$"))
				requiredElements.add(RequiredElement.EmailRequired);
			if (role == null)
				requiredElements.add(RequiredElement.EmployeeRoleRequired);
			if (registrationDate == null)
				registrationDate = LocalDate.now();

			if (!requiredElements.isEmpty()) {
				requiredElements.forEach(System.out::println);
				throw new InformationRequiredException(requiredElements);
			}
		}

	}

	public ActemiumEmployee clone() throws CloneNotSupportedException {

		ActemiumEmployee cloned = null;
		try {
			cloned = new EmployeeBuilder()
					.username(this.usernameProperty().get())
					.password(this.getPassword())
					.firstName(this.firstNameProperty().get())
					.lastName(this.lastNameProperty().get())
					.address(this.getAddress())
					.phoneNumber(this.getPhoneNumber())
					.emailAddress(this.getEmailAddress())
					.role(this.getRole())
					.build();
		} catch (InformationRequiredException e) {
			//this should be a good Employee
			e.printStackTrace();
		}
		return cloned;
	}

	@Override
	public int compareTo(Object o) {		
		return Integer.compare((int)getUserId(), (int)((ActemiumEmployee) o).getUserId());
	}
	
}
