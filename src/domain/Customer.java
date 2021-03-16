package domain;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.ObservableList;

public interface Customer extends User {
	
	// getters

	public int getCustomerNr();
	
	public Company giveCompany();
	
	public ObservableList<Contract> giveContracts();

	public LocalDate getRegistrationDate();
	
	public int giveSeniority();	
	
}
