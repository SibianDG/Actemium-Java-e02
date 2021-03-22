package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface Ticket {

	public String getTicketIdString();	
//	public int getTicketIdInt();	

	public String getStatusAsString();
	
	public TicketStatus getStatus();

	public String getPriorityAsString();
	
	public TicketPriority getPriority();

	public String getTicketTypeAsString();

	public TicketType getTicketType();

	public LocalDate getDateOfCreation();

	public LocalDateTime getDateAndTimeOfCreation();

	public LocalDateTime getDateAndTimeOfCompletion();

	public String getTitle();

	public String getDescription();

	public Customer giveCustomer();

	public List<TicketComment> giveComments();

	public String getAttachments();
//	public List<String> getAttachments();
	
	public ObservableList<Employee> giveTechnicians();
	
	public String getSolution();

	public String getQuality();

	public String getSupportNeeded();

	public List<TicketChange> giveTicketChanges();
	
	public StringProperty titleProperty();
	
	public StringProperty priorityProperty();
	
	public StringProperty statusProperty();
	
//	public StringProperty ticketIdProperty();
	public IntegerProperty ticketIdProperty();

	public StringProperty ticketTypeProperty();
	
	public StringProperty completionDateProperty();
	
}
