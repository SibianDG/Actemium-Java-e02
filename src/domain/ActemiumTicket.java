package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Transient;

import domain.enums.RequiredElement;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import exceptions.InformationRequiredException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


@Entity
@Access(AccessType.FIELD)
public class ActemiumTicket implements Ticket, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ticketId;
	
//	@Transient
//	private StringProperty ticketIdString = new SimpleStringProperty();
	@Transient
	private IntegerProperty ticketIdInt = new SimpleIntegerProperty();

	@Transient
	private StringProperty status = new SimpleStringProperty();
	@Transient
	private StringProperty priority = new SimpleStringProperty();
	private LocalDate dateOfCreation;

	// For statistics
	private LocalDateTime dateAndTimeOfCreation;
	private LocalDateTime dateAndTimeOfCompletion;

	@Transient
	private StringProperty title = new SimpleStringProperty();
	private String description;
	@ManyToOne
	private ActemiumCustomer customer;

	@ManyToOne
	private ActemiumCompany company;
	private String remarks;
	private String attachments;	
	// List of technicians contain all the technicians assigned to the ticket
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<ActemiumEmployee> technicians = new ArrayList<>();

	@Transient
	private StringProperty ticketType = new SimpleStringProperty();
	
	// Following list of additional data would prove usefull 
	// for further follow up on tickets and reporting:
	// - How was the solution acquired: through the KB or not. 
	//   This allows us to check if the KB must be 
	//   modified and expanded or not
	private String solution;
	// - Quality: the information was sufficient or not. 
	//   This would allow us to decided wether or not additional
	//   (mandatorry) fields are required when creating a ticket
	private String quality;
	// - Support needed: was the ticket created correctly? 
	//   E.g. a ticket was created but the wrong  
	//   department was assigned
	private String supportNeeded;
	
	public ActemiumTicket() {
		super();
	}


	//TODO:
	//remove CTOR & setters
	/*
	public ActemiumTicket(TicketPriority ticketPriority, TicketType ticketType, String title, String description, ActemiumCustomer customer,
			String remarks, String attachments) {
		super();
		// new ticket always gets TicketStatus CREATED
		setStatus(TicketStatus.CREATED);
		setTicketType(ticketType);
		setPriority(ticketPriority);
		setDateOfCreation(LocalDate.now());
		setDateAndTimeOfCreation(LocalDateTime.now());
		setTitle(title);
		setDescription(description);
		setCustomer(customer);
		setRemarks(remarks);
		setAttachments(attachments);
		setSolution("(not filled in yet)");
		setQuality("(not filled in yet)");
		setSupportNeeded("(not filled in yet)");
	}
	 */

	//TODO:
	//remove CTOR & setters
	// remarks and attachments are optional ( (none) is used so the field can be modified in gui)
	/*
	public ActemiumTicket(TicketPriority ticketPriority, TicketType ticketType, String title, String description, ActemiumCustomer customer) {
		this(ticketPriority, ticketType, title, description, customer, "(none)", "(none)");
	}

	 */

	private ActemiumTicket(TicketBuilder builder) throws InformationRequiredException {
		this.status.set(TicketStatus.CREATED.toString());
		this.priority.set(String.valueOf(builder.ticketPriority));
		this.ticketType.set(String.valueOf(builder.ticketType));
		//this.ticketType.set(String.valueOf());
		this.dateOfCreation = builder.dateOfCreation;
		this.dateAndTimeOfCreation = builder.dateAndTimeOfCreation;
		this.dateAndTimeOfCompletion = builder.dateAndTimeOfCompletion;
		this.title.set(builder.title);
		this.description = builder.description;
		this.customer = builder.customer;
		this.remarks = builder.remarks;
		this.solution = builder.solution;
		this.quality = builder.quality;
		this.supportNeeded = builder.supportNeeded;
		this.technicians = builder.technicians;
		this.attachments = builder.attachments;
	}
	
	public String getTicketIdString() {
		return String.valueOf(ticketId);
	}
	
	public int getTicketIdInt() {
		return (int) ticketId;
	}
	
	public void setTicketIdInt() {
		this.ticketIdInt.set((int) ticketId);
	}

	public String getStatusAsString() {
		return status.get();
	}

	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public TicketStatus getStatus() {
		return TicketStatus.valueOf(status.get());
	}

	public void setStatus(TicketStatus ticketStatus) throws InformationRequiredException {
		if (ticketStatus == TicketStatus.COMPLETED) {
			setDateAndTimeOfCompletion(LocalDateTime.now());
		}
		this.status.set(ticketStatus.toString());
		//checkAttributes();
	}

	public String getPriorityAsString() {
		return priority.get();
	}

	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public TicketPriority getPriority() {
		return TicketPriority.valueOf(priority.get());
	}

	public void setPriority(TicketPriority priority) throws InformationRequiredException {
		this.priority.set(String.valueOf(priority));

		//checkAttributes();
	}

	public String getTicketTypeAsString() {
		return ticketType.get();
	}

	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public TicketType getTicketType() {
		return TicketType.valueOf(ticketType.get());
	}

	public void setTicketType(TicketType ticketType) throws InformationRequiredException {
		this.ticketType.set(String.valueOf(ticketType));

		//@Transient
		//checkAttributes();
	}

	public LocalDate getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(LocalDate dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public LocalDateTime getDateAndTimeOfCreation() {
		return dateAndTimeOfCreation;
	}

	public void setDateAndTimeOfCreation(LocalDateTime dateAndTimeOfCreation) {
		this.dateAndTimeOfCreation = dateAndTimeOfCreation;
	}

	public LocalDateTime getDateAndTimeOfCompletion() {
		return dateAndTimeOfCompletion;
	}

	public void setDateAndTimeOfCompletion(LocalDateTime dateAndTimeOfCompletion) {
		this.dateAndTimeOfCompletion = dateAndTimeOfCompletion;
	}

	@Access(AccessType.PROPERTY)
	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) throws InformationRequiredException {
		//if (title == null || title.isBlank()) {
		//	throw new IllegalArgumentException(LanguageResource.getString("ticketTitle_invalid"));
		//}
		this.title.set(title);
		//checkAttributes();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws InformationRequiredException {
		//if (description == null || description.isBlank()) {
		//	throw new IllegalArgumentException(LanguageResource.getString("description_invalid"));
		//}
		this.description = description;
		//checkAttributes();
	}

	public ActemiumCustomer getCustomer() {
		return customer;
	}
	
	@Override
	public Customer giveCustomer() {
		return (Customer) customer;
	}


	public void setCustomer(ActemiumCustomer customer) throws InformationRequiredException {
		//if (customer == null) {
		//	throw new IllegalArgumentException(LanguageResource.getString("customerAssigned_invalid"));
		//}
		this.customer = customer;
		checkAttributes();
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		// Optional, can be null or blank
		this.remarks = remarks;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		// Optional, can be null or blank
		this.attachments = attachments;
	}
	
	public List<ActemiumEmployee> getTechnicians() {
		return technicians;
	}

	@Override
	public ObservableList<Employee> giveTechnicians() {
		return FXCollections.observableArrayList((List<Employee>) (Object) technicians);
	}
	
	public void setTechnicians(List<ActemiumEmployee> technicians) {
		this.technicians = technicians;
	}
	
	public void addTechnician(ActemiumEmployee technician) {
		technicians.add(technician);
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getSupportNeeded() {
		return supportNeeded;
	}

	public void setSupportNeeded(String supportNeeded) {
		this.supportNeeded = supportNeeded;
	}

	public StringProperty titleProperty() {
		return title;
	}
	
	public StringProperty priorityProperty() {
		return priority;
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	public IntegerProperty ticketIdProperty() {
		// must be set right before requesting the ticketIdProperty
		setTicketIdInt();
		return ticketIdInt;
	}

	public StringProperty ticketTypeProperty() {
		return ticketType;
	}

	public void checkAttributes() throws InformationRequiredException {
		//TODO: maybe not check all?
		//if you make a new TicketBuilder with the current set attributes, it will throw exception.
		// Ms. Malfait her idea
		new TicketBuilder()
				.ticketPriority(getPriority())
				.ticketStatus(getStatus())
				.ticketType(getTicketType())
				.title(this.title.get())
				.description(this.description)
				.customer(this.customer)
				//.dateOfCreation(this.dateOfCreation)
				//.dateAndTimeOfCreation(this.dateAndTimeOfCreation)
				//.dateAndTimeOfCompletion(this.dateAndTimeOfCompletion)
				//.remarks(this.remarks)
				//.attachments(this.attachments)
				//.solution(this.solution)
				//.quality(this.quality)
				//.supportNeeded(this.supportNeeded)
				.build();

	}

	public static class TicketBuilder {
		//private int ticketIdInt;
		//private ActemiumCompany company;


		private TicketStatus ticketStatus;
		private TicketPriority ticketPriority;
		private TicketType ticketType;
		private LocalDate dateOfCreation;
		private LocalDateTime dateAndTimeOfCreation;
		private LocalDateTime dateAndTimeOfCompletion;
		private String title;
		private String description;
		private ActemiumCustomer customer;
		private String remarks;
		private String attachments;
		private String solution;
		private String quality;
		private String supportNeeded;
		private List<ActemiumEmployee> technicians = new ArrayList<>();

		private Set<RequiredElement> requiredElements;


		public TicketBuilder ticketPriority(TicketPriority priority){
			this.ticketPriority = priority;
			return this;
		}

		public TicketBuilder ticketType(TicketType ticketType){
			this.ticketType = ticketType;
			return this;
		}
		public TicketBuilder title(String title){
			this.title = title;
			return this;
		}
		public TicketBuilder description(String description){
			this.description = description;
			return this;
		}
		public TicketBuilder customer(ActemiumCustomer customer){
			this.customer = customer;
			return this;
		}
		public TicketBuilder ticketStatus(TicketStatus ticketStatus){
			this.ticketStatus = ticketStatus;
			return this;
		}

		public TicketBuilder dateOfCreation(LocalDate dateOfCreation){
			this.dateOfCreation = dateOfCreation;
			return this;
		}
		public TicketBuilder dateAndTimeOfCreation(LocalDateTime dateAndTimeOfCreation){
			this.dateAndTimeOfCreation = dateAndTimeOfCreation;
			return this;
		}
		public TicketBuilder dateAndTimeOfCompletion(LocalDateTime dateAndTimeOfCompletion){
			this.dateAndTimeOfCompletion = dateAndTimeOfCompletion;
			return this;
		}
		public TicketBuilder remarks(String remarks){
			this.remarks = remarks;
			return this;
		}
		public TicketBuilder attachments(String attachments){
			this.attachments = attachments;
			return this;
		}
		public TicketBuilder solution(String solution){
			this.solution = solution;
			return this;
		}
		public TicketBuilder quality(String quality){
			this.quality = quality;
			return this;
		}
		public TicketBuilder supportNeeded(String supportNeeded){
			this.supportNeeded = supportNeeded;
			return this;
		}
		public TicketBuilder technicians(List<ActemiumEmployee> technicians){
			this.technicians = technicians;
			return this;
		}

		public ActemiumTicket build() throws InformationRequiredException {
			requiredElements = new HashSet<>();

			//ActemiumTicket ticket = ;
			checkAttributesTicketBuilder();
			return new ActemiumTicket(this);
		}

		public void checkAttributesTicketBuilder() throws InformationRequiredException {

			if (ticketPriority == null)
				requiredElements.add(RequiredElement.TicketPriorityRequired);
			if (ticketType == null)
				requiredElements.add(RequiredElement.TicketTypeRequired);
			if (title == null || title.isBlank())
				requiredElements.add(RequiredElement.TicketTitleRequired);
			if (description == null || description.isBlank())
				requiredElements.add(RequiredElement.TicketDescriptionRequired);
			if (customer == null)
				requiredElements.add(RequiredElement.TicketCustomerIDRequired);
			if (ticketStatus == null)
				this.ticketStatus = TicketStatus.CREATED;
			if (dateOfCreation == null)
				this.dateOfCreation = LocalDate.now();
			if (dateAndTimeOfCreation == null)
				this.dateAndTimeOfCreation = LocalDateTime.now();
			if (solution == null)
				this.solution = "(not filled in yet)";
			if (quality == null)
				this.quality = "(not filled in yet)";
			if (supportNeeded == null)
				this.supportNeeded = "(not filled in yet)";
			if (remarks == null)
				this.remarks = "(none)";
			if (attachments == null)
				this.attachments = "(none)";
			if (!requiredElements.isEmpty()) {
				//requiredElements.forEach(element -> System.out.println(element));
				throw new InformationRequiredException(requiredElements);
			}
		}

	}

	public ActemiumTicket clone() throws CloneNotSupportedException {

		ActemiumTicket cloned = null;
		try {
			cloned = new TicketBuilder()
					.ticketStatus(this.getStatus())
					.ticketPriority(this.getPriority())
					.ticketType(this.getTicketType())
					.title(this.getTitle())
					.description(this.getDescription())
					.customer(this.getCustomer())
					.remarks(this.getRemarks())
					.attachments(this.getAttachments())
					.solution(this.getSolution())
					.quality(this.getQuality())
					.supportNeeded(this.getSupportNeeded())
					.build();
		} catch (InformationRequiredException e) {
			//this should be a good Employee
			e.printStackTrace();
		}
		return cloned;
	}

}
