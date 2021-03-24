package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The type Actemium company.
 */
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

	/**
	 * Instantiates a new Actemium company.
	 */
	public ActemiumCompany() {
		super();
	}

	/**
	 * Instantiates a new Actemium company.
	 *
	 * @param builder the builder
	 */
	public ActemiumCompany(CompanyBuilder builder) {
		this.name.set(builder.name);
		this.country = builder.country;
		this.city = builder.city;
		this.address = builder.address;
		this.phoneNumber = builder.phoneNumber;
		this.registrationDate = builder.registrationDate;
	}

	/**
	 * Gets name.
	 *
	 * @return name
	 */
	@Access(AccessType.PROPERTY)
	public String getName() {
		return name.get();
	}

	/**
	 *
	 * @return nameProperty
	 */
	public StringProperty nameProperty() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name.set(name);
	}

	/**
	 *
	 * @return country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets country.
	 *
	 * @param country the country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 *
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets city.
	 *
	 * @param city the city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 *
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets address.
	 *
	 * @param address the address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 *
	 * @return phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets phone number.
	 *
	 * @param phoneNumber the phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 *
	 * @return registration date
	 */
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * Sets registration date.
	 *
	 * @param registrationDate the registration date
	 */
	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * Gets contact persons.
	 *
	 * @return the contact persons
	 */
	public List<ActemiumCustomer> getContactPersons() {
		return contactPersons;
	}

	/**
	 * give contact persons
	 *
	 * @return contact persons list
	 */
	public List<Customer> giveContactPersons() {
		return (List<Customer>) (Object) contactPersons;
	}

	/**
	 * Add contact person.
	 *
	 * @param contactPerson the contact person
	 */
	public void addContactPerson(ActemiumCustomer contactPerson) {
		this.contactPersons.add(contactPerson);
	}

	/**
	 * Gets actemium tickets.
	 *
	 * @return the actemium tickets
	 */
	public List<ActemiumTicket> getActemiumTickets() {
		return actemiumTickets;
	}


	public List<Ticket> giveTickets() {
		return (List<Ticket>) (Object) actemiumTickets;
	}

	/**
	 * Add actemium ticket.
	 *
	 * @param ticket the ticket
	 */
	public void addActemiumTicket(ActemiumTicket ticket) {
		this.actemiumTickets.add(ticket);
	}

	/**
	 * Check attributes. This method is when you make changes to the object, that the attributes are indeed valid.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		// Ms. Malfait her idea
		new ActemiumCompany.CompanyBuilder()
				.name(this.getName())
				.country(this.getCountry())
				.city(this.getCity())
				.address(this.getAddress())
				.phoneNumber(this.getPhoneNumber())
				.registrationDate(this.getRegistrationDate())
				.build();
	}

	/**
	 * The type Company builder.
	 */
	public static class CompanyBuilder {
		private String name;
		private String country;
		private String city;
		private String address;
		private String phoneNumber;
		private LocalDate registrationDate;


		private Set<RequiredElement> requiredElements;

		/**
		 * Name company builder.
		 *
		 * @param name the name
		 * @return the company builder
		 */
		public CompanyBuilder name(String name){
			this.name = name;
			return this;
		}

		/**
		 * Country company builder.
		 *
		 * @param country the country
		 * @return the company builder
		 */
		public CompanyBuilder country(String country){
			this.country = country;
			return this;
		}

		/**
		 * City company builder.
		 *
		 * @param city the city
		 * @return the company builder
		 */
		public CompanyBuilder city(String city){
			this.city = city;
			return this;
		}

		/**
		 * Address company builder.
		 *
		 * @param address the address
		 * @return the company builder
		 */
		public CompanyBuilder address(String address){
			this.address = address;
			return this;
		}

		/**
		 * Phone number company builder.
		 *
		 * @param phoneNumber the phone number
		 * @return the company builder
		 */
		public CompanyBuilder phoneNumber(String phoneNumber){
			this.phoneNumber = phoneNumber;
			return this;
		}

		/**
		 * Registration date company builder.
		 *
		 * @param registrationDate the registration date
		 * @return the company builder
		 */
		public CompanyBuilder registrationDate(LocalDate registrationDate){
			this.registrationDate = registrationDate;
			return this;
		}

		/**
		 * Build actemium company.
		 *
		 * @return the actemium company
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumCompany build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesEmployeeBuiler();
			return new ActemiumCompany(this);
		}

		private void checkAttributesEmployeeBuiler() throws InformationRequiredException {
			if (name == null || name.isBlank())
				requiredElements.add(RequiredElement.CompanyNameRequired);
			if (country == null || country.isBlank())
				requiredElements.add(RequiredElement.CompanyCountryRequired);
			if (city == null || city.isBlank())
				requiredElements.add(RequiredElement.CompanyCirtyRequired);
			if (address == null || address.isBlank())
				requiredElements.add(RequiredElement.CompanyAddressRequired);
			if (phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches("\\+?[0-9 /-]+"))
				requiredElements.add(RequiredElement.CompanyPhoneRequired);
			if (registrationDate == null)
				registrationDate = LocalDate.now();

			if (!requiredElements.isEmpty()) {
				throw new InformationRequiredException(requiredElements);
			}
		}
	}

	/**
	 * This clones an actemium company.
	 *
	 * @return Actemium Company
	 * @throws CloneNotSupportedException throws a clone not supported exception
	 */
	@Override
	public ActemiumCompany clone() throws CloneNotSupportedException {

		ActemiumCompany cloned = null;
		try {
			cloned = new CompanyBuilder()
					.name(this.getName())
					.country(this.getCountry())
					.city(this.getCity())
					.address(this.getAddress())
					.phoneNumber(this.getPhoneNumber())
					.registrationDate(this.getRegistrationDate())
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
		return cloned;
	}
}
