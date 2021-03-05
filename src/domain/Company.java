package domain;

import java.time.LocalDate;

public interface Company {

	// getters

	public String getName();

	public String getAddress();

	public String getPhoneNumber();

	public LocalDate getRegistrationDate();
}
