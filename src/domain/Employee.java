package domain;

import java.time.LocalDate;
import java.util.List;

import domain.enums.EmployeeRole;
import javafx.beans.property.StringProperty;

public interface Employee extends User {

	// getters

	public int giveSeniority();

	public int getEmployeeNr();

	public String getAddress();

	public String getEmailAddress();

	public String getPhoneNumber();

	public LocalDate getRegistrationDate();

	public String getRole();

	public EmployeeRole getRoleAsEnum();

	public List<ActemiumTicket> getTickets();
	
	public StringProperty roleProperty();

}
