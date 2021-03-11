package domain;

import java.time.LocalDate;

public interface Customer extends User {
	
	// getters

	public int getCustomerNr();
	
	public Company giveCompany();

	public LocalDate getRegistrationDate();
	
	public int giveSeniority();	
	
}
