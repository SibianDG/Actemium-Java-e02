package domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class ContactPerson implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	@Id
	private String emailAddress;

	//@ManyToMany
	//private List<Customer> customerList;

	public ContactPerson() {
		super();
	}

	public ContactPerson(String firstName, String lastName, String emailAddress) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
}
