package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.FIELD)
public class ActemiumTicket implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ticketId;

	@Enumerated(EnumType.STRING)
	private TicketStatus ticketStatus;
	@Enumerated(EnumType.STRING)
	private TicketPriority ticketPriority;
	private LocalDate dateOfCreation;
	private String title;
	private String description;
	@ManyToOne
	private Customer customer;
//	@ManyToMany(mappedBy = "employee")
	private List<Employee> technicians = new ArrayList<>();
	private String remarks;
	private String attachments;

	public ActemiumTicket() {
		super();
	}

	public ActemiumTicket(TicketPriority ticketPriority, String title, String description, Customer customer,
			String remarks, String attachments) {
		super();
		// new ticket always gets TicketStatus CREATED
		this.ticketStatus = TicketStatus.CREATED;
		this.ticketPriority = ticketPriority;
		this.dateOfCreation = LocalDate.now();
		this.title = title;
		this.description = description;
		this.customer = customer;
		this.remarks = remarks;
		this.attachments = attachments;
	}

	// remarks and attachments are optional
	public ActemiumTicket(TicketPriority ticketPriority, String title, String description, Customer customer) {
		this(ticketPriority, title, description, customer, null, null);
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public TicketPriority getTicketPriority() {
		return ticketPriority;
	}

	public void setTicketPriority(TicketPriority ticketPriority) {
		this.ticketPriority = ticketPriority;
	}

	public LocalDate getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(LocalDate dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Employee> getTechnicians() {
		return technicians;
	}

	public void setTechnicians(List<Employee> technicians) {
		this.technicians = technicians;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public void addTechnician(Employee technician) {
		technicians.add(technician);
	}

}
