package domain;

import java.time.LocalDate;
import java.util.List;

import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface Ticket {

	public String getTicketIdString();	

	public String getStatus();
	
	// probably not needed
	public TicketStatus getStatusAsEnum();

	public String getPriority();
	
	// probably not needed
	public TicketPriority getPriorityAsEnum();

	public String getTicketType();

	// probably not needed
	public TicketType getTicketTypeAsEnum();

	public LocalDate getDateOfCreation();

	public String getTitle();

	public String getDescription();

	public Customer giveCustomer();

	public String getRemarks();

	public String getAttachments();
	
	public ObservableList<Employee> giveTechnicians();
	
	public String getSolution();

	public String getQuality();

	public String getSupportNeeded();
	
	public StringProperty titleProperty();
	
	public StringProperty priorityProperty();
	
	public StringProperty statusProperty();
	
	public StringProperty ticketIdProperty();

	public StringProperty ticketTypeProperty();
	
}
