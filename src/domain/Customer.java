package domain;

import java.time.LocalDate;

public interface Customer extends User {
	
	// getters

	public int getCustomerNr();
	
	public ActemiumCompany getCompany();

	public LocalDate getRegistrationDate();
	
	public int giveSeniority();	
	
}
