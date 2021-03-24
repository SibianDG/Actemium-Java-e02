package domain;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.StringProperty;

/**
 * The interface Company.
 */
public interface Company {

	// getters

	public String getName();

	public String getCountry();

	public String getCity();

	public String getAddress();

	public String getPhoneNumber();

	public LocalDate getRegistrationDate();

	public StringProperty nameProperty();

	public List<Customer> giveContactPersons();

	public List<Ticket> giveTickets();
}
