package domain;

import java.time.LocalDate;

import javafx.beans.property.StringProperty;

public interface Company {

	// getters

	public String getName();

	public String getAddress();

	public String getPhoneNumber();

	public LocalDate getRegistrationDate();

	public StringProperty nameProperty();
}
