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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import languages.LanguageResource;

@Entity
@Access(AccessType.FIELD)
public class ActemiumCompany implements Company, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long companyId;

	@Transient
	private StringProperty name = new SimpleStringProperty();
	
	// Country
	// e.g. "United States"
	private String country;
	// (Postalcode (opt.)) + City
	// || City + State + zipCode
	// e.g. "9700 Oudenaarde"
	// e.g. "Mountain View, CA 94043"
	private String city;
	// Street + Nr. 
	// || Nr. + Street
	// e.g. "Bedrijvenpark Coupure 15"
	// e.g. "1600 Amphitheatre Parkway"
	private String address;
	
	private String phoneNumber;
	private LocalDate registrationDate;

	@OneToMany(mappedBy = "company"
//			   cascade = CascadeType.PERSIST
	)
	private List<ActemiumCustomer> contactPersons = new ArrayList<>();

	@OneToMany(mappedBy = "company",
				   cascade = CascadeType.PERSIST)
	private List<ActemiumTicket> actemiumTickets = new ArrayList<>();

	public ActemiumCompany() {		
		super();
	}
	
	public ActemiumCompany(String name, String country, String city, String address, String phoneNumber) {
		setName(name);
		setCountry(country);
		setCity(city);
		setAddress(address);
		setPhoneNumber(phoneNumber);
		setRegistrationDate(LocalDate.now());
	}

	@Access(AccessType.PROPERTY)
	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException(LanguageResource.getString("companyName_invalid"));
		}
		this.name.set(name);
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if (country == null || country.isBlank()) {
			throw new IllegalArgumentException(LanguageResource.getString("empty_country"));
		}
		this.country = country;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if (city == null || city.isBlank()) {
			throw new IllegalArgumentException(LanguageResource.getString("empty_city"));
		}
		this.city = city;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException(LanguageResource.getString("empty_address"));
		}
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		//TODO fix phoneNumberRegex
//		String phoneNumberRegex = "[0-9 /-]+";
//		if (phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches(phoneNumberRegex)) {
		if (phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches(".*\\d.*")) {
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

	public List<ActemiumCustomer> getContactPersons() {
		return contactPersons;
	}
	
	public List<Customer> giveContactPersons() {
		return (List<Customer>) (Object) contactPersons;
	}	

	public void addContactPerson(ActemiumCustomer contactPerson) {
		this.contactPersons.add(contactPerson);
	}

	public List<ActemiumTicket> getActemiumTickets() {
		return actemiumTickets;
	}
	
	public List<Ticket> giveTickets() {
		return (List<Ticket>) (Object) actemiumTickets;
	}

	public void addActemiumTicket(ActemiumTicket ticket) {
		this.actemiumTickets.add(ticket);
	}
}
