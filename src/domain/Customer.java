package domain;

import java.time.LocalDate;
import java.util.List;

public interface Customer extends User {
	
	// getters

	public int getCustomerNr();
	
	public Company giveCompany();
	
	public List<Contract> giveContracts();

	public LocalDate getRegistrationDate();
	
	public int giveSeniority();	
	
}
